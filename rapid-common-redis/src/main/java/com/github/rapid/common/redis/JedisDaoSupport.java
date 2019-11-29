package com.github.rapid.common.redis;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import redis.clients.jedis.JedisPool;
/**
 * 
 * redis dao base
 * @author badqiu
 *
 */
public class JedisDaoSupport implements InitializingBean{
	
	private JedisTemplate jedisTemplate;
	
	public void setJedisPool(JedisPool jedisPool) {
		setJedisTemplate(new RedisTemplate(jedisPool));
	}
	
	public JedisTemplate getJedisTemplate() {
		return jedisTemplate;
	}

	public void setJedisTemplate(JedisTemplate jedisTemplate) {
		this.jedisTemplate = jedisTemplate;
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(jedisTemplate,"jedisTemplate must be not null");
	}
	
}
