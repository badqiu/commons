package com.github.rapid.common.util;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class SpringUtil {
	
	public static void initializing(Object[] array) {
		if(array == null) return;
		
		for(Object obj : array) {
			SpringUtil.initializing(obj);
		}
	}
	
	public static void initializing(Object obj) {
		if(obj == null) return;
		
		if(obj instanceof InitializingBean) {
			try {
				((InitializingBean)obj).afterPropertiesSet();
			}catch(Exception e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
	public static void destroy(Object[] array) {
		if(array == null) return;
		
		for(Object obj : array) {
			destroy(obj);
		}
	}
	
	public static void destroy(Object obj) {
		if(obj == null) return;
		
		if(obj instanceof DisposableBean) {
			try {
				((DisposableBean)obj).destroy();
			}catch(Exception e) {
				throw new IllegalStateException(e);
			}
		}
	}
	
}
