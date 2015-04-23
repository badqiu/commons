package com.github.rapid.common.log.helper;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;

/**
 * LogbackTriggerUtil.java
 * 2013-1-28 下午03:02:53
 * 
 * @author chenming@yy.com
 * logback ILoginEvent方法共用方法
 * 
 * 1.当用log.info("log content {}", exception)方式输出时
 * 	IThrowableProxy throwable = logEvent.getThrowableProxy(); exception 放到ThrowableProxy中
 *  跟是否用error级别打印日志,没有关系
 *  Object[] argums = logEvent.getArgumentArray();
 *  argums == null
 *  
 * 2.当用log.info("log content {}", new Object[]{exception})方式输出日志时
 * 	IThrowableProxy throwable = logEvent.getThrowableProxy();
 * 	throwable == null
 * 	Object[] argums = logEvent.getArgumentArray(); exception 放到ArgumentArray中
 * 
 * 
 */
public class LogbackTriggerUtil {
	public enum OUTPUT_FORMAT {
		TEXT("\n", "\t"),
		HTML("<br/>", "&nbsp;&nbsp;&nbsp;");
		
		private String splitChar;
		private String spaceChar;
		private OUTPUT_FORMAT(String split, String space) {
			splitChar = split;
			spaceChar = space;
		}
		
		public String getSplitChar() {
			return splitChar;
		}
		
		public String getSpaceChar() {
			return spaceChar;
		}
	}
	
	/**
	 * 把错误对象转换成一个字符串,用于标识同一错误
	 * 生成规则
	 * 错误发生类名.方法名(行号)
	 * @param logEvent
	 * @return
	 */
	public static String event2Key(ILoggingEvent logEvent) {
		IThrowableProxy throwable = logEvent.getThrowableProxy();
		if(null != throwable) {
			StackTraceElementProxy proxy = throwable.getStackTraceElementProxyArray()[0];
			return proxy.toString();
		} else {
			Object[] args = logEvent.getArgumentArray();
			if(args != null) {
				for(Object arg : args) {
					if(arg instanceof Throwable) {
						return arg.getClass().getName();
					} 
				}
			}
			// 日志输出中没有异常,错误对象
			return null;
		}
	}
	
	/**
	 * 打印出错误的堆栈信息
	 * 用于邮件输出,看到错误详情
	 * @param logEvent
	 * @param format
	 * @return
	 */
	public static String event2String(ILoggingEvent logEvent, OUTPUT_FORMAT format) {
		StringBuilder sb = new StringBuilder();
		String SPACE_CHAR = format.getSpaceChar();
		String SPLIT_CHAR = format.getSplitChar();
		
		IThrowableProxy throwable = logEvent.getThrowableProxy();
		if(throwable != null) {
			sb.append(throwable.getMessage()).append(SPLIT_CHAR);
			StackTraceElementProxy[] proxys = throwable.getStackTraceElementProxyArray();
			for(StackTraceElementProxy proxy : proxys) {
				sb.append(format.getSpaceChar()).append("at ").append(proxy.toString()).append(SPLIT_CHAR);
			}
		} else {
			Object[] args = logEvent.getArgumentArray();
			if(args != null) {
				for(Object obj : args) {
					if(obj instanceof Throwable) {
						Throwable th = (Throwable) obj;
						sb.append(th.getLocalizedMessage()).append(SPLIT_CHAR);
						StackTraceElement[] elements = th.getStackTrace();
						for(StackTraceElement element : elements) {
							sb.append(SPACE_CHAR).append("at ").append(element.toString()).append(SPLIT_CHAR);
						}
						sb.append(SPLIT_CHAR);
					}
				}
			}
		}
		
		return sb.toString();
	}
	
	/**
	 * 打印错误的简单信息
	 * 用于手机信息和YY群信息发送
	 * @param logEvent
	 * @param errorTimes
	 * @return
	 */
	public static String simpleEvent2String(ILoggingEvent logEvent) {
		StringBuilder content = new StringBuilder();
		/*
		 * 服务器:ip
		 * 错误信息,发生了N次
		 * at xxxx.xxx.className.methodName():line 18
		 */
		String message = logEvent.getFormattedMessage();
		StackTraceElement trace = logEvent.getCallerData()[0];
		content.append("服务器:").append(IPUtil.getIp()).append("\n发生异常[").append(message)
				.append("]\n").append("at ").append(trace).append("\n");

		return content.toString();
	}
}
