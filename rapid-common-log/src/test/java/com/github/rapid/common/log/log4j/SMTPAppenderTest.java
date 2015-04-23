package com.github.rapid.common.log.log4j;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SMTPAppenderTest {
	static Logger logger = LoggerFactory.getLogger(SMTPAppenderTest.class);
	
	@Test
	public void test() throws InterruptedException {
		for(int i = 0; i < 100; i++) {
			logger.error("error msg",new SecurityException("exception msg"));
		}
		Thread.sleep(1000);
	}
	
}
