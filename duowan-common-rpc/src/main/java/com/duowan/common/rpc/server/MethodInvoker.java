package com.duowan.common.rpc.server;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.convert.ConversionService;
import org.springframework.util.Assert;

import com.duowan.common.rpc.util.BeanUtilsConvertRegisterHelper;
import com.duowan.common.rpc.util.FastBeanUtil;
import com.duowan.common.rpc.util.ParameterEscapeUtil;
import com.duowan.common.rpc.util.PrimitiveTypeUtil;

/**
 * Java对象方法调用器，通过指定method反射调用类的方法
 * 
 * @author badqiu
 * @see MethodInvoker#invoke(String, String, Map)
 *
 */
public class MethodInvoker {
//	public static final String KEY_VERSION = "__version"; // TODO 协议版本 
	public static final String KEY_FORMAT = "__format"; // 协议返回值格式: json,xml
	public static final String KEY_PROTOCOL = "__protocol"; // 通讯协议,用于决定参数的传递方式 
	public static final String KEY_PARAMETERS = "__params"; // 参数值列表
	
	// 参数协议类型
	public static final String PROTOCOL_KEYVALUE = "kv"; // param1Name=param2Value&param2Name=param2Value
	public static final String PROTOCOL_ARRAY = "array"; // __params=param1Value;param2Value
	public static final String PROTOCOL_JSON = "json";  //  __params=["param1Value",param2Value]
	
	public static final String NULL_VALUE = "null";
	
	private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
	private Map<String,Object> serviceMapping = new HashMap<String,Object>();
	
	private ConversionService conversionService = null;
	{
		ConversionServiceFactoryBean factory = new ConversionServiceFactoryBean();
		factory.afterPropertiesSet();
		conversionService = factory.getObject();
	}
	
	public Object invoke(String serviceId,String methodName,Map<String,Object> params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, InstantiationException, SecurityException, NoSuchMethodException {
		Object service = lookupService(serviceId);
		Method method = lookupMethod(service,methodName);
		String protocol = resolveProtocol(params,method);
		Object[] parameters = deserializeForParameters(params, method,protocol);
		
		return method.invoke(service, parameters); //TODO 方法调用前应该有拦截器
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

	private Object[] deserializeForParameters(Map<String, Object> params,
			Method method,String protocol) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		Object[] parameters = new Object[method.getParameterTypes().length];
		if(PROTOCOL_ARRAY.equals(protocol)) {
			String parametersStringValue = (String)params.get(KEY_PARAMETERS);
			List<String> arguments = Arrays.asList(StringUtils.split(parametersStringValue, PARAMETERS_SEPERATOR)); 
			for(int i = 0; i < arguments.size(); i++) {
				String rawValue = ParameterEscapeUtil.unescapeParameter(arguments.get(i));
				Class<?> parameterType = method.getParameterTypes()[i];
				parameters[i] = deserializeParameterValue(parameterType,rawValue,params);
			}
		}else if(PROTOCOL_KEYVALUE.equals(protocol)){
			String[] parameterNames = getParameterNames(method);
			Assert.notNull(parameterNames,"not found parameterNames for method:"+method+" by parameterNameDiscoverer:"+parameterNameDiscoverer);
			Class[] paramTypes = method.getParameterTypes();
			for(int i = 0; i < parameters.length; i++) {
				String name = parameterNames[i];
				Class<?> parameterType = paramTypes[i];
				Object rawValue = params.get(name);
				parameters[i] = deserializeParameterValue(parameterType,rawValue,params);
			}
		}else if(PROTOCOL_JSON.equals(protocol)) {
			String parametersStringValue = (String)params.get(KEY_PARAMETERS);
			try {
				parameters = deserializationParameterByJson(method,parametersStringValue);
			}catch(JsonProcessingException e) {
				throw new IllegalArgumentException("cannot process json arguments"+parametersStringValue+" for method:"+method,e);
			}catch(IOException e) {
				throw new IllegalArgumentException("cannot process json arguments"+parametersStringValue+" for method:"+method,e);
			}
		}else {
			throw new RuntimeException("invalid protocol:"+protocol+" legal protocals:json,kv,array");
		}
		return parameters;
	}

	ConcurrentHashMap<Method,String[]> parameterNamesCache = new ConcurrentHashMap<Method,String[]>();
	private String[] getParameterNames(Method method) {
		String[] parameterNames = parameterNamesCache.get(method);
		if(parameterNames == null) {
			parameterNames = parameterNameDiscoverer.getParameterNames(method);
			parameterNamesCache.put(method, parameterNames);
		}
		return parameterNames;
	}

	private Object[] deserializationParameterByJson(Method method,
			String parametersStringValue)
			throws IOException, JsonProcessingException, JsonParseException,
			JsonMappingException {
		Object[] parameters = new Object[method.getParameterTypes().length];
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Type[] parameterTypes = method.getGenericParameterTypes();
		JsonNode arrayNode = objectMapper.readTree(parametersStringValue);
		Iterator<JsonNode> arguments = arrayNode.getElements();
		int i = 0;
		for(JsonNode arg = null; arguments.hasNext(); i++){
			arg = arguments.next();
			Object argumentValue = objectMapper.readValue(arg.traverse(), TypeFactory.type(parameterTypes[i]));
			parameters[i] = argumentValue;
		}
		return parameters;
	}

	public Map<String,Object> serializeForParametersMap(Object[] arguments)  {
		String sb = serializeParametersForString(arguments);
		Map<String,Object> map = new HashMap<String,Object>();
		map.put(KEY_PARAMETERS, sb);
		return map;
	}

	public String serializeParametersForString(Object[] arguments) {
		return convertUtil.serializeParametersForString(arguments);
	}
	
	// TODO 某些参数类型不支持序列化，应该抛错误
	private String serializeParameterValue(Object arg) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if(arg == null) {
			return NULL_VALUE;
		}
		Class parameterType = arg.getClass();
		
		// support for array
		if(parameterType.isArray()) {
			return convertUtil.array2String(arg);
		}
		
		// support for collection
		if(arg instanceof Collection) {
			return convertUtil.list2String((Collection)arg); //TODO 需要支持复杂对象
		}
		
		// support for Date,timestamp,sql.Date,sql.Time
		if(arg instanceof Date) {
			Date date = (Date)arg;
			return DateFormatUtils.format(date, "yyyyMMddHHmmssSSS"); //TODO 需要支持时间戳的传输格式以便提升效率
//			return String.valueOf(date.getTime());
		}
		
		
		// support for Enum
		if(parameterType.isEnum()) {
			return ((Enum)arg).name();
		}
		
		// support for Map
		if(arg instanceof Map) {
			return convertUtil.map2String((Map)arg);
		}
		
		// support for DTO object
		if(!parameterType.getName().startsWith("java")) {
			Map map = PropertyUtils.describe(arg);
			return convertUtil.map2String((Map)map);
		}
		
		// support for int,long,Long
		return arg.toString();
	}

