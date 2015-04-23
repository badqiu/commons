package com.github.rapid.common.log.log4j;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

import com.github.rapid.common.log.helper.SmsUtil;


/**
 * 用于发送手机短消息的Appender
 * 
 * @author badqiu
 *
 */
public class SmsAppender extends AppenderSkeleton{
	private String phone;
//	private long intervalSeconds = 10 * 60;
	private LogEventIntervalTrigger eventTrigger = new LogEventIntervalTrigger();
	
	public SmsAppender() {
		layout = new SimpleLayout();
	}
	
	public void close() {
	}

	public boolean requiresLayout() {
		return false;
	}
	
	public void setIntervalSeconds(long intervalSeconds) {
		eventTrigger.setIntervalSeconds(intervalSeconds);
	}
	
	public void setCrons(String crons) {
		eventTrigger.setCrons(crons);
	}

	public void setActiveByEnvExpr(String envExpr) {
		eventTrigger.setActiveByEnvExpr(envExpr);
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@Override
	protected void append(LoggingEvent event) {
		ThrowableInformation throwable = event.getThrowableInformation();
		String errorClassName = null;
		if(throwable!=null) {
			errorClassName = throwable.getThrowable().getClass().getName();
		}
		if(eventTrigger.isSendEvent(errorClassName)) {
			sendSms(event);
		}
	}

	private void sendSms(LoggingEvent event) {
		String content = layout.format(event);
		content = content.substring(0, Math.min(120, content.length()));
		SmsUtil.send(phone, content, false);
		System.out.println("sendSms,phone:"+phone+" content:"+content);
	}

}
