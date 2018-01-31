package com.github.rapid.common.rpc.serde;

import java.beans.PropertyDescriptor;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;

import com.github.rapid.common.rpc.RPCConstants;
import com.github.rapid.common.rpc.SerDe;
import com.github.rapid.common.rpc.SerializeException;
import com.github.rapid.common.rpc.server.MethodInvoker;
import com.github.rapid.common.rpc.util.BeanUtilsConvertRegisterHelper;
import com.github.rapid.common.rpc.util.FastBeanUtil;
import com.github.rapid.common.rpc.util.ParameterEscapeUtil;
import com.github.rapid.common.rpc.util.PrimitiveTypeUtil;

public class SimpleSerDeImpl implements SerDe{

	private ConversionService conversionService = null;
	{
		ConversionServiceFactoryBean factory = new ConversionServiceFactoryBean();
		factory.afterPropertiesSet();
		conversionService = factory.getObject();
	}
	
	public void serialize(Object object, OutputStream output,
			Map<String, Object> serializeParams) throws SerializeException {
		throw new UnsupportedOperationException("serialize Unsupported");
	}

	public Object deserialize(InputStream input, Type returnType,
			Map<String, Object> serializeParams) throws SerializeException {
		throw new UnsupportedOperationException("serialize Unsupported");
	}
	
	public String getContentType() {
		throw new UnsupportedOperationException();
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
		
		public Object deserializeParameterValue(Class<?> parameterType,
				Object value, Map<String, Object> params) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
			if(value == null && parameterType.isPrimitive()) {
				return PrimitiveTypeUtil.getPrimitiveDefaultValue(parameterType);
			}
			if(value == null && (parameterType.isEnum())) {
				return null;
			}
			
			if(RPCConstants.NULL_VALUE.equals(value)) {
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
				if(value instanceof String && StringUtils.isBlank((String)value)) {
					return null;
				}
				Date result = (Date)(parameterType.isAssignableFrom(Date.class) ? (Date)parameterType.newInstance() : parameterType.getConstructor(long.class).newInstance(0L));
				long time = Long.parseLong((String)value);
				result.setTime(time);
				return result;
			}
			
			// support for int,long,Long
			if(value != null && conversionService.canConvert(value.getClass(), parameterType)) {
				return conversionService.convert(value, parameterType);
			}

			
			if(parameterType.isInterface()) {
				return null;
			}
			
			// support for DTO object
			if(!parameterType.getName().startsWith("java")) {
				Object result = parameterType.newInstance();
				Map map = convertUtil.string2Map((String)value);
				if(MapUtils.isEmpty(map)) {
					map = params;
				}
				
				FastBeanUtil.copyProperties(deserializationMap(map,parameterType),result); //TODO 性能需要提升
				return result;
			}
			
			if(value == null) {
				return null;
			}
			
			
			
			throw new IllegalArgumentException("cannot convert value:"+value+" to targetType:"+parameterType);
		}

		private Map deserializationMap(Map map, Class<?> targetType) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
			Map result = new HashMap();
			PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(targetType);
			for(PropertyDescriptor pd : targetPds) {
				if (pd.getWriteMethod() != null) {
					String stringValue = (String)map.get(pd.getName());
					try {
						Object value = deserializeParameterValue(pd.getPropertyType(), stringValue, map);
						result.put(pd.getName(), value);
					}catch(Exception e){
						throw new RuntimeException("cannot write property:"+pd.getName()+" by value:"+stringValue+" for targetType:"+pd.getPropertyType(),e);
					}
				}
			}
			return result;
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
		
		public String serializeParametersForString(Object[] arguments) {
			return convertUtil.serializeParametersForString(arguments);
		}
		
		// TODO 某些参数类型不支持序列化，应该抛错误
		private String serializeParameterValue(Object arg) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
			if(arg == null) {
				return RPCConstants.NULL_VALUE;
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
//				return String.valueOf(date.getTime());
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
		
		public Map<String,Object> serializeForParametersMap(Object[] arguments)  {
			String sb = serializeParametersForString(arguments);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put(MethodInvoker.KEY_PARAMETERS, sb);
			return map;
		}
}
