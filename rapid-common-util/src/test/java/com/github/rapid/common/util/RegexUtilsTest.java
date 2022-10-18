package com.github.rapid.common.util;

import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RegexUtilsTest extends Assert {
	
	private static Logger logger = LoggerFactory.getLogger(RegexUtilsTest.class);
	
	@Test
	public void print_log() {
		logger.error("msg:{}, info:{}", "hello","info",new Exception());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void test_findByRegexGroup() {
		assertEquals(null,RegexUtil.findByRegexGroup(null, "a", 0));
		assertEquals("a",RegexUtil.findByRegexGroup("abc123", "a", 0));
		assertEquals("abc123",RegexUtil.findByRegexGroup("abc123", "(a).*(123)", 0));
		assertEquals("a",RegexUtil.findByRegexGroup("abc123", "(a).*(123)", 1));
		assertEquals("123",RegexUtil.findByRegexGroup("abc123", "(a).*(123)", 2));
		
		assertEquals(null,RegexUtil.findByRegexGroup(" asdf", null, 0));
	}
	
	@Test
	public void perf() {
		long count = 10000 * 100;
		Profiler.start("Pattern.compile",count);
		
		for(int i = 0; i < count; i++) {
			Pattern.compile("[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)");
		}
		
		Profiler.release();
		
		System.out.println(Profiler.dump());
	}
}
