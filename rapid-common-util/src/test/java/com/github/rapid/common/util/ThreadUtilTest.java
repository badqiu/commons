package com.github.rapid.common.util;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

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

	@Test
	public void sleepByUnit() {
		Profiler.start("sleep");
		ThreadUtil.sleep(2, TimeUnit.SECONDS);
		Profiler.release();
		System.out.println(Profiler.dump());
	}
}
