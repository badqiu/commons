package com.github.rapid.common.log;

import org.junit.Assert;
import org.junit.Test;


public class ProfilerRunnableTest extends Assert{
	
	@Test
	public void test() throws InterruptedException {
		Thread t = new Thread(new Runnable() {
			public void run() {
				String msg = ProfilerRunnable.defaultProfilerMessage(null);
				System.out.println(msg);
				assertEquals(msg,"Thread-");
			}
		}
		);
		t.start();
		t.join();
		
	}
}
