package com.github.rapid.common.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class ResourceUtil {
	
	public static String getResourceAsText(String path) {
		if(StringUtils.isBlank(path)) return null;
		
		InputStream input = ResourceUtil.class.getResourceAsStream(path);
		if(input == null) return null;
		
		try {
			return IOUtils.toString(input,"UTF-8");
		}catch(IOException e) {
			throw new RuntimeException("error on read classpath path:"+path,e);
		}finally {
			IOUtils.closeQuietly(input);
		}
	}
	
	public static byte[] getResourceAsByteArray(String path) {
		if(StringUtils.isBlank(path)) return null;
		
		InputStream input = ResourceUtil.class.getResourceAsStream(path);
		if(input == null) return null;
		
		try {
			return IOUtils.toByteArray(input);
		}catch(IOException e) {
			throw new RuntimeException("error on read classpath path:"+path,e);
		}finally {
			IOUtils.closeQuietly(input);
		}
	}
	
}
