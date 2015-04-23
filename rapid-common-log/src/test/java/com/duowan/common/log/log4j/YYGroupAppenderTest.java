package com.duowan.common.log.log4j;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class YYGroupAppenderTest {
	static Logger logger = LoggerFactory.getLogger(YYGroupAppenderTest.class);
	@Test
	public void test() {
		for(int i = 0; i < 10; i++) {
			logger.error("error msg",new SecurityException("exception msg"));
		}
	}
}
