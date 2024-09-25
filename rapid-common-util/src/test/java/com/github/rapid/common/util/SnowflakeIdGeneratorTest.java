package com.github.rapid.common.util;

import java.sql.Timestamp;

import org.junit.Test;

public class SnowflakeIdGeneratorTest {

	@Test
	public void generateId() {
		for(int i = 0 ; i < 5000; i++) {
			long generateId = SnowflakeIdGenerator.generateId();
			Timestamp timestamp = SnowflakeIdGenerator.convertToTimestamp(generateId);
			System.out.println(generateId+" => size:"+String.valueOf(generateId).length()+" "+(Long.MAX_VALUE - generateId) +" timestamp:"+timestamp);
		}
		
		SnowflakeIdGenerator id = new SnowflakeIdGenerator(1,1);
		System.out.println(id.sequenceMask);
		System.out.println(id.sequenceBits);
		
		long count = 100000;
		Profiler.startRelease("generateId_perf_test_by_sequenceBits="+id.sequenceBits,count, () -> {
			for(int i = 0; i < count; i++) {
				SnowflakeIdGenerator.generateId();
			}
		});
		
		Profiler.printDump();
	}
	
	@Test
	public void generateId2() {
		SnowflakeIdGenerator.generateId();
	}

}
