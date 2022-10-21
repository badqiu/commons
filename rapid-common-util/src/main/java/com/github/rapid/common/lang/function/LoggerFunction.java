package com.github.rapid.common.lang.function;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggerFunction<T,R> implements Function<T,R> {
	private String logger = LoggerFunction.class.getName();
	
	private String prefix = null;
	
	private Logger _logger = null;
	
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getLogger() {
		return logger;
	}

	public void setLogger(String logger) {
		this.logger = logger;
	}
	
	public void setLogger(Class logger) {
		this.logger = logger.getName();
	}

	@Override
	public R apply(T row) {
		if(row == null) return null;
		init();
		
		if(prefix == null) {
			_logger.info(String.valueOf(row));
		}else {
			_logger.info(prefix + row);
		}
		
		return null;
	}
	


	public void init() {
		if(_logger == null) {
			_logger = LoggerFactory.getLogger(logger);
		}
	}
}
