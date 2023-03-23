package com.github.rapid.common.spring.factory.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class StringPropertiesFactoryBean extends org.springframework.beans.factory.config.PropertiesFactoryBean{

	private String content;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	protected Properties createProperties() throws IOException {
		Properties p = super.createProperties();
		
		if(StringUtils.isNotBlank(content)) {
			Properties custom = loadFromContentString();
			p.putAll(trim(custom));
		}
		
		return p;
	}

	private Properties loadFromContentString() throws IOException, InvalidPropertiesFormatException {
		Properties custom = new Properties();
		if(content.contains("<?xml")) {
			custom.loadFromXML(new ByteArrayInputStream(content.getBytes()));
		}else {
			StringReader reader = new StringReader(content);
			custom.load(reader);
		}
		return custom;
	}

	private static Properties trim(Properties custom) {
		Properties r = new Properties();
		custom.forEach((k,v) -> {
			r.put(trim(k), trim(v));
		});
		return r;
	}

	private static String trim(Object k) {
		if(k == null) return null;
		
		return StringUtils.trim(k.toString());
	}
	
}
