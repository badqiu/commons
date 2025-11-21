package com.github.rapid.common.util;

import org.junit.Test;

public class ThreadTest {

	@Test
	public void test() throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				long count = 0;
				while(true) {
					ThreadUtil.sleepSeconds(1);
					System.out.println("execute:"+(count++));
				}
			}
		},"test-t");
		t.start();
		
		ThreadUtil.sleepSeconds(1);
		
//		t.interrupt();
//		t.destroy();
		System.out.println("stop");
		t.stop();
		
		ThreadUtil.sleepSeconds(5);
		
		System.out.println("resume");
		t.resume();
		
		ThreadUtil.sleepSeconds(5);
		
		System.out.println("suspend");
		t.suspend();
		
		ThreadUtil.sleepSeconds(5);
		System.out.println("destroy");
		t.destroy();
		
		
		System.out.println("sleepSeconds(1000)");
		ThreadUtil.sleepSeconds(1000);
	}
}
