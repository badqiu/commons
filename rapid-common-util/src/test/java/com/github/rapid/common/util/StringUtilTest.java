package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilTest {

	@Test
	public void underscoreName() {
		assertEquals("test_name",StringUtil.underscore("TestName"));
		assertEquals("test_name123_sex",StringUtil.underscore("testName123Sex"));
	}

	@Test
	public void camelCaseName() {
		assertEquals("testName",StringUtil.camelCase("test_name"));
		assertEquals("testName123Sex",StringUtil.camelCase("test_name123_sex"));
		
		assertEquals("testNameAbc",StringUtil.camelCase("testNameAbc"));
		assertEquals("testNameAbc",StringUtil.camelCase("test_Name_Abc"));
	}
	
}
