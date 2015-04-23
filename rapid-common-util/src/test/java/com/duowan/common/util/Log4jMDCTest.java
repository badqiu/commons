package com.duowan.common.util;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class Log4jMDCTest {
	private Logger logger = LoggerFactory.getLogger(Log4jMDCTest.class);
	@Test
	public void test() {
		
		MDC.put("loginUserId", "badqiu");
		try {
			logger.info("hello");
			logger.info("my name is haimeimei");
		}finally {
			MDC.remove("loginUserId");
		}
	}
	
	@Test
	public void testTraceId() {
		MDC.put("traceId", System.currentTimeMillis()+RandomStringUtils.randomAlphanumeric(5));
		try {
			logger.info("hello");
			logger.info("my name is haimeimei");
		}finally {
			MDC.remove("traceId");
		}
	}
	
}
