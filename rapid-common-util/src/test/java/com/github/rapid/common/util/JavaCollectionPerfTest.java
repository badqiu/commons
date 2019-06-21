package com.github.rapid.common.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

public class JavaCollectionPerfTest {

	@Test
	public void testMap() {
		Map map = new HashMap();
		for(int i = 0; i < 1000; i++) {
			map.put(RandomStringUtils.random(i), i);
		}
		String key = "random_key";
		int loopCount = 10000 * 10000;
		Profiler.start();
		Profiler.enter("HashMap.get() ",loopCount);
		for(int i = 0; i < loopCount;i++) {
			Object o = map.get(key);
		}
		Profiler.release();
		
		loopCount = 10000 * 100;
		Profiler.enter("HashMap.put() ",loopCount);
		for(int i = 0; i < loopCount;i++) {
			map.put(i, i);
		}
		Profiler.release();
		Profiler.release();
		
		System.out.println(Profiler.dump());
	}
}
