package com.github.rapid.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;


public class ArrayUtil {
	
	/**
	 * 将一个array转为根据keys转为map
	 * @param array
	 * @param keys
	 * @return
	 */
	@SuppressWarnings("all")
	public static Map toMap(Object[] array,String...keys) {
		if(array == null) return new HashMap();
		
		Map m = new LinkedHashMap();
		for(int i = 0; i < keys.length; i++) {
			if(array.length == i ) {
				break;
			}
			m.put(StringUtils.trim(keys[i]), array[i]);
		}
		return m;
	}
	
	
	public static <T> T toBean(Object[] array,Class<T> clazz) {
		if(array == null) return null;
		
		try {
			T result = clazz.newInstance();
			return toBean(array,result);
		}catch(Exception e) {
			throw new RuntimeException("toBean error,class:"+clazz+" array:"+ArrayUtils.toString(array),e);
		}
	}


	public static <T> T toBean(Object[] array, T result) throws IllegalAccessException, InvocationTargetException {
		if(array == null || result == null) {
			return result;
		}
		
		Class clazz = result.getClass();
		
		Field[] fields = clazz.getDeclaredFields();
		
		for(int i = 0; i < fields.length; i++) {
			if(array.length == i ) {
				break;
			}
			Field field = fields[i];
			String name = field.getName();
			Object value = array[i];
			
			PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(clazz, name);
			try {
//				Class type = field.getType();
//				Object targetValue = FastConvertUtil.convert(type,value);
//				field.setAccessible(true);
//				field.set(result, targetValue);
				FastBeanUtil.setProperty(result, pd, value);
			}catch(Exception e) {
				throw new RuntimeException("setValue error on field:"+name+" clazz:"+clazz+" value:"+value,e);
			}
		}
		
		return result;
	}
}
