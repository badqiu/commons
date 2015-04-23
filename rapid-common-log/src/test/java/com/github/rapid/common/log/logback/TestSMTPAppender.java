package com.github.rapid.common.log.logback;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.MDC;
import org.slf4j.helpers.Util;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.classic.util.LogbackMDCAdapter;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * TestSMTPAppender.java
 * 2013-1-18 下午06:06:36
 * 
 * @author chenming@yy.com
 * TODO:说点什么呗
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MDC.class})
public class TestSMTPAppender {
	//private Logger iLog = LoggerFactory.getLogger(TestSMTPAppender.java);

	private SMTPAppender<TestSMTPAppender> appender = new SMTPAppender<TestSMTPAppender>();
	private Logger iLog;

	@Before
	public void setUp() throws Exception {
		LoggerContext defaultLoggerContext = new LoggerContext();
		try {
			new ContextInitializer(defaultLoggerContext).autoConfig();
		} catch (JoranException je) {
			Util.report("Failed to auto configure default logger context", je);
		}
		/*
		 * MDCAdapter应该是具体的日志实现,mock之
		 */
		PowerMockito.mockStatic(MDC.class);
		PowerMockito.when(MDC.getMDCAdapter()).thenReturn(new LogbackMDCAdapter());
		
		StatusPrinter.printInCaseOfErrorsOrWarnings(defaultLoggerContext);
		iLog = defaultLoggerContext.getLogger(TestSMTPAppender.class.getName());
	}
	
	@Test
	public void test_sendEmail() {
		iLog.error("this is a test email message");
//		try {
//			Thread.sleep(10000);
//		} catch (InterruptedException e) {
//		}

		for(int i = 0; i < 10; i++) {
			iLog.error("this is a test email message");
		}
		
		try {
			Thread.sleep(12000);
		} catch (InterruptedException e) {
		}
		iLog.error("this is a test email message");
	}
	
	@Test
	public void test_appender() {
		iLog.info("this is a common log");
		iLog.info("this is a common log: {}", new RuntimeException("this is a mock exception"));
		iLog.info("this is a common log: {}", new Object[]{new RuntimeException("this is a mock exception in arrays")});
	
		iLog.error("this is a error log");
		iLog.error("this is a error log: {}", new RuntimeException("this is a mock exception"));
		iLog.error("this is a error log: {}", new Object[]{new RuntimeException("this is a mock exception in arrays")});
	}
	
	/**
	 * 
	 * 正确配置
	 */
	@Test
	public void test_setEmail() {
		// 单个邮件
		String emailss = "chenming@yy.com";
		appender.setEmails(emailss);
		
		// 多个邮件
		emailss = "chenming@yy.com, xxx@163.com";
		appender.setEmails(emailss);
		
		// 以","结尾
		emailss = "chenming@yy.com, xxx@163.com, ";
		appender.setEmails(emailss);
	}
	
	@Test
	public void test_setEmail_error() {
		String emailss = "chenming@yy";
		try {
			appender.setEmails(emailss);
			fail();
		} catch (Exception e) {
			assertEquals("email地址配置错误:["+emailss+"]", e.getMessage());
		}
		
		emailss = "chenming@yy.com, xxx@com";
		try {
			appender.setEmails(emailss);
			fail();
		} catch (Exception e) {
			assertEquals("email地址配置错误:["+emailss+"]", e.getMessage());
		}
	}
}
