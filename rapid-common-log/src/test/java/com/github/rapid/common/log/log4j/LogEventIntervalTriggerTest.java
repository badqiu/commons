package com.github.rapid.common.log.log4j;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.github.rapid.common.util.DateConvertUtils;

public class LogEventIntervalTriggerTest extends Assert{

	LogEventIntervalTrigger trigger = new LogEventIntervalTrigger();
	
	@Test
	public void test_isSendByException() throws Exception {
		trigger.setIntervalSeconds(2);
		assertTrue(trigger.isSendByException(exception1));
		assertTrue(trigger.isSendByException(exception2));
		assertFalse(trigger.isSendByException(exception1));
		assertFalse(trigger.isSendByException(exception2));
		
		Thread.sleep(2100);
		assertTrue(trigger.isSendByException(exception1));
		assertTrue(trigger.isSendByException(exception2));
	}
	
	@Test
	public void test_isSendByMsg() throws Exception {
		trigger.setIntervalSeconds(2);
		assertTrue(trigger.isSendByMsg());
		assertFalse(trigger.isSendByMsg());
		
		Thread.sleep(2100);
		assertTrue(trigger.isSendByMsg());
	}
	
//	LoggingEvent exception1 = new LoggingEvent("loggerClass", Category.getInstance("loggerClass"), Level.ERROR, "hello", new Exception("aaaa"));
//	LoggingEvent exception2 = new LoggingEvent("loggerClass", Category.getInstance("loggerClass"), Level.ERROR, "hello", new RuntimeException("bbbb"));
//	LoggingEvent msg = new LoggingEvent("loggerClass", Category.getInstance("loggerClass"), Level.ERROR, "hello", null);
	
	String exception1 = "com.duowan.common.log.log4j.LogEventIntervalTriggerTest";
	String exception2 = "com.duowan.common.log.log4j.LogEventIntervalTriggerTest2";
	String msg = null;
	
	@Test
	public void test_isSendEvent() throws Exception {
		trigger.setIntervalSeconds(2);
		assertTrue(trigger.isSendEvent(exception1));
		assertTrue(trigger.isSendEvent(exception2));
		assertTrue(trigger.isSendEvent(msg));
		assertFalse(trigger.isSendEvent(exception1));
		assertFalse(trigger.isSendEvent(msg));
		assertFalse(trigger.isSendEvent(exception2));
		
		Thread.sleep(2100);
		assertTrue(trigger.isSendEvent(exception1));
		assertTrue(trigger.isSendEvent(exception2));
		assertTrue(trigger.isSendEvent(msg));
		assertFalse(trigger.isSendEvent(exception1));
		assertFalse(trigger.isSendEvent(msg));
		assertFalse(trigger.isSendEvent(exception2));
	}
	
	@Test
	public void test_isSendEvent_by_cron() throws Exception {
		trigger.setIntervalSeconds(-1);
		trigger.setCrons("0/2 * * * * *  ;  0/2 * * * * *");
		assertTrue(trigger.isSendEvent(exception1));
		assertTrue(trigger.isSendEvent(exception2));
		assertTrue(trigger.isSendEvent(msg));
		assertFalse(trigger.isSendEvent(exception1));
		assertFalse(trigger.isSendEvent(msg));
		assertFalse(trigger.isSendEvent(exception2));
		
		Thread.sleep(2100);
		assertTrue(trigger.isSendEvent(exception1));
		assertTrue(trigger.isSendEvent(exception2));
		assertTrue(trigger.isSendEvent(msg));
		assertFalse(trigger.isSendEvent(exception1));
		assertFalse(trigger.isSendEvent(msg));
		assertFalse(trigger.isSendEvent(exception2));
	}
	
	Date testDate = DateConvertUtils.parse("2012-11-10 09:00:00", "yyyy-MM-dd HH:mm:ss");
	@Test
	public void test_isTriggerByCron() throws Exception {
		trigger.setCrons("0 0 9-12 * * *  ;  0 0 18-20 * * *");
		testDate = DateConvertUtils.parse("2012-11-10 09:00:02", "yyyy-MM-dd HH:mm:ss");
		assertTrue(trigger.isTriggerByCron(testDate,DateUtils.addHours(testDate,2)));
		assertFalse(trigger.isTriggerByCron(testDate,DateUtils.addMinutes(testDate,59)));
		
		DateConvertUtils.parse("2012-11-10 09:00:00", "yyyy-MM-dd HH:mm:ss");
		assertFalse(trigger.isTriggerByCron(testDate,DateUtils.addMinutes(testDate,59)));
	}
	
	@Test
	public void test_isActiveEnv() throws Exception {
		assertTrue(trigger.isActiveByENV());
		
		trigger.setActiveEnv("testENV");
		trigger.setActiveEnvValue("prod");
		assertFalse(trigger.isActiveByENV());
		
		System.setProperty("testENV", "prod");
		assertTrue(trigger.isActiveByENV());
		
	}
	
	@After
	public void testDown() {
		System.setProperty("testENV", "");
	}
}
