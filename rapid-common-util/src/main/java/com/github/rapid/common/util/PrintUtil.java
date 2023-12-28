package com.github.rapid.common.util;

import java.util.Map;

import edu.emory.mathcs.backport.java.util.Arrays;

public class PrintUtil {

	public static String toString(Map map) {
		if(map == null) return null;
		
		StringBuilder sb = new StringBuilder();
		map.forEach((key,value) -> {
			sb.append(key).append("=").append(value);
			sb.append("\n");
		});
		
		return sb.toString();
	}
	
	public static String toString(Object... array) {
		if(array == null) return null;
		
		return toString(Arrays.asList(array));
	}
	
	public static String toString(Iterable<Object> iter) {
		if(iter == null) return null;
		
		StringBuilder sb = new StringBuilder();
		for(Object item : iter) {
			sb.append(item);
			sb.append("\n");
		}
		
		return sb.toString();
	}
}
