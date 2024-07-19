package com.github.rapid.common.util;

import java.time.Duration;

import org.junit.Test;

public class ThreadUtilTest {

	@Test
	public void test() {
		Profiler.start("sleep");
		ThreadUtil.sleep(0);
		ThreadUtil.sleep(-1);
		ThreadUtil.sleep(100);
		ThreadUtil.sleepSeconds(1);
		ThreadUtil.sleep(Duration.ofSeconds(1));
		Profiler.release();
		System.out.println(Profiler.dump());
	}

}
