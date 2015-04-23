package com.duowan.common.util;

import org.junit.Test;

public class ProfilerDemoTest {

	@Test
	public void test_simple_loop() {
		int loopCount = 1000000000;
		Profiler.start("test_simple_loop",loopCount); //开始计时
		for(int i = 0; i < loopCount; i++) {
			int a = i * 100;
		}
		Profiler.release(); //结束计时
		
		Profiler.printDump(); //打印日志至控制台
	}
	
	@Test
	public void test_loop() {
		int loopCount = 1000000000;
		Profiler.start("test_loop_a*100",loopCount); //开始计时
		for(int i = 0; i < loopCount; i++) {
			int a = i * 100;
		}
		
		testThrowException();		
		
		Profiler.release(); //结束计时
		
		Profiler.printDump(); //打印日志至控制台
		
	}

	private void testThrowException() {
		int loopCount = 1000000;
		Profiler.enter("test_throw_exception",loopCount);  //进入:子任务计时
		for(int i = 0; i < loopCount; i++) {
			try {
				throw new RuntimeException();
			}catch(Exception e){
			}
		}
		Profiler.release(); //结束:子任务计时
	}
	
	
}
