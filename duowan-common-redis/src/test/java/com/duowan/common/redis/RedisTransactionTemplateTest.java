package com.duowan.common.redis;

import org.junit.Assert;
import org.junit.Test;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;
import redis.clients.jedis.Transaction;


public class RedisTransactionTemplateTest extends Assert {

	JedisPool jedisPool = new JedisPool("113.108.228.101");
	String key = ""+System.currentTimeMillis();
	@Test
	public void test1() throws Exception {
		RedisTemplate t = new RedisTemplate();
		t.setJedisPool(jedisPool);
		t.afterPropertiesSet();
		
		t.execute(new RedisTransactionCallback<Object>() {

			public Object doInTransaction(Transaction tran) {
				tran.set(key, "100");
				tran.exec();
				return null;
			}
			
		});
		
		Object obj = t.execute(new RedisTransactionCallback<String>() {

			public String doInTransaction(Transaction tran) {
				Response<String> r = tran.get(key);
				tran.exec();
				return r.get();
			}
			
		});
		
		assertEquals("100",obj);
	}
	
	@Test
	public void test2() throws Exception {
		JedisPool jedisPool = new JedisPool("113.108.228.101");
		RedisTemplate t = new RedisTemplate(jedisPool);
		t.afterPropertiesSet();
	}
}
