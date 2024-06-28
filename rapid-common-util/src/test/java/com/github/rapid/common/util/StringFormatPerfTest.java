package com.github.rapid.common.util;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class StringFormatPerfTest {

	@Test
	public void test() {
		int loopCount = 100000;
		String temp = null;
		Profiler.start("perf test start");
		
		Profiler.enter("String.format()",loopCount);
		for(int i = 0; i < loopCount; i++) {
			temp = String.format("%s_%s_%s_%s", "11111","22222","33333","44444");
		}
		Profiler.release();
		
		String[] array = new String[] {"11111","22222","33333","44444"};
		Profiler.enter("String.join()",loopCount);
		for(int i = 0; i < loopCount; i++) {
			temp = StringUtils.join(array);
		}
		Profiler.release();
		
		Profiler.release();
		
		System.out.println(Profiler.dump());
		System.out.println(temp);
	}
}
