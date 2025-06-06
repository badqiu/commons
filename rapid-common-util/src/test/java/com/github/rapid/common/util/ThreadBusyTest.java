package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ThreadBusyTest {

	long count = 0;
	
	@Test
	public void test() {
		Thread t = new Thread("mycustom-buzy-thread") {
			public void run() {
				while(true) {
					count++;
					long maxCount = Integer.MAX_VALUE * 1000L;
					if(count > maxCount) {
						count = 0;
					}
				}
			}
		};
		
		t.start();
		
		ThreadUtil.sleepMinutes(100000);
	}

}
