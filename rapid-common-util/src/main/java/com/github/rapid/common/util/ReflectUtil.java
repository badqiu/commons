package com.github.rapid.common.util;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReflectUtil {
	private static Logger logger = LoggerFactory.getLogger(ReflectUtil.class);
	
	public static void modifyAllStaticVariables(Class<?> clazz, Map<String, Object> newValues) {
		modifyAllStaticVariables(clazz,newValues,false);
	}
	
	public static void modifyAllStaticVariables(Class<?> clazz, Class newValues) {
		Map<String,Object> fieldValues = getAllFieldsMap(newValues);
		
		modifyAllStaticVariables(clazz,fieldValues);
	}

	private static Map<String,Object> getAllFieldsMap(Class clazz)  {
		if(clazz == null) return null;
		
		List<Field> fields = FieldUtils.getAllFieldsList(clazz);
		Map<String,Object> map = new HashMap<String,Object>();
		for(Field field : fields) {
			field.setAccessible(true);
			
			try {
				map.put(field.getName(), field.get(clazz));
			} catch (Exception e) {
				throw new RuntimeException("read field error:"+field+" on class:"+clazz,e);
			} 
		}
		return map;
	}
	/**
	 *  修改一个类的常量
	 * @param clazz
	 * @param newValues
	 * @throws IllegalAccessException
	 */
    public static void modifyAllStaticVariables(Class<?> clazz, Map<String, Object> newValues,boolean ignoreError) {
        if(MapUtils.isEmpty(newValues)) return;
        
    	Field[] fields = clazz.getDeclaredFields(); // 获取所有声明的变量，包括私有变量

        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) { // 判断是否为静态变量
                if (newValues.containsKey(field.getName())) { // 判断传入的新值Map中是否包含当前字段名
                    Object newValue = newValues.get(field.getName());
                    Object finalNewValue = null;
                    try {
                    	finalNewValue = ConvertUtils.convert(newValue, field.getType());
                    	
                    	field.setAccessible(true); // 设置为可访问
	                    field.set(null, finalNewValue); // 根据Map中对应字段的新值修改静态变量的值
	                    
                    }catch(Exception e) {
                    	String message = clazz + " modify static variable error,newValue:"+finalNewValue+" field:"+field.getName();
                    	if(ignoreError) {
                    		logger.warn("ignore field set error,msg:"+message);
                    	}else {
							throw new RuntimeException(message,e);
                    	}
                    }
                }
            }
        }
    }

    
    public static Field[] getAllFieldsFromSuperToChildOrder(final Class<?> cls) {
   	 List<Field> fieldList = new ArrayList<>();
   	for(Class clazz : getAllSuperClassBy(cls)) {
   		Field[] declaredFields = clazz.getDeclaredFields();
   		if(declaredFields != null) {
   			fieldList.addAll(Arrays.asList(declaredFields));
   		}
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
   
    public static void main(String[] args) throws IllegalAccessException {
        Map<String, Object> newStaticValues = MapUtil.newMap("staticInt", "999", "staticString", "new value","staticNull",null);
        modifyAllStaticVariables(TestClass.class, newStaticValues);
    }
}

class TestClass {
    private static int staticInt = 123;
    public static String staticString = "original value";
    public static String staticNull = "original value";
}