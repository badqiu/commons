package com.github.rapid.common.beanutils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtilsBean;

/**
 * apache PropertyUtils的等价类，修改如下:
 * 1. 将check exception改为uncheck exception
 * 2. getSimpleProperty(bean,name) getProperty(bean,name) 如果发现bean是Map则使用 Map.get(name)返回值
 * @author badqiu
 *
 */
public class PropertyUtils {

	public static PropertyUtilsBean propertyUtilsBean = BeanUtils.getPropertyUtils();
	
	private static void handleException(Exception e) {
		BeanUtils.handleReflectionException(e);
	}
	
	public static void clearDescriptors() {
		propertyUtilsBean.clearDescriptors();
	}

	public static void copyProperties(Object dest, Object orig){
		try {
			propertyUtilsBean.copyProperties(dest, orig);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public static Map describe(Object bean) {
		try {
			return propertyUtilsBean.describe(bean);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public static Object getIndexedProperty(Object bean, String name, int index){
		try {
			return propertyUtilsBean.getIndexedProperty(bean, name, index);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public static Object getIndexedProperty(Object bean, String name) {
		try {
			return propertyUtilsBean.getIndexedProperty(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public static Object getMappedProperty(Object bean, String name, String key) {
		try {
			return propertyUtilsBean.getMappedProperty(bean, name, key);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public static Object getMappedProperty(Object bean, String name) {
		try {
			return propertyUtilsBean.getMappedProperty(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public static Object getNestedProperty(Object bean, String name) {
		try {
			return propertyUtilsBean.getNestedProperty(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public static Object getProperty(Object bean, String name) {
		if(bean == null) return null;
		try {
			if(bean instanceof Map) {
				return ((Map)bean).get(name);
			} else {
				return propertyUtilsBean.getProperty(bean, name);
			}
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public static PropertyDescriptor getPropertyDescriptor(Object bean, String name) {
		try {
			return propertyUtilsBean.getPropertyDescriptor(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Class beanClass) {
		return propertyUtilsBean.getPropertyDescriptors(beanClass);
	}

	public static PropertyDescriptor[] getPropertyDescriptors(Object bean) {
		return propertyUtilsBean.getPropertyDescriptors(bean);
	}

	public static Class getPropertyEditorClass(Object bean, String name) {
		try {
			return propertyUtilsBean.getPropertyEditorClass(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public static Class getPropertyType(Object bean, String name) {
		try {
			return propertyUtilsBean.getPropertyType(bean, name);
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public static Method getReadMethod(PropertyDescriptor descriptor) {
		return propertyUtilsBean.getReadMethod(descriptor);
	}

	public static Object getSimpleProperty(Object bean, String name){
		if(bean == null) return null;
		try {
			if(bean instanceof Map) {
				return ((Map)bean).get(name);
			} else {
				return propertyUtilsBean.getSimpleProperty(bean, name);
			}
		}catch(Exception e) {
			handleException(e);
			return null;
		}
	}

	public static Method getWriteMethod(PropertyDescriptor descriptor) {
		return propertyUtilsBean.getWriteMethod(descriptor);
	}


	public static boolean isReadable(Object bean, String name) {
		return propertyUtilsBean.isReadable(bean, name);
	}

	public static boolean isWriteable(Object bean, String name) {
		return propertyUtilsBean.isWriteable(bean, name);
	}

	public static void setIndexedProperty(Object bean, String name, int index,Object value) {
		try {
			propertyUtilsBean.setIndexedProperty(bean, name, index, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public static void setIndexedProperty(Object bean, String name, Object value){
		try {
			propertyUtilsBean.setIndexedProperty(bean, name, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public static void setMappedProperty(Object bean, String name, Object value){
		try {
			propertyUtilsBean.setMappedProperty(bean, name, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public static void setMappedProperty(Object bean, String name, String key,Object value) {
		try{
			propertyUtilsBean.setMappedProperty(bean, name, key, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public static void setNestedProperty(Object bean, String name, Object value){
		try {
			propertyUtilsBean.setNestedProperty(bean, name, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public static void setProperty(Object bean, String name, Object value){
		try {
			propertyUtilsBean.setProperty(bean, name, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	public static void setSimpleProperty(Object bean, String name, Object value){
		try {
			propertyUtilsBean.setSimpleProperty(bean, name, value);
		}catch(Exception e) {
			handleException(e);
		}
	}

	
}
