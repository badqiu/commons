package com.github.rapid.common.redis;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.embedded.RedisServer;

public class BaseRedisTest {

	RedisServer redisServer = null;
	@Before
	public void startRedisServer() throws Exception {
//		redisServer = new RedisServer(6379);
//		redisServer.start();
	}
	
	@After
	public void closeRedisServer() {
		if(redisServer != null) {
			redisServer.stop();
		}
	}
	
	@Test
	public void empty() throws InterruptedException {
		Thread.sleep(1000 * 1000);
	}
	
}
