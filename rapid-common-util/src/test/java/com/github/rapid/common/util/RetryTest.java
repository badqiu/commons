package com.github.rapid.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;

import org.junit.Test;

public class RetryTest {

	Callable<Object> errorCmd = new Callable<Object>() {
		public Object call() throws Exception {
			execCount++;
			if(true) throw new IllegalAccessException();
			return null;
		}
	};
	
	int execCount = 0;
	@Test
	public void testTimes() {
		try {
			Retry.retry(2, 10,errorCmd);
		}catch(Exception e) {
			e.printStackTrace();
		}
		assertEquals(execCount,3);
	}
	
	@Test
	public void test_interval() {
		long start = System.currentTimeMillis();
		try {
			Retry.retry(2, 1000,errorCmd);
		}catch(Exception e) {
			e.printStackTrace();
		}
		long cost = System.currentTimeMillis() - start;
		assertTrue(cost > 2000 && cost < 2100);
	}

}
