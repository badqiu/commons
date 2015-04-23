package com.duowan.common.test.util;

import junit.framework.TestCase;

import org.junit.Test;

public class MultiThreadTestUtilsTest extends TestCase {
//	private AtomicInteger executedCount = new AtomicInteger();
	int expectedCount = 2000;
//	public void testExecute() throws InterruptedException {
//		CountDownLatch doneSignel = MultiThreadTestUtils.execute(expectedCount, new Runnable() {
//			public void run() {
//				executedCount.getAndIncrement();
//			}
//		});
//		
//		doneSignel.await();
//		
//		assertEquals(expectedCount,executedCount.intValue());
//	}
//	
//	public void testExecuteFail() throws InterruptedException {
//		CountDownLatch doneSignel = MultiThreadTestUtils.execute(expectedCount, new Runnable() {
//			public void run() {
//				executedCount.getAndIncrement();
//			}
//		});
//		
//		System.out.println(executedCount);
//		assertTrue(executedCount.intValue() < expectedCount);
//	}
//	
//	@Test(timeout=9000)	 //使用@Test指定超时
//	public void testPerf() throws InterruptedException {
//		int threadCount = 30; //使用多少条线程跑任务
//		long costTime = MultiThreadTestUtils.executeAndWait(threadCount, new Runnable() {
//			public void run() {
//				// do something 
//			}
//		});
//		//执行完成，可以得到多线程执行时间
//		System.out.println("cost seconds:"+(costTime/1000));
//	}
//	
	@Test
	public void testexecuteAndWaitForDone() throws InterruptedException {
		
		long costTime = MultiThreadTestUtils.executeAndWait(expectedCount, new Runnable() {
			public void run() {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		System.out.println("costTime:"+costTime);
		assertTrue(costTime > 0);
	}
//	
//	
//	public void testMultiThreadPermenece() throws InterruptedException {
//		Map map = new TreeMap();
//		int steps = 100;
//		for(int i = 1; i < 3000; i = i + steps) {
//			steps = steps + (int)(steps * 0.2);
//			long costTime = execute(i);
//			System.out.println("threadCount:"+ i +" costTime:"+costTime+" nextStep:"+steps);
//			map.put(costTime,i);
//		}
//		System.out.println(map);
//	}
//
//	long MAX_COUNT = 10000;
//	private long execute(int threadCount) throws InterruptedException {
//		final AtomicLong count = new AtomicLong(0);
//		long costTime = MultiThreadTestUtils.executeAndWait(threadCount, new Runnable() {
//			int selfCount = 0;
//			public void run() {
//				while(true) {
//					if(count.incrementAndGet() > MAX_COUNT) {
//						return;
//					}
//					for(int i = 0; i < 50000; i++) {
//					}
//				}
//			}
//		});
//		return costTime;
//	}
	public void test(){
	}
}
