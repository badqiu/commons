package com.github.rapid.common.redis;

import org.junit.Assert;
import org.junit.Test;

import com.github.rapid.common.redis.RedisTemplate;

import redis.clients.jedis.JedisPool;


public class RedisTemplateTest extends Assert {

	String key = "RedisTemplateTest_"+System.currentTimeMillis();
	
	@Test
	public void test() {
		RedisTemplate t = new RedisTemplate();
		
		t.setJedisPool(new JedisPool("113.108.228.101"));
		
		t.set(key, "100");
		assertEquals("100",t.get(key));
	}
}
