package com.github.rapid.common.util;

import java.time.Duration;

import com.github.rapid.common.exception.RuntimeInterruptedException;

public class ThreadUtil {

	public static void sleep(long millis) {
		try {
			if(millis > 0)
				Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeInterruptedException("InterruptedException",e);
		}
	}
	
	public static void sleepSeconds(long seconds) {
		sleep(seconds * 1000);
	}
	
	public static void sleep(Duration duration) {
		sleep(duration.toMillis());
	}
	
}