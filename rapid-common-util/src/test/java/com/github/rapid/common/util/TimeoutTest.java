package com.github.rapid.common.util;

import static org.junit.Assert.*;

import java.time.Duration;

import org.junit.Test;

public class TimeoutTest {

	@Test
	public void isTimeout() throws InterruptedException {
		Timeout timeout = new Timeout(Duration.ofSeconds(3));
		assertFalse(timeout.isTimeout());
		Thread.sleep(3100);
		assertTrue(timeout.isTimeout());
		
		
		timeout = new Timeout(Duration.ofSeconds(1));
		assertFalse(timeout.isTimeout());
		Thread.sleep(500);
		assertFalse(timeout.isTimeout());
		Thread.sleep(600);
		assertTrue(timeout.isTimeout());
	}

}
