package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class LogTraceUtilTest {

	@Test
	public void newTraceId() {
		for(int i = 0; i <100; i++) {
			String traceId = LogTraceUtil.newTraceId();
			System.out.println(traceId+"-"+traceId.length());
			ThreadUtil.sleep(100);
		}
	}

}
