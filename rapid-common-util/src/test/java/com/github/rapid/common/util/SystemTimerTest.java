package com.github.rapid.common.util;

import java.util.Date;

import org.junit.Test;


public class SystemTimerTest {

	@Test
	public void test() throws InterruptedException {
		for(int i = 0; i < 100; i++) {
			System.out.println(SystemTimer.currentTimeMillis());
			Thread.sleep(50);
		}
	}
	
	@Test
	public void test_perf() throws InterruptedException {
		long count = 1000000000;
		Profiler.start("start time test");
		
		Profiler.enter("SystemTimer.currentTimeMillis",count);
		for(long i = 0; i < count; i++) {
			SystemTimer.currentTimeMillis();
		}
		Profiler.release();
		
		Profiler.enter("System.currentTimeMillis",count);
		for(long i = 0; i < count; i++) {
			System.currentTimeMillis();
		}
		Profiler.release();
		
		Profiler.enter("new java.util.Date()",count);
		for(long i = 0; i < count; i++) {
			new Date();
		}
		Profiler.release();
		
		Profiler.enter("new Object()",count);
		for(long i = 0; i < count; i++) {
			new Object();
		}
		Profiler.release();
		
		Profiler.release();
		System.out.println(Profiler.dump());
	}
	
	@Test
	public void test_sleep() throws InterruptedException {
		int count = 300;
		Profiler.start("sleep",count);
		for(int i = 0; i < count; i++) {
			Thread.sleep(10);
		}
		Profiler.release();
		System.out.println(Profiler.dump());
	}
}
