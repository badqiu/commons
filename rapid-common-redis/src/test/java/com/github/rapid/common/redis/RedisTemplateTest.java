package com.github.rapid.common.redis;

import org.junit.Assert;
import org.junit.Test;

import com.github.rapid.common.redis.RedisTemplate;

import redis.clients.jedis.JedisPool;


public class RedisTemplateTest extends Assert {

	String key = "RedisTemplateTest_"+System.currentTimeMillis();
	
	@Test
	public void test() throws InterruptedException {
		final RedisTemplate t = new RedisTemplate();
		
		t.setJedisPool(new JedisPool("localhost",6379));
		
		for(int i = 0; i < 10000; i++) {
			runThread(t, i);
		}
		
		Thread.sleep(1000 * 100);
	}

	private void runThread(final RedisTemplate t, final int i) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				t.set(key, "100");
				assertEquals("100",t.get(key));
				System.out.println("execute i:"+i);
			}
		}).start();;
	}
}
