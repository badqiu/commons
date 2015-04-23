package com.github.rapid.common.spring.jdbc;

import org.junit.Test;

import com.github.rapid.common.spring.jdbc.BeanPropertyRowMapper;
import com.github.rapid.common.util.ClassTestUtil;


public class BeanPropertyRowMapperTest {

	@Test
	public void test() {
		BeanPropertyRowMapper b = new BeanPropertyRowMapper();
		ClassTestUtil.invokeAllMethods(b);
	}
}
