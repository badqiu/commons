package com.github.rapid.common.perf_test;

import java.lang.reflect.Method;

import org.junit.Test;

import com.github.rapid.common.util.Profiler;
import com.github.rapid.common.util.graph.Graph;

public class ReflectPerfTest {
	
	@Test
	public void testMethod() throws Exception {
		Graph g = new Graph();
		Method m = g.getClass().getMethod("getNodes");
		m.invoke(g);
		
		long count = 1000000000;
		Profiler.start(count);
		for(long i = 0; i < count; i++)
			m.invoke(g);
		Profiler.release();
		
		System.out.println(Profiler.dump());
	}
	
}
