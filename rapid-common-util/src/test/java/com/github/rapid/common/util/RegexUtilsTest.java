package com.github.rapid.common.util;

import org.junit.Assert;
import org.junit.Test;

import com.github.rapid.common.util.RegexUtil;


public class RegexUtilsTest extends Assert {
	
	@Test(expected=IllegalArgumentException.class)
	public void test_findByRegexGroup() {
		assertEquals(null,RegexUtil.findByRegexGroup(null, "a", 0));
		assertEquals("a",RegexUtil.findByRegexGroup("abc123", "a", 0));
		assertEquals("abc123",RegexUtil.findByRegexGroup("abc123", "(a).*(123)", 0));
		assertEquals("a",RegexUtil.findByRegexGroup("abc123", "(a).*(123)", 1));
		assertEquals("123",RegexUtil.findByRegexGroup("abc123", "(a).*(123)", 2));
		
		assertEquals(null,RegexUtil.findByRegexGroup(" asdf", null, 0));
	}
}
