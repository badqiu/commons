package com.duowan.common.log.log4j;

import org.apache.log4j.Layout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.duowan.common.log.helper.IPUtil;
/**
 * 只显示:  IP,exception,msg的layout
 *  
 * @author badqiu
 *
 */
public class SimpleLayout extends Layout{

	public void activateOptions() {
	}

	@Override
	public String format(LoggingEvent event) {
		String exception = getExceptionMsg(event);
		String ip = IPUtil.getIp();
		String logMsg = String.valueOf(event.getMessage());
		return buildContent(exception,ip,logMsg);
	}

	private String buildContent(String exception, String ip, String logMsg) {
		String content = ip+"\n " +logMsg + exception;
		return content;
	}
	
	@Override
	public boolean ignoresThrowable() {
		return false;
	}
	
	private String getExceptionMsg(LoggingEvent event) {
		ThrowableInformation throwableInformation = event.getThrowableInformation();
		String exception = "";
		if(throwableInformation != null) {
			Throwable throwable = throwableInformation.getThrowable();
			String errorNo = throwable.getClass().getSimpleName();
			String errorMsg = throwable.getMessage();
			exception = "\n err:"+errorNo+",errMsg:" + errorMsg;
		}
		return exception;
	}

}
