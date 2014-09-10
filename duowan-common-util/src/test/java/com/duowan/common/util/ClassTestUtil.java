package com.duowan.common.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import junit.framework.Test;

import com.duowan.common.test.util.BeanDefaultValueUtils;

public class ClassTestUtil {

	public static void invokeAllMethods(Class clazz) {
		Object obj;
		try {
			obj = clazz.newInstance();
			invokeAllMethods(obj);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static void invokeAllMethods(Object obj) {
		Class clazz = obj.getClass();
		for(Method m : clazz.getDeclaredMethods()) {
			Class[] paramType = m.getParameterTypes();
			Object[] args = newInstances(paramType);
			try {
				System.out.println("invoke method:"+m);
				m.setAccessible(true);
				m.invoke(obj, args);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Object[] newInstances(Class[] clazzes) {
		if(clazzes == null) return null;
		Object[] instances = new Object[clazzes.length];
		for(int i = 0; i < clazzes.length; i++) {
			try {
				instances[i] = BeanDefaultValueUtils.getDefaultValue(clazzes[i]);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return instances;
	}
	
	
	
}
