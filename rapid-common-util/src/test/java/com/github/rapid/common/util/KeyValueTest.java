package com.github.rapid.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class KeyValueTest {

	@Test
	public void test() {
		assertNull(KeyValue.parse(null));
		assertNull(KeyValue.parse(" "));
		assertNull(KeyValue.parse(""));
		assertNull(KeyValue.parse(" \t \n"));

		assertEquals("name=123",KeyValue.parse(" name = 123").toString());
		assertEquals("name=",KeyValue.parse(" name = ").toString());
		assertEquals(null,KeyValue.parse("  = "));
		assertEquals("name=",KeyValue.parse("name= ").toString());
		assertEquals(null,KeyValue.parse("="));
		assertEquals(null,KeyValue.parse("= "));
		assertEquals(null,KeyValue.parse(" = "));
		assertEquals(null,KeyValue.parse("=123"));
	}

}
