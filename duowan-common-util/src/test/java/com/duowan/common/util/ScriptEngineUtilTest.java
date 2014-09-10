package com.duowan.common.util;

import org.junit.Assert;
import org.junit.Test;


public class ScriptEngineUtilTest extends Assert{
	
	@Test
	public void test() {
		int count = 100000;
		Profiler.start("javascript",count);
		for(int i = 0; i < count; i++) {
			assertEquals(2d,ScriptEngineUtil.eval("javascript", "1+1"));
		}
		Profiler.release();
		System.out.println(Profiler.dump());
	}
	
}
