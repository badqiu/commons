package com.github.rapid.common.log.logger;

public interface Logger {
	
	public void trace(String msgType,String msg,Exception exception);
	
	public void debug(String msgType,String msg,Exception exception);
	
	public void info(String msgType,String msg,Exception exception);
	
	public void warn(String msgType,String msg,Exception exception);
	
	public void error(String msgType,String msg,Exception exception);
	
	public void fatal(String msgType,String msg,Exception exception);
}
