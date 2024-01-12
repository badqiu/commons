package com.github.rapid.common.util;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.collections.MapUtils;

import com.github.rapid.common.util.MapUtil;

public class ReflectUtil {

	/**
	 *  修改一个类的常量
	 * @param clazz
	 * @param newValues
	 * @throws IllegalAccessException
	 */
    public static void modifyAllStaticVariables(Class<?> clazz, Map<String, Object> newValues) {
        if(MapUtils.isEmpty(newValues)) return;
        
    	Field[] fields = clazz.getDeclaredFields(); // 获取所有声明的变量，包括私有变量

        for (Field field : fields) {
            if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) { // 判断是否为静态变量
                field.setAccessible(true); // 设置为可访问
                if (newValues.containsKey(field.getName())) { // 判断传入的新值Map中是否包含当前字段名
                    Object newValue = newValues.get(field.getName());
                    Object finalNewValue = ConvertUtils.convert(newValue, field.getType());
                    try {
	                    field.set(null, finalNewValue); // 根据Map中对应字段的新值修改静态变量的值
	                    
	                    System.out.println("Modified static variable " + field.getName() + " = " + field.get(null)); // 输出修改后的静态变量值
                    }catch(IllegalAccessException e) {
                    	throw new RuntimeException("modify static variable error,newValue:"+finalNewValue+" field:"+field.getName());
                    }
                }
            }
        }
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