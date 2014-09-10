package com.duowan.common.log.logback;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.core.OutputStreamAppender;

import com.duowan.common.log.helper.EmailUtil;
import com.duowan.common.log.helper.LogbackTriggerUtil;
import com.duowan.common.log.helper.LogbackTriggerUtil.OUTPUT_FORMAT;
import com.duowan.common.log.log4j.LogEventIntervalTrigger;

/**
 * SMTPAppender.java
 * 2013-1-18 下午05:42:24
 * 
 * @author chenming@yy.com
 *         邮件appender
 * 
 *         采用TimeBucketTrigger,一段时间收集一次
 *         然后用异步的方式把错误用邮件发送出去
 * 
 */
public class SMTPAppender<E> extends OutputStreamAppender<E> {

	public SMTPAppender() {
//		System.out.println("set output stream...");
		setOutputStream(System.out);
	}

	@Override
	protected void append(E eventObject) {
		if (eventObject instanceof ILoggingEvent) {
			try {
				ILoggingEvent logEvent = (ILoggingEvent) eventObject;
				if(logEvent.getLevel().levelInt < _threshold.levelInt) {
					return;
				}
				
				String errorClassName = LogbackTriggerUtil.event2Key(logEvent);
				if (trigger.isSendEvent(errorClassName)) {
					String content = LogbackTriggerUtil.event2String(logEvent, OUTPUT_FORMAT.HTML);
					if (StringUtils.isNotBlank(content)) {
						EmailUtil.asynSend(subject, content, emails);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 测试ILogginEvent数据结构用的
	 * TODO:如此传神的名字,还用加注释吗?
	 * @param logEvent
	 */
//	void print(ILoggingEvent logEvent) {
//		System.out.println("****************************************************");
//		IThrowableProxy proxy = logEvent.getThrowableProxy();
//		if(proxy != null) {
//			StackTraceElementProxy[] proxyArrays = proxy.getStackTraceElementProxyArray();
//			for(StackTraceElementProxy proxyE : proxyArrays) {
//				System.out.println("*** " + proxyE);
//			}
//		} else {
//			System.out.println("=== proxy null");
//		}
//		
//		String message = logEvent.getMessage();
//		System.out.println("**Message: " + message);
//		
//		Object[] arguments = logEvent.getArgumentArray();
//		if(arguments != null) {
//			for(Object argm : arguments) {
//				System.out.println("**args: " + argm);
//			}
//		} else {
//			System.out.println("=== arguments null");
//		}
//		
//		StackTraceElement[] traces = logEvent.getCallerData();
//		for(StackTraceElement trace : traces) {
//			System.out.println("**&&: " + trace);
//		}
//		System.out.println("****************************************************");
//	}

	/**
	 * 邮件列表
	 * 多个邮件地址用","隔开
	 */
	private List<String> emails;
	/**
	 * 邮件触发器
	 * 一般用TimeBucketTrigger,一段时间收集一次,然后用异步的方式把错误用邮件发送出去
	 */
	private LogEventIntervalTrigger trigger = new LogEventIntervalTrigger();
	/**
	 * 邮件主题,如:XX系统错误告警
	 */
	private String subject;
	/**
	 * 日志输出最低级别,如果不配置或配置错误,则为DEBUG
	 */
	private Level _threshold = Level.DEBUG;
	
	public void setEmails(String emailss) {
		// 删除结尾的","
		emailss = emailss.replaceAll(",\\s*$", "");
		String emailRgx = "\\w+@\\w+\\.\\w+(\\s*,\\s*\\w+@\\w+\\.\\w+)*";
		if (emailss.matches(emailRgx)) {
			emails = Arrays.asList(emailss.split(",\\s*"));
		} else {
			throw new RuntimeException("email地址配置错误:[" + emailss + "]");
		}
	}

	public void setSubject(String subject) {
		this.subject = subject;
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
