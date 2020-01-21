package com.github.rapid.common.log.logger;


public class LoggerFactory {
	
	public static Logger getLogger(String name){
		org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(name);
		return new LoggerImpl(logger);
	}
	
	public static Logger getLogger(Class clazz){
		org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(clazz);
		return new LoggerImpl(logger);
	}
	
}
