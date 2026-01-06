package com.github.rapid.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;


public class ArrayUtil {
	
	/**
	 * 将一个array转为根据keys转为map
	 * @param array
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("all")
	public static <V> Map<String,V> toMap(V[] array,String...keys) {
		if(array == null) return new HashMap();
		
		Map m = new LinkedHashMap();
		for(int i = 0; i < keys.length; i++) {
			if(i >= array.length) {
				break;
			}
			String trimKey = StringUtils.trim(keys[i]);
			V value = array[i];
			m.put(trimKey, value);
		}
		return m;
	}
	
	public static <T> List<T> fromMap(Map<String,T> map,String...keys) {
	    if (map == null || keys == null || keys.length == 0) {
	        return new ArrayList<>();
	    }
	    
	    List<T> result = new ArrayList<>(keys.length);
	    for (String key : keys) {
	        String trimmedKey = StringUtils.trim(key);
	        T value = map.get(trimmedKey);
			result.add(value);
	    }
	    return result;		
	}
	
	/**
	 * 根据传递的clazz的fields顺序,自动填充array数据进Bean
	 * 
	 * 注意: 不使用PropertyDescriptor,因为这个无序.
	 */
	public static <T> T toBean(Object[] array,Class<T> clazz) {
		if(array == null) return null;
		
		T object = BeanUtils.instantiate(clazz);
		return toBean(array,object);
	}

	/**
	 * 根据传递的clazz的fields顺序,自动填充array数据进Bean
	 * 
	 * 注意: 不使用PropertyDescriptor,因为这个无序.
	 */
	public static <T> T toBean(Object[] array, T result)  {
		if(array == null || result == null) {
			return result;
		}
		
		Class clazz = result.getClass();
		
//		Field[] fields = clazz.getDeclaredFields();
//		Field[] fields = FieldUtils.getAllFields(clazz);
		Field[] fields = ReflectUtil.getAllFieldsFromSuperToChildOrder(clazz);
		
		for(int i = 0; i < fields.length; i++) {
			if(i >= array.length) {
				break;
			}
			Field field = fields[i];
			String name = field.getName();
			Object value = array[i];
			
			PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, name);
			try {
//				setFieldValue(result, field, value);
				FastBeanUtil.setProperty(result, pd, value);
			}catch(Exception e) {
				throw new RuntimeException("setValue error on field:"+name+" clazz:"+clazz+" value:"+value,e);
			}
		}
		
		return result;
	}

	private static <T> void setFieldValue(T result, Field field, Object value) throws IllegalAccessException {
		Class type = field.getType();
		Object targetValue = FastConvertUtil.convert(type,value);
		field.setAccessible(true);
		field.set(result, targetValue);
	}
}
