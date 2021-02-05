package com.github.rapid.common.log;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.github.rapid.common.log.standard_log_format.EventLoggerImpl;
import com.github.rapid.common.log.standard_log_format.LoggerMsg;

public class EventLoggerImplTest {

	EventLoggerImpl logger = new EventLoggerImpl(EventLoggerImpl.class);
	@Test
	public void test() {
		logger.info(new LoggerMsg().eventType(EventLoggerImpl.class).eventAction("run").message(""));
	}

}
