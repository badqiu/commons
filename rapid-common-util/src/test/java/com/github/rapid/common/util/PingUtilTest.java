package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Test;


public class PingUtilTest {
	
	@Test
	public void test() {
		boolean r = PingUtil.socketPing("www.baidu.com", 80);
		assertTrue(r);
	}

	@Test
	public void test_error() {
		boolean r = PingUtil.socketPing("www.baidu.com", 12922);
		assertFalse(r);
	}

}
