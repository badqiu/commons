package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class UUIDUtilTest {

	@Test
	public void test_randomUUID32() {
		System.out.println(UUIDUtil.randomUUID32());
		assertEquals(32,UUIDUtil.randomUUID32().length());
	}

}
