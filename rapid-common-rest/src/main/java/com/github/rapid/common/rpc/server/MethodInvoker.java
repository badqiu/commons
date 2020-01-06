package com.github.rapid.common.rpc.server;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.github.rapid.common.rpc.RPCConstants;
import com.github.rapid.common.rpc.SerDe;
import com.github.rapid.common.rpc.SerDeMapping;
import com.github.rapid.common.rpc.serde.SimpleSerDeImpl;
import com.github.rapid.common.rpc.util.ParameterEscapeUtil;

/**
 * Java对象方法调用器，通过指定method反射调用类的方法
 * 
 * @author badqiu
 * @see MethodInvoker#invoke(String, String, Map)
 *
 */
public class MethodInvoker {
//	public static final String KEY_VERSION = "__version"; // TODO 协议版本 
	public static final String KEY_FORMAT = RPCConstants.KEY_FORMAT; // 协议返回值格式: json,xml
	public static final String KEY_PROTOCOL = RPCConstants.KEY_PROTOCOL; // 通讯协议,用于决定参数的传递方式 
	public static final String KEY_PARAMETERS = RPCConstants.KEY_PARAMETERS; // 参数值列表
	
	// 参数协议类型
	public static final String PROTOCOL_KEYVALUE = RPCConstants.PROTOCOL_KEYVALUE; // param1Name=param2Value&param2Name=param2Value
	public static final String PROTOCOL_ARRAY = RPCConstants.PROTOCOL_ARRAY; // __params=param1Value;param2Value
	public static final String PROTOCOL_JSON = RPCConstants.PROTOCOL_JSON;  //  __params=["param1Value",param2Value]
	public static final String PROTOCOL_JAVA = RPCConstants.PROTOCOL_JAVA;  //  __params=["param1Value",param2Value]
	
	
	private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
	private Map<String,Object> serviceMapping = new HashMap<String,Object>();
	
	public Object invoke(String serviceId,String methodName,Map<String,Object> params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, SecurityException, NoSuchMethodException{
		return invoke(serviceId,methodName,params,null);
	}
	
	public Object invoke(String serviceId,String methodName,Map<String,Object> params,byte[] body) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, SecurityException, NoSuchMethodException {
		Object service = lookupService(serviceId);
		Method method = lookupMethod(service,methodName);
		String protocol = resolveProtocol(params,method);
		Object[] arguments = deserializeForParameters(params, method,protocol,body);
		
		
		beforeMethodInvoke(method,arguments);
		Object result = method.invoke(service, arguments); //TODO 方法调用前应该有拦截器 FIXME: 反射是否有性能问题
		return afterMethodInvoke(method,result);
	}

	protected Object afterMethodInvoke(Method method, Object result) {
		return result;
	}

	protected void beforeMethodInvoke(Method method,Object[] arguments) {
	}

	private String resolveProtocol(Map<String, Object> params, Method method) {
		String protocol = (String)params.get(KEY_PROTOCOL);
		if(StringUtils.isNotEmpty(protocol)) {
			return protocol;
		}
		if(params.containsKey(KEY_PARAMETERS)) { 
			return PROTOCOL_ARRAY;
		}else {
			return PROTOCOL_KEYVALUE;
		}
	}

	SimpleSerDeImpl simpleSerDe = new SimpleSerDeImpl();
	protected Object[] deserializeForParameters(Map<String, Object> params,
			Method method,String protocol,byte[] body) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		Object[] parameters = new Object[method.getParameterTypes().length];
		SerDe serDe = null;
		if(PROTOCOL_ARRAY.equals(protocol)) {
			String parametersStringValue = (String)params.get(KEY_PARAMETERS);
			List<String> arguments = Arrays.asList(StringUtils.split(parametersStringValue, SimpleSerDeImpl.PARAMETERS_SEPERATOR)); 
			for(int i = 0; i < arguments.size(); i++) {
				String rawValue = ParameterEscapeUtil.unescapeParameter(arguments.get(i));
				Class<?> parameterType = method.getParameterTypes()[i];
				parameters[i] = simpleSerDe.deserializeParameterValue(parameterType,rawValue,params);
			}
		}else if(PROTOCOL_KEYVALUE.equals(protocol)){
			String[] parameterNames = getParameterNames(method);
			Assert.notNull(parameterNames,"not found parameterNames for method:"+method+" by parameterNameDiscoverer:"+parameterNameDiscoverer);
			Class[] paramTypes = method.getParameterTypes();
			for(int i = 0; i < parameters.length; i++) {
				String name = parameterNames[i];
				if(name == null) {
					throw new RuntimeException("fatal error,cannot get parameter name on method:"+method+"; current javaVersion:"+System.getProperty("java.version")+", required javaVersion must be java8");
				}
				
				Class<?> parameterType = paramTypes[i];
				Object rawValue = params.get(name);
				parameters[i] = simpleSerDe.deserializeParameterValue(parameterType,rawValue,params);
			}
		}else if(PROTOCOL_JSON.equals(protocol)) {
			String parametersStringValue = (String)params.get(KEY_PARAMETERS);
			try {
				parameters = deserializationParameterByJson(method,parametersStringValue,body);
			}catch(Exception e) {
				throw new IllegalArgumentException("cannot process json arguments:"+parametersStringValue+" for method:"+method,e);
			}
		}else if((serDe = SerDeMapping.DEFAULT_MAPPING.getSerDeByFormat(protocol)) != null) {
			parameters = (Object[])serDe.deserialize(new ByteArrayInputStream(body), method.getGenericReturnType(), params);
		}else {
			throw new RuntimeException("invalid protocol:"+protocol+" legal protocals:json,kv,array,java");
		}
		return parameters;
	}

