package com.duowan.common.spring.jdbc;

import org.junit.Test;

import com.duowan.common.util.ClassTestUtil;


public class BeanPropertyRowMapperTest {

	@Test
	public void test() {
		BeanPropertyRowMapper b = new BeanPropertyRowMapper();
		ClassTestUtil.invokeAllMethods(b);
	}
}
