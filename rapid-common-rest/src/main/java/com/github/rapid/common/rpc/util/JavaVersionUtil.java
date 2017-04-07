package com.github.rapid.common.rpc.util;

import java.lang.reflect.Parameter;

public class JavaVersionUtil {

	static boolean java8 = false;
	
	static {
		try {
			Parameter.class.getName();
			java8 = true;
		}catch(Throwable e) {
		}
	}
	
	public static boolean isGreatthanJava8() {
		return java8;
		
	}
	
	
}
