package com.github.rapid.common.util;

import static org.junit.Assert.fail;

import org.junit.Test;

public class LRUMapTest {

	LRUMap<Integer,Integer> userFriendCache = new LRUMap(10000,10000 * 20);
	@Test
	public void test() {
		int loop = 100000000;
		Profiler.start("put",loop);
		for(int i = 0; i < loop; i++) {
			userFriendCache.put(i,i);
		}
		Profiler.release();
		System.out.println(Profiler.dump());
		
		Profiler.start("get",loop);
		for(int i = 0; i < loop; i++) {
			userFriendCache.get(i);
		}
		Profiler.release();
		
		System.out.println(Profiler.dump());
		
		System.out.println(userFriendCache.size());
		
	}

}
