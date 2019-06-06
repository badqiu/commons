package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class SnowflakeIdGeneratorTest {

	@Test
	public void generateId() {
		for(int i = 0 ; i < 100; i++)
			System.out.println(SnowflakeIdGenerator.generateId());
	}

}
