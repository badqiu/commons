package com.github.rapid.common.log.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;

import com.github.rapid.common.log.helper.LogbackTriggerUtil.OUTPUT_FORMAT;

/**
 * TestLogbackTriggerUtil.java
 * 2013-1-28 下午03:29:54
 * 
 * @author chenming@yy.com
 * TODO:说点什么呗
 * 
 */
public class TestLogbackTriggerUtil {
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testEvent2Key() {
		ILoggingEvent logEvent = Mockito.mock(ILoggingEvent.class);
		StackTraceElement[] stacks = new StackTraceElement[1];
		stacks[0] = new StackTraceElement("com.duowan.common.log.helper.TestLogbackTriggerUtil", 
				"testEvent2Key", "TestLogbackTriggerUtil.java", 33);
		Mockito.when(logEvent.getCallerData()).thenReturn(stacks);
		String key = LogbackTriggerUtil.event2Key(logEvent);
		System.out.println(key);
		assertEquals("com.duowan.common.log.helper.TestLogbackTriggerUtil.testEvent2Key(TestLogbackTriggerUtil.java:33)", key);
	}

	@Test
	public void testEvent2String() {
		ILoggingEvent logEvent = Mockito.mock(ILoggingEvent.class);
		ThrowableProxy throwableProxy = Mockito.mock(ThrowableProxy.class);
		StackTraceElement[] threadStacks = Thread.currentThread().getStackTrace();
		StackTraceElementProxy[] stacks = new StackTraceElementProxy[threadStacks.length];
		for(int i=0; i < threadStacks.length; i++) {
			stacks[i] = new StackTraceElementProxy(threadStacks[i]);
		}
		
		Mockito.when(throwableProxy.getStackTraceElementProxyArray()).thenReturn(stacks);
		Mockito.when(logEvent.getThrowableProxy()).thenReturn(throwableProxy);
		String string = LogbackTriggerUtil.event2String(logEvent, OUTPUT_FORMAT.TEXT);
		assertNotNull(string);
		System.out.println(string);
	}
	
	@Test
	public void testEvent2String2() {
		ILoggingEvent logEvent = Mockito.mock(ILoggingEvent.class);
		Mockito.when(logEvent.getThrowableProxy()).thenReturn(null);
		Object[] args = new Object[]{"this is output error", new RuntimeException("this is a test error")};
		Mockito.when(logEvent.getArgumentArray()).thenReturn(args);
		String string = LogbackTriggerUtil.event2String(logEvent, OUTPUT_FORMAT.TEXT);
		assertNotNull(string);
		System.out.println(string);
	}

	@Test
	public void testSimpleEvent2String() {
		ILoggingEvent logEvent = Mockito.mock(ILoggingEvent.class);
		String message = "this is a mock error";
		Mockito.when(logEvent.getFormattedMessage()).thenReturn(message);
		StackTraceElement[] traces = new StackTraceElement[1]; 
		traces[0] = new StackTraceElement(TestLogbackTriggerUtil.class.getName(), 
				"testSimpleEvent2String", "TestLogbackTriggerUtil.java", 75);
		Mockito.when(logEvent.getCallerData()).thenReturn(traces);
		
		String str = LogbackTriggerUtil.simpleEvent2String(logEvent);
		System.out.println(str);
		assertNotNull(str);
	}

	ILoggingEvent testEvent() {
		ILoggingEvent logEvent = Mockito.mock(ILoggingEvent.class);
//		Mockito.when(logEvent.getLevel()).thenReturn(Level.INFO);
		
		Object[] argument = new Object[]{"this has a error {}", new RuntimeException("fuck all test")};
		Mockito.when(logEvent.getArgumentArray()).thenReturn(argument);
		
		ThrowableProxy throwableProxy = Mockito.mock(ThrowableProxy.class);
		StackTraceElementProxy[] stacks = new StackTraceElementProxy[5];
		stacks[0] = Mockito.mock(StackTraceElementProxy.class);
		Mockito.when(stacks[0].toString()).thenReturn("this.is.a.stack.trace.Method()");
		Mockito.when(throwableProxy.getStackTraceElementProxyArray()).thenReturn(stacks);
		
		Mockito.when(logEvent.getThrowableProxy()).thenReturn(throwableProxy);
		
		return logEvent;
	}
}
