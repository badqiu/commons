package com.github.rapid.common.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.util.Assert;

public class NumberUtilTest {

	@Test
	public void toNumber() {
		assertEquals(null,NumberUtil.toNumber(null));
		assertEquals(1.0,NumberUtil.toNumber("1"));
		assertEquals(1.1,NumberUtil.toNumber("1.10"));
		assertEquals(100L,NumberUtil.toNumber(100L));
		assertEquals(1.11,NumberUtil.toNumber(1.11));
		
		assertEquals(1.0,NumberUtil.toNumber("abc1"));
		assertEquals(-1.1,NumberUtil.toNumber("-1.10"));
		assertEquals(1.0,NumberUtil.toNumber("1abc1"));
		assertEquals(-1.1,NumberUtil.toNumber("yyy-1.10ddd"));
		assertEquals(-1.01,NumberUtil.toNumber("-1.01abc2.1"));
		assertEquals(-1.1056,NumberUtil.toNumber("yyy-1.1056ddd"));
	}
	
	@Test
	public void toLong() {
		assertEquals(null,NumberUtil.toLong(null));
		assertEquals(1L,NumberUtil.toLong("1"));
		assertEquals(1L,NumberUtil.toLong("1.10"));
		assertEquals(100L,NumberUtil.toLong(100L));
		assertEquals(1L,NumberUtil.toLong(1.11));
		assertEquals(Long.MAX_VALUE,NumberUtil.toLong(Long.MAX_VALUE+".111110000"));
		assertEquals(Long.MAX_VALUE,NumberUtil.toLong(Double.MAX_VALUE));
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
	
	public static void assertEquals(Object o1,Object o2) {
		if(o1 == o2) return;
		Assert.isTrue(o1.equals(o2),"error must equals,v1="+o1+" v2="+o2);
	}
}
