package com.github.rapid.common.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rapid.common.xstream.XStreamUtil;

public class SmartConfigParser {

	public static Map smartParseFile(String filename,String configContent) {
		if(!StringUtils.hasText(configContent)) {
			return new HashMap();
		}
		
		try {
			return smartParseFile0(filename, configContent);
		} catch (JsonProcessingException | UnsupportedEncodingException e) {
			throw new RuntimeException("parse config error,filename:"+filename+" content:"+configContent,e);
		}
	}

	private static Map smartParseFile0(String filename, String configContent)
			throws JsonProcessingException, JsonMappingException, UnsupportedEncodingException {
		String ext = StringUtils.getFilenameExtension(filename);
		if("properties".equals(ext)) {
			return PropertiesUtil.loadProperties(configContent);
		}else if("json".equals(ext)) {
			ObjectMapper om = new ObjectMapper();
			return om.readValue(configContent, Map.class);
		}else if("yaml".equals(ext)) {
			YamlMapFactoryBean yaml = new YamlMapFactoryBean();
			yaml.setResources(new ByteArrayResource(configContent.getBytes("UTF-8")));
			yaml.afterPropertiesSet();
			return yaml.getObject();
		}else if("xml".equals(ext)) {
			return (Map)XStreamUtil.newXStream().fromXML(configContent);			
		}else {
			return PropertiesUtil.loadProperties(configContent);
		}
	}
}
