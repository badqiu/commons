package com.github.rapid.common.log.standard_log_format;

public interface EventLogger {
	
	public boolean isTraceEnabled();
	public boolean isDebugEnabled();
	public boolean isInfoEnabled();
	public boolean isWarnEnabled();
	public boolean isErrorEnabled();
	
	public void trace(LoggerMsg msg);
	public void debug(LoggerMsg msg);
	public void info(LoggerMsg msg);
	public void warn(LoggerMsg msg);
	public void error(LoggerMsg msg);
	
}
