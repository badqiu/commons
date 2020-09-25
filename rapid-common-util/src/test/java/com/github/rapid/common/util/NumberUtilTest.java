package com.github.rapid.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class NumberUtilTest {

	@Test
	public void toNumber() {
		assertEquals(null,NumberUtil.toNumber(null));
		assertEquals(1.0,NumberUtil.toNumber("1"));
		assertEquals(1.1,NumberUtil.toNumber("1.10"));
		assertEquals(100L,NumberUtil.toNumber(100L));
		assertEquals(1.11,NumberUtil.toNumber(1.11));
	}

	@Test
	public void toBoolean() {
		assertEquals(null,NumberUtil.toBoolean(null));
		assertEquals(true,NumberUtil.toBoolean(1));
		assertEquals(false,NumberUtil.toBoolean(0));
		assertEquals(true,NumberUtil.toBoolean(-1));
		assertEquals(true,NumberUtil.toBoolean(true));
		assertEquals(false,NumberUtil.toBoolean(false));
		assertEquals(true,NumberUtil.toBoolean("true"));
		assertEquals(false,NumberUtil.toBoolean("1"));
		assertEquals(false,NumberUtil.toBoolean("false"));

	}
	
}