	ConcurrentHashMap<Method,String[]> parameterNamesCache = new ConcurrentHashMap<Method,String[]>();
	private String[] getParameterNames(Method method) {
//		if(JavaVersionUtil.isGreatthanJava8()) {
//			Parameter[] params = method.getParameters();
//			String[] names = new String[params.length];
//			for(int i = 0; i < params.length; i++) {
//				names[i] = params[i].getName();
//			}
//			return names;
//		}else {
			String[] parameterNames = parameterNamesCache.get(method);
			if(parameterNames == null) {
				parameterNames = parameterNameDiscoverer.getParameterNames(method);
				parameterNamesCache.put(method, parameterNames);
			}
			return parameterNames;
//		}
	}

//	ObjectMapper objectMapper = new ObjectMapper(); 
//	{
////		objectMapper.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//	}
//	private Object[] deserializationParameterByJson(Method method,
//			String parametersStringValue,byte[] body)
//			throws Exception{
//		Object[] parameters = new Object[method.getParameterTypes().length];
//		Type[] parameterTypes = method.getGenericParameterTypes();
//		JsonNode arrayNode = StringUtils.isEmpty(parametersStringValue) ? objectMapper.readTree(body) : objectMapper.readTree(parametersStringValue);
//		Iterator<JsonNode> elems = arrayNode.elements();
//		int i = 0;
//		for(JsonNode arg = null; elems.hasNext(); i++){
//			arg = elems.next();
//			Object argumentValue = objectMapper.readValue(arg.traverse(), objectMapper.constructType((parameterTypes[i])));
//			parameters[i] = argumentValue;
//		}
//		return parameters;
//	}
	
	private Object[] deserializationParameterByJson(Method method,
			String parametersStringValue,byte[] body)
			throws Exception{
		Object[] parameters = new Object[method.getParameterTypes().length];
		Type[] parameterTypes = method.getGenericParameterTypes();
		
		List<Object> arrayNode = StringUtils.isEmpty(parametersStringValue) ? JSON.parseArray(IOUtils.toString(body,"UTF-8"),parameterTypes) : JSON.parseArray(parametersStringValue,parameterTypes);
		
		Iterator<Object> elems = arrayNode.iterator();
		int i = 0;
		for(Object arg = null; elems.hasNext(); i++){
			arg = elems.next();
			Object argumentValue = arg;
			parameters[i] = argumentValue;
		}
		return parameters;
	}

