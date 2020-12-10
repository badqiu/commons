package com.github.rapid.common.log;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventLoggerImpl implements EventLogger {
	private Logger logger;
	
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
		logger.trace(initAndToString(msg));
	}
	
	@Override
	public void debug(LoggerMsg msg) {
		logger.debug(initAndToString(msg));
	}

	@Override
	public void info(LoggerMsg msg) {
		logger.info(initAndToString(msg));
	}

	@Override
	public void warn(LoggerMsg msg) {
		logger.warn(initAndToString(msg));
	}

	@Override
	public void error(LoggerMsg msg) {
		logger.error(initAndToString(msg));
	}
	
	private String initAndToString(LoggerMsg msg) {
		if(msg.getEventType() == null) {
			msg.setEventType(logger.getName());
		}
		if(msg.getStartTime() == null) {
			msg.setStartTime(new Date());
		}
		
		return msg.toString();
	}

}
