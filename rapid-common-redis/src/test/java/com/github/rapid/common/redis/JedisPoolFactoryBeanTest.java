package com.github.rapid.common.redis;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.util.Assert;

import redis.clients.jedis.JedisPool;

public class JedisPoolFactoryBeanTest extends BaseRedisTest {

	@Test
	public void testSet() throws Exception {
		JedisPoolFactoryBean f = new JedisPoolFactoryBean();
		f.setServer("redis://localhost:"+SimpleRedisServer.DEFAULT_PORT+"/1");
		f.afterPropertiesSet();
		
		JedisPool pool = (JedisPool)f.getObject();
		Assert.notNull(pool);
		
		JedisTemplate template = new JedisTemplate();
		template.setJedisPool(pool);
		
		System.out.println("set before");
		template.set("hello", "123");
		System.out.println("set after");
		
		assertEquals("123",template.get("hello"));
	}

}
