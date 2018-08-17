package com.github.rapid.common.util;
public class ThreadUtil {

	public static void sleep(long millis) {
		try {
			if(millis > 0)
				Thread.sleep(millis);
		} catch (InterruptedException e) {
			throw new RuntimeException("InterruptedException",e);
		}
	}
	
	public static void sleepSeconds(long seconds) {
		sleep(seconds * 1000);
	}
	
}