	private Object lookupService(String serviceId) {
		Object service = serviceMapping.get(serviceId);
		if(service == null) {
			throw new IllegalArgumentException("not found service object by id:"+serviceId);
		}
		return service;
	}

	//1.需要处理集合类:List,Map,Set,Collection,Array
	//2.需要处理简单类:long,Integer,String,Date,Timestamp,Enum
	//3.需要处理对象类:UserInfo,BlogInfo
	//4.需要处理对象集合类: List<UserInfo>,UserInfo[]
	static BeanUtilsBean beanUtils = new BeanUtilsBean();
	static String[] DATE_PATTERNS = new String[]{"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd","yyyyMMddHHmmssSSS","yyyyMMddHHmmss","yyyyMMdd","yyyy-MM-dd HH:mm:ss.SSS"};
	static {
		BeanUtilsConvertRegisterHelper.registerConverters(beanUtils.getConvertUtils(), DATE_PATTERNS);
	}
	private Object deserializeParameterValue(Class<?> parameterType,
			Object value, Map<String, Object> params) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		if(value == null && parameterType.isPrimitive()) {
			return PrimitiveTypeUtil.getPrimitiveDefaultValue(parameterType);
		}
		if(value == null && (parameterType.isEnum())) {
			return null;
		}
		
		if(NULL_VALUE.equals(value)) {
			return null;
		}
		
		// support for map
		if(Map.class.isAssignableFrom(parameterType)) {
			Map result = null;
			if(parameterType.isInterface()) {
				result = new HashMap();
			}else {
				result = (Map)parameterType.newInstance();
			}
			Map map = convertUtil.string2Map((String)value);
			if(map.isEmpty()) {
				map = params;
			}
			result.putAll(map);
			return result;
		}
		
		// support for list
		if(Collection.class.isAssignableFrom(parameterType)) {
			if(value == null) {
				return null;
			}
			Collection result = null;
			if(parameterType.isInterface()) {
				if(parameterType.isAssignableFrom(List.class)) {
					result = new ArrayList();
				}else if(parameterType.isAssignableFrom(Set.class)) {
					result = new HashSet();
				}
			}else {
				result = (Collection)parameterType.newInstance();
			}
			if(value instanceof Object[]) {
				return conversionService.convert((Object[])value, parameterType); //TODO 增加这个分支的单元测试
			}else {
				Collection list = convertUtil.string2List((String)value);
				result.addAll(list);
				if(conversionService.canConvert(result.getClass(), parameterType)) {
					return conversionService.convert(result, parameterType);
				}
			}
			
			throw new IllegalArgumentException("cannot convert list value:"+result+" to targetType:"+parameterType);
		}

