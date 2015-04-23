package com.github.rapid.common.log.logback;

import java.util.Arrays;
import java.util.List;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;

import com.github.rapid.common.log.helper.LogbackTriggerUtil;
import com.github.rapid.common.log.helper.SmsUtil;
import com.github.rapid.common.log.log4j.LogEventIntervalTrigger;

/**
 * SmsAppender.java
 * 2012-12-3 下午11:13:47
 * 
 * @author chenming@chinaduo.com
 * 短信appender
 * 1)在DWENV=prod才发
 * 2)如果是相同的错误,异常,在09:00~23:00是每隔10分钟,报一次;在23:00~09:00是每隔1小时发一次
 * 间隔可以调整tfExp来调整
 */
public class SmsAppender<E> extends OutputStreamAppender<E> {
	public SmsAppender() {
		setOutputStream(System.out);
	}
	
	protected final static String phoneRgx = "^1[0-9]{10}(,1[0-9]{10})*$";
	private LogEventIntervalTrigger trigger = new LogEventIntervalTrigger();

	/**
	 * 要发送的手机号码
	 * 如果有多个,用","隔开
	 */
	private List<String> phoness;
	
	/**
	 * 日志输出最低级别,如果不配置或配置错误,则为DEBUG
	 */
	private Level _threshold = Level.DEBUG;
	
	@Override
	protected void append(E eventObject) {
		if(eventObject instanceof ILoggingEvent) {
			try {
				ILoggingEvent logEvent = (ILoggingEvent)eventObject;
				if(logEvent.getLevel().levelInt < _threshold.levelInt) {
					return;
				}
				
				String errorClass = LogbackTriggerUtil.event2Key(logEvent);
				if(trigger.isSendEvent(errorClass)) {
					// 发送短信:
					String content = LogbackTriggerUtil.simpleEvent2String(logEvent);
					for(String phone : phoness) {
						SmsUtil.send(phone, content, false);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setPhones(String phones) {
		if(phones.matches(phoneRgx)) {
			phoness = Arrays.asList(phones.split(","));
		} else {
			throw new RuntimeException("手机号码配置错误:[" + phones + "]");
		}
	}
	
	public void setIntervalSeconds(long intervalSeconds) {
		trigger.setIntervalSeconds(intervalSeconds);
	}
	
	public void setCrons(String crons) {
		trigger.setCrons(crons);
	}

	public void setActiveByEnvExpr(String envExpr) {
		trigger.setActiveByEnvExpr(envExpr);
	}
	public void setThreshold(String threshold) {
		_threshold = Level.valueOf(threshold);
	}
}
