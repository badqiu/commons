package com.github.rapid.common.rpc.util;

public class StringUtil {

	public static String removeExtension(String fileName) {
		if(fileName == null) return null;
		
		int extentionIndex = fileName.indexOf('.');
		return extentionIndex >= 0 ? fileName.substring(0,extentionIndex) : fileName;
	}
	
}