		// support for Array
		if(parameterType.isArray()) {
			if(value == null) {
				return null;
			}
			if(value instanceof Object[]) {
				return conversionService.convert((Object[])value, parameterType); //TODO 增加这个分支的单元测试
			}else {
				Collection<String> result = convertUtil.string2List((String)value);
				return conversionService.convert(result, parameterType);
			}
		}
		
		// support for Date,timestamp,sql.Date,sql.Time
		if(Date.class.isAssignableFrom(parameterType)) {
			if(value == null) {
				return null;
			}
			Date result = (Date)(parameterType.isAssignableFrom(Date.class) ? (Date)parameterType.newInstance() : parameterType.getConstructor(long.class).newInstance(0L));
			try {
				Date parseDate = DateUtils.parseDate((String)value,DATE_PATTERNS);
				result.setTime(parseDate.getTime());
				return result;
			} catch (ParseException e) {
				long time = Long.parseLong((String)value);
				result.setTime(time);
				return result;
			}
		}
		
		// support for int,long,Long
		if(value != null && conversionService.canConvert(value.getClass(), parameterType)) {
			return conversionService.convert(value, parameterType);
		}

		// support for DTO object
		if(!parameterType.getName().startsWith("java")) {
			Object result = parameterType.newInstance();
			Map map = convertUtil.string2Map((String)value);
			if(MapUtils.isEmpty(map)) {
				map = params;
			}
			
			FastBeanUtil.copyProperties(result,map); //TODO 性能需要提升
			return result;
		}
		
		if(value == null) {
			return null;
		}
		
		throw new IllegalArgumentException("cannot convert value:"+value+" to targetType:"+parameterType);
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
	
	//分隔符: 属性分隔符 ; 数组分隔符 , Map KV分隔符: //TODO 1.需要处理转义问题,需要处理null值转义问题，如null=\N 
	public static final String ARRAY_SEPERATOR = ",";
	public static final String MAP_SEPERATOR = ":";
	public static final String PARAMETERS_SEPERATOR = ";";
	ConvertUtil convertUtil = new ConvertUtil();
	public class ConvertUtil {

		public String map2String(Map map) {
			StringBuilder sb = new StringBuilder();
			Set<Map.Entry> entrySet = map.entrySet();
			boolean first = true;
			for(Map.Entry entry : entrySet) {
				if(first) {
					sb.append(entry.getKey()).append(':').append(entry.getValue());
					first = false;
				}else {
					sb.append(ARRAY_SEPERATOR);
					sb.append(entry.getKey()).append(':').append(entry.getValue());
				}
			}
			return sb.toString();
		}
		
		public Map string2Map(String str) {
			if(str == null) {
				return Collections.EMPTY_MAP;
			}
			Map map = new HashMap();
			String[] array = StringUtils.split(str, ARRAY_SEPERATOR); 
			for(int i = 0; i < array.length; i++) {
				String[] keyValue = StringUtils.split(array[i],MAP_SEPERATOR);
				map.put(keyValue[0], keyValue[1]);
			}
			return map;
		}
		
		public String array2String(Object array) {
			int length = Array.getLength(array); //TODO 提升性能
			ArrayList<Object> list = new ArrayList<Object>(length);
			for(int i = 0; i < length; i++) {
				list.add(Array.get(array, i));
			}
			return list2String(list);
		}
		
		public String list2String(Collection collection) {
			return StringUtils.join(collection, ARRAY_SEPERATOR);
		}
		
		public List<String> string2List(String str) {
			String[] array = StringUtils.split(str,ARRAY_SEPERATOR);
			return Arrays.asList(array);
		}
		
		public String serializeParametersForString(Object[] arguments) {
			StringBuilder sb = new StringBuilder(1024);
			for(int i = 0; i < arguments.length; i++) {
				Object arg = arguments[i];
				try {
					if(i == 0) {
						sb.append(ParameterEscapeUtil.escapeParameter(serializeParameterValue(arg)));
					}else {
						sb.append(PARAMETERS_SEPERATOR);
						sb.append(ParameterEscapeUtil.escapeParameter(serializeParameterValue(arg)));
					}
				}catch(Exception e) {
					throw new IllegalArgumentException("cannot serialize argument["+i+"]="+arg,e);
				}
			}
			return sb.toString();
		}
	}
}