	private Object lookupService(String serviceId) {
		Object service = serviceMapping.get(serviceId);
		if(service == null) {
			throw new IllegalArgumentException("not found service object by id:"+serviceId);
		}
		return service;
	}

	

	/**
	 * TODO 只有接口暴露的方法才能被调用
	 * @param serviceObject
	 * @param methodName
	 * @return
	 */
	Map<Object,Map<String,Method>> cachedMethod = new HashMap<Object,Map<String,Method>>();
	private Method lookupMethod(Object serviceObject, String methodName) {
		Map<String,Method> methods = cachedMethod.get(serviceObject);
		if(methods == null) {
			throw new IllegalArgumentException("not found methods by object:"+serviceObject+", methodName:"+methodName);
		}
		Method result = methods.get(methodName);
		if(result == null) {
			throw new IllegalArgumentException("not found method by:"+methodName+" on object:"+serviceObject);
		}
		return result;
	}
	
	public Map<String, Object> getServiceMapping() {
		return serviceMapping;
	}

	public void setServiceMapping(Map<String, Object> serviceMapping) {
		this.serviceMapping = serviceMapping;
		
		for(Map.Entry<String, Object> entry : serviceMapping.entrySet()) {
			Method[] methods = entry.getValue().getClass().getDeclaredMethods();
			Map<String,Method> methodMapping = new HashMap<String,Method>();
			for(Method m : methods) {
				if(Modifier.isPublic(m.getModifiers())) {
					if(methodMapping.containsKey(m.getName())) {
						throw new IllegalArgumentException("method names must be unique,already has method:"+m.getName()+" full:"+m);
					}
					methodMapping.put(m.getName(), m);
				}
			}
			cachedMethod.put(entry.getValue(), methodMapping);
		}
	}
	
	public void addService(String serviceId,Object serviceObject,Class serviceInterface) {
		Assert.hasText(serviceId,"serviceId must be not empty");
		Assert.notNull(serviceObject,"serviceObject must be not null");
		Assert.notNull(serviceInterface,"serviceInterface must be not null");
		
		serviceMapping.put(serviceId, serviceObject);
		
		Method[] methods = serviceInterface.getDeclaredMethods();
		Map<String,Method> methodMapping = new HashMap<String,Method>();
		for(Method m : methods) {
			if(Modifier.isPublic(m.getModifiers())) {
				if(methodMapping.containsKey(m.getName())) {
					throw new IllegalArgumentException("method names must be unique,already has method:"+m.getName()+" full:"+m);
				}
//				methodMapping.put(m.getName(), m);
				Method serviceObjectMethod = findMethod(serviceObject,m);
				if(serviceObjectMethod == null) {
					throw new IllegalArgumentException("not found serviceObject method by name:"+m.getName());
				}
				methodMapping.put(m.getName(), serviceObjectMethod);
			}
		}
		cachedMethod.put(serviceObject, methodMapping);
	}

	private Method findMethod(Object serviceObject, Method m) {
		for(Method method : serviceObject.getClass().getDeclaredMethods()) {
			if(method.getName().equals(m.getName())) {
				Class[] types = method.getParameterTypes();
				if(types.length != m.getParameterTypes().length) {
					continue;
				}
				boolean isEquals = true;
				for(int i = 0; i < types.length ;i ++) {
					if(types[i] != m.getParameterTypes()[i]) {
						isEquals = false;
					}
				}
				if(isEquals) {
					return method;
				}
			}
		}
		return null;
	}

	public ParameterNameDiscoverer getParameterNameDiscoverer() {
		return parameterNameDiscoverer;
	}

	public void setParameterNameDiscoverer(
			ParameterNameDiscoverer parameterNameDiscoverer) {
		this.parameterNameDiscoverer = parameterNameDiscoverer;
	}
	
	
}
