package com.github.rapid.common.log.log4j;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.github.rapid.common.log.helper.IPUtil;


/**
 * 自定义log4j发邮件appender
 * 在subject加上系统IP
 * @author hemeng
 *
 * 2012-7-25
 */
public class SMTPAppender extends org.apache.log4j.net.SMTPAppender{
	LogEventIntervalTrigger eventTrigger = new LogEventIntervalTrigger();
	
	public void setIntervalSeconds(long intervalSeconds) {
		eventTrigger.setIntervalSeconds(intervalSeconds);
	}	
	
	public void setCrons(String crons) {
		eventTrigger.setCrons(crons);
	}

	public void setActiveByEnvExpr(String envExpr) {
		eventTrigger.setActiveByEnvExpr(envExpr);
	}

	@Override
	public void setSubject(String subject) {
		super.setSubject(IPUtil.getIp() + " " + subject);
	}
	
	@Override
	public void append(LoggingEvent event) {
		ThrowableInformation throwable = event.getThrowableInformation();
		String errorClassName = null;
		if(throwable!=null) {
			errorClassName = throwable.getThrowable().getClass().getName();
		}
		
		if(eventTrigger.isSendEvent(errorClassName)) {
			super.append(event);
		}
	}
}
