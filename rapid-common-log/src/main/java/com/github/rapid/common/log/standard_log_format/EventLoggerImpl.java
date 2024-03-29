package com.github.rapid.common.log.standard_log_format;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLoggerImpl implements EventLogger {
	public static String defaultSystem = "default_system";
	
	private Logger logger;
	private String system = defaultSystem;
//	public EventLoggerImpl() {
//	}
	
	public EventLoggerImpl(String loggerName) {
		logger = LoggerFactory.getLogger(loggerName);
	}
	
	public EventLoggerImpl(Class<?> loggerName) {
		logger = LoggerFactory.getLogger(loggerName);
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}

	public void setSystem(String system) {
		this.system = system;
	}
	
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public void trace(LoggerMsg msg) {
		logger.trace(initAndToString(msg,"trace"));
	}
	
	@Override
	public void debug(LoggerMsg msg) {
		logger.debug(initAndToString(msg,"debug"));
	}

	@Override
	public void info(LoggerMsg msg) {
		logger.info(initAndToString(msg,"info"));
	}

	@Override
	public void warn(LoggerMsg msg) {
		logger.warn(initAndToString(msg,"warn"));
	}

	@Override
	public void error(LoggerMsg msg) {
		logger.error(initAndToString(msg,"error"));
	}
	
	private String initAndToString(LoggerMsg msg,String level) {
		msg.setLevel(level);
		if(msg.getEventType() == null) {
			msg.setEventType(logger.getName());
		}
		if(msg.getStartTime() == null) {
			msg.setStartTime(new Date());
		}
		if(msg.getSystem() == null) {
			msg.setSystem(system);
		}
		
		return msg.toString();
	}

}
