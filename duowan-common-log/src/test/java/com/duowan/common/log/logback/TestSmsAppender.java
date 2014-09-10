package com.duowan.common.log.logback;

import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.MDC;
import org.slf4j.helpers.Util;
import org.springframework.scheduling.support.CronSequenceGenerator;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.classic.util.LogbackMDCAdapter;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * TestSmsAppender.java
 * 2012-12-4 上午09:33:10
 * 
 * @author chenming@chinaduo.com
 *         TODO:说点什么呗
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MDC.class})
public class TestSmsAppender {
	private Logger iLog = null;
	final static Object key = new Object();

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
		iLog = defaultLoggerContext.getLogger(TestSmsAppender.class.getName());
		
		/*
		 * 从StaticLoggerBinder类中copy的代码
		 */ 
//		ContextSelectorStaticBinder contextSelectorBinder = ContextSelectorStaticBinder
//				.getSingleton();
//		boolean initialized = false;
//		
//		try {
//			try {
//				new ContextInitializer(defaultLoggerContext).autoConfig();
//			} catch (JoranException je) {
//				Util.report("Failed to auto configure default logger context", je);
//			}
//			StatusPrinter.printInCaseOfErrorsOrWarnings(defaultLoggerContext);
//			contextSelectorBinder.init(defaultLoggerContext, key);
//			initialized = true;
//		} catch (Throwable t) {
//			Util.report("Failed to instantiate [" + LoggerContext.class.getName() + "]", t);
//		}
//
//		LoggerContext factory = null;
//		if (!initialized) {
//			factory = defaultLoggerContext;
//		}
//
//		if (contextSelectorBinder.getContextSelector() == null) {
//			throw new IllegalStateException("contextSelector cannot be null. See also "
//					+ CoreConstants.CODES_URL + "#null_CS");
//		}
//		factory = contextSelectorBinder.getContextSelector().getLoggerContext();
//		
//		iLog = factory.getLogger(TestSmsAppender.class.getName());
	}

	@Test
	public void test_mockStaticMDCBinder() throws Exception {
		PowerMockito.mockStatic(MDC.class);
		PowerMockito.when(MDC.getMDCAdapter()).thenReturn(new LogbackMDCAdapter());
		
	}
	
	@Test
	public void test_regex() {
		String phones = "13876505073,15919178983";
		assertTrue(phones.matches(SmsAppender.phoneRgx));
	}

	@Test
	public void testSmsAppender() {
		// fail("Not yet implemented");
		Throwable throwable = new RuntimeException("this is a test error");
		iLog.error("{}", throwable);
		for (int i = 0; i < 10; i++) {
			iLog.error("{}", new Object[] { throwable });
		}
		
		try {
			Thread.sleep(5 * 1000l);
		} catch (InterruptedException e) {
			iLog.error("exception:{}", e);
		}
		
		System.out.println("wait out...");
		iLog.error("{}", new Object[] { throwable });
	}

	@Test
	public void test_CronSequenceGenerator() {
		String expression = "0 */6 * * * 6-7";
		CronSequenceGenerator cron = new CronSequenceGenerator(expression, TimeZone.getDefault());
		Date next = cron.next(new Date());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(next));

		String rule = "xxx,12;aaa,3;";
		String[] rules = rule.split(";");
		for (String r : rules) {
			System.out.println(r);
		}
	}

	// @Test
	// public void testAppendE() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testSmsContent() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testIsTrigger() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testGetInterval() {
	// fail("Not yet implemented");
	// }
	//
	// @Test
	// public void testFormat() {
	// fail("Not yet implemented");
	// }
}
