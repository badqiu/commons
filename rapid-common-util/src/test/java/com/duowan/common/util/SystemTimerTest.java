package com.duowan.common.util;

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
		int count = 1000000000;
		Profiler.start("SystemTimer.currentTimeMillis",count);
		for(int i = 0; i < count; i++) {
			SystemTimer.currentTimeMillis();
		}
		Profiler.release();
		System.out.println(Profiler.dump());
		
		Profiler.start("System.currentTimeMillis",count);
		for(int i = 0; i < count; i++) {
			System.currentTimeMillis();
		}
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
