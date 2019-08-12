package com.github.rapid.common.redis;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.util.Assert;

import redis.clients.jedis.JedisPool;

public class JedisPoolFactoryBeanTest extends BaseRedisTest {

	@Test
	public void test() throws Exception {
		JedisPoolFactoryBean f = new JedisPoolFactoryBean();
		f.setServer("redis://120.92.6.245:6379/1");
		f.afterPropertiesSet();
		
		JedisPool pool = (JedisPool)f.getObject();
		Assert.notNull(pool);
		
		RedisTemplate template = new RedisTemplate();
		template.setJedisPool(pool);
		
		template.set("hello", "123");
		assertEquals("123",template.get("hello"));
	}

}
