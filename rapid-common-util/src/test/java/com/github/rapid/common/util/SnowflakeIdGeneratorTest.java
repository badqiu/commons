package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class SnowflakeIdGeneratorTest {

	@Test
	public void generateId() {
		for(int i = 0 ; i < 5000; i++) {
			long generateId = SnowflakeIdGenerator.generateId();
			System.out.println(generateId+"-"+String.valueOf(generateId).length());
		}
		
		SnowflakeIdGenerator id = new SnowflakeIdGenerator(1,1);
		System.out.println(id.sequenceMask);
		System.out.println(id.sequenceBits);
	}

}
