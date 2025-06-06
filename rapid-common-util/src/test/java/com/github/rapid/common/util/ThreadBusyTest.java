package com.github.rapid.common.util;

import org.apache.commons.lang3.RandomStringUtils;
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
					String str = RandomStringUtils.randomAlphabetic(100);
					if(count > maxCount) {
						count = 0;
					}
					if(str.contains("111111111111111111")) {
						count++;
					}
				}
			}
		};
		
		t.start();
		
		ThreadUtil.sleepMinutes(100000);
	}

}
