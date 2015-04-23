package com.github.rapid.common.util;

import org.junit.Assert;
import org.junit.Test;

import com.github.rapid.common.util.RegexUtils;


public class RegexUtilsTest extends Assert {
	
	@Test(expected=IllegalArgumentException.class)
	public void test_findByRegexGroup() {
		assertEquals(null,RegexUtils.findByRegexGroup(null, "a", 0));
		assertEquals("a",RegexUtils.findByRegexGroup("abc123", "a", 0));
		assertEquals("abc123",RegexUtils.findByRegexGroup("abc123", "(a).*(123)", 0));
		assertEquals("a",RegexUtils.findByRegexGroup("abc123", "(a).*(123)", 1));
		assertEquals("123",RegexUtils.findByRegexGroup("abc123", "(a).*(123)", 2));
		
		assertEquals(null,RegexUtils.findByRegexGroup(" asdf", null, 0));
	}
}
