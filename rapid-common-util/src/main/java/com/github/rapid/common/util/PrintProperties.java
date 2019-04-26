package com.github.rapid.common.util;

import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
/**
 * 将日志打印在命令行的工具类
 * 
 * @author badqiu
 *
 */
public class PrintProperties implements InitializingBean{
	private static Logger logger = LoggerFactory.getLogger(PrintProperties.class);
	
	private Properties properties;

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		printProperties(properties);
	}

	public static void printProperties(Properties props) {
		if(props == null) return;
		
		TreeMap map = new TreeMap(props);
		Set<Map.Entry> entrySet = map.entrySet();
		for(Map.Entry entry : entrySet) {
			logger.info(entry.getKey()+" = "+entry.getValue());
		}
	}
	
	
}
