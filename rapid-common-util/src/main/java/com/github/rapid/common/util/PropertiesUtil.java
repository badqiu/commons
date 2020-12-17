package com.github.rapid.common.util;

import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

public class PropertiesUtil {

	public static Properties loadProperties(String props) {
		return loadProperties(props,"load properties error from string");
	}
	
	public static Properties loadProperties(String props,String errorInfo) {
		if(StringUtils.isBlank(props)) return new Properties();
		
		try {
			Properties p = new Properties();
			if(props.contains("<properties>")) {
				p.loadFromXML(new ByteArrayInputStream(props.getBytes()));
			}else {
				p.load(new StringReader(props));
			}
			
			return trimKeyValue(p);
		}catch(Exception e) {
			throw new RuntimeException(errorInfo,e);
		}
	}
	
	public static Properties trimKeyValue(Properties p) {
		if(p == null) return null;
		
		Properties result = new Properties();
		p.forEach((k,v) -> {
			String key = (String)k;
			String value = (String)v;
			result.put(StringUtils.trim(key), StringUtils.trim(value));
		});
		return result;
	}
	
}
