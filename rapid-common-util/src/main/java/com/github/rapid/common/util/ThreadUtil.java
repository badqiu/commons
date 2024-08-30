package com.github.rapid.common.util;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

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
	
	public static void sleepMinutes(long minutes) {
		sleep(minutes * 1000 * 60);
	}
	
	public static void sleep(Duration duration) {
		sleep(duration.toMillis());
	}
	
	public static void sleep(int time,TimeUnit timeUnit) {
		sleep(timeUnit.toMillis(time));
	}
	
}