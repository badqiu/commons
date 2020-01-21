package com.github.rapid.common.log.logger;


public class LoggerImpl implements Logger {
	private org.slf4j.Logger proxy;
	
	public LoggerImpl(org.slf4j.Logger logger) {
		this.proxy = logger;
	}

	@Override
	public void trace(String msgType, String msg, Exception exception) {
		
	}

	@Override
	public void debug(String msgType, String msg, Exception exception) {
		
	}

	@Override
	public void info(String msgType, String msg, Exception exception) {
		
	}

	@Override
	public void warn(String msgType, String msg, Exception exception) {
		
	}

	@Override
	public void error(String msgType, String msg, Exception exception) {
		
	}

	@Override
	public void fatal(String msgType, String msg, Exception exception) {
		
	}

}
