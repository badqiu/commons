package com.github.rapid.common.redis;

import redis.clients.jedis.Transaction;

/**
 * Redis批处理方法的回调接口
 * 需要手动 Transaction.exec()
 * 
 * @author badqiu
 * 
 * @see RedisTemplate
 * @param <T>
 */
public interface RedisTransactionCallback <T> {

	public T doInTransaction(Transaction tran);
	
}
