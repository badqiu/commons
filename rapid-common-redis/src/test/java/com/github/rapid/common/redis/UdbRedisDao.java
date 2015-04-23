package com.github.rapid.common.redis;

import java.util.List;

import com.github.rapid.common.redis.RedisCallback;
import com.github.rapid.common.redis.RedisDaoSupport;
import com.github.rapid.common.redis.RedisTransactionCallback;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;


public class UdbRedisDao extends RedisDaoSupport {
	
	// 单条操作
	public void setUser(String username,String pwd) {
		getRedisTemplate().set("pwd_"+username, pwd);
	}
	
	// 通过getRedisTemplate() 执行单条Redis操作
	public void setUser(final String username,final String pwd,final int age) {
		getRedisTemplate().execute(new RedisCallback<Object>() {
			public Object doInRedis(Jedis jedis) {
				jedis.set(username, pwd);
				jedis.hset(username, "age", ""+age);
				return null;
			}
		});
	}
	
	// 通过RedisTransactionCallback 批量操作redis
	public void delUser(final List<String> userList) {
		getRedisTemplate().execute(new RedisTransactionCallback<Object>() {
			public Object doInTransaction(Transaction tran) {
				for(String key : userList) {
					tran.del(key);
				}
				tran.exec();
				return null;
			}
		});
	}
	
}
