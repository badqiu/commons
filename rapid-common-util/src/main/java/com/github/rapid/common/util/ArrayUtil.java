package com.github.rapid.common.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
	public static Map toMap(Object[] array,String...keys) {
		if(array == null) return new HashMap();
		
		Map m = new LinkedHashMap();
		for(int i = 0; i < keys.length; i++) {
			if(i >= array.length) {
				break;
			}
			m.put(StringUtils.trim(keys[i]), array[i]);
		}
		return m;
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
		Field[] fields = getAllFieldsFromSuperToChildOrder(clazz);
		
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

    public static Field[] getAllFieldsFromSuperToChildOrder(final Class<?> cls) {
    	 List<Field> fieldList = new ArrayList<>();
    	for(Class clazz : getAllSuperClassBy(cls)) {
    		Field[] declaredFields = clazz.getDeclaredFields();
    		fieldList.addAll(Arrays.asList(declaredFields));
    	}
        return fieldList.toArray(new Field[fieldList.size()]);
    }
    
    public static List<Class> getAllSuperClassBy(Class<?> clazz) {  
        List<Class> result = new ArrayList<>();  
        Class<?> currentClass = clazz;  
  
        // 遍历类的继承树，从父类到子类收集字段  
        while (currentClass != null && currentClass != Object.class) { // 可以选择是否包括Object类的字段  
        	result.add(currentClass);
            currentClass = currentClass.getSuperclass();  
        }  
        Collections.reverse(result);
        return result;  
    }  
    
	private static <T> void setFieldValue(T result, Field field, Object value) throws IllegalAccessException {
		Class type = field.getType();
		Object targetValue = FastConvertUtil.convert(type,value);
		field.setAccessible(true);
		field.set(result, targetValue);
	}
}
