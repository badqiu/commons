package com.duowan.common.redis;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
/**
 * 
 * Jedis拦截器，用于生成Jedis代理. 调用代理的方法,都将坐JedisPool获取Jedis实例
 * 
 * @author badqiu
 *
 */
public class JedisInteceptor implements  MethodInterceptor {

	private JedisPool jedisPool;
	
	public JedisInteceptor(JedisPool jedisPool) {
		super();
		this.jedisPool = jedisPool;
	}

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Jedis jedis = jedisPool.getResource();
		try {
			Method m = invocation.getMethod();
			return m.invoke(jedis, invocation.getArguments());
		}catch(Exception e) {
			jedisPool.returnBrokenResource(jedis);
			throw e;
		}finally {
			jedisPool.returnResource(jedis);
		}
	}

	public void setJedisPool(JedisPool jedisPool) {
		this.jedisPool = jedisPool;
	}

	
}
