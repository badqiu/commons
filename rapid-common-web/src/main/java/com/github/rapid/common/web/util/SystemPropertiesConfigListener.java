package com.github.rapid.common.web.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.PropertyPlaceholderHelper.PlaceholderResolver;

import edu.emory.mathcs.backport.java.util.Arrays;

/**
 * 用于实现将配置文件加载进 System.properties
 * <br/>
 * <pre>
 * 	&lt;context-param>
 *		&lt;param-name>configSystemPropertiesLocations&lt;/param-name>
 *		&lt;param-value>
 *		classpath*:application.properties,
 *		classpath*:application-local.properties,
 *		file:/data/apps/demoproject/application.properties
 *		&lt;/param-value>
 *	&lt;/context-param>
 * </pre>
 * @author badqiu
 * 
 */
public class SystemPropertiesConfigListener implements ServletContextListener {
	private static Logger logger = LoggerFactory.getLogger(SystemPropertiesConfigListener.class);
	
	static String CONFIG_PARAM_NAME = "configSystemPropertiesLocations";
	public void contextInitialized(ServletContextEvent sce) {
		loadPropertiesIntoSystemProperties(sce);
	}

	public void contextDestroyed(ServletContextEvent sce) {
	}

	private void loadPropertiesIntoSystemProperties(ServletContextEvent sce) {
		String configSystemPropertiesLocations = sce.getServletContext()
				.getInitParameter(CONFIG_PARAM_NAME);
		if(StringUtils.isBlank(configSystemPropertiesLocations)) {
			throw new IllegalArgumentException("not found required context InitParameter:'configSystemPropertiesLocations'");
		}
		
		try {
			logger.info("Start load properties from configSystemPropertiesLocations:"+configSystemPropertiesLocations);
			final Properties properties = loadProperties(configSystemPropertiesLocations);
			logger.info("Load properties into System.properties:"+properties);
			
			resolvePropertiesPlaceholder(properties);
			
			properties.putAll(System.getProperties());
			System.setProperties(properties);
		}catch(Exception e) {
			throw new RuntimeException("load loadPropertiesIntoSystemProperties occer error,configSystemPropertiesLocations:"+configSystemPropertiesLocations,e);
		}
	}

	private void resolvePropertiesPlaceholder(final Properties properties) {
		PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${","}");
		for(Object key : properties.keySet()) {
			String value = properties.getProperty((String)key);
			String resolvedValue = helper.replacePlaceholders(value, new PlaceholderResolver() {
				public String resolvePlaceholder(String placeholderName) {
					String val = properties.getProperty(placeholderName);
					if(StringUtils.isBlank(val)) {
						val = System.getProperty(placeholderName);
						if(StringUtils.isBlank(val)) {
							val = System.getenv(placeholderName);
						}
					}
					return val;
				}
			});
			properties.put(key, resolvedValue);
		}
	}

	private Properties loadProperties(String configSystemPropertiesLocations)
			throws IOException {
		List<Resource> resources = getResources(configSystemPropertiesLocations);
		
		PropertiesFactoryBean factoryBean = new PropertiesFactoryBean();
		factoryBean.setIgnoreResourceNotFound(true);
		factoryBean.setLocations(resources.toArray(new Resource[0]));
		factoryBean.afterPropertiesSet();
		Properties properties = factoryBean.getObject();
		return properties;
	}

	private List<Resource> getResources(String configSystemPropertiesLocations)
			throws IOException {
		String[] locations = org.springframework.util.StringUtils.tokenizeToStringArray(configSystemPropertiesLocations," \n\t\r\f,");
		List<Resource> resources = new ArrayList<Resource>();
		for(String location : locations) {
			Resource[] array = new PathMatchingResourcePatternResolver()
			.getResources(location);
			resources.addAll(Arrays.asList(array));
		}
		return resources;
	}

}
