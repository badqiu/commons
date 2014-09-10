package com.duowan.common.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Client;
import redis.clients.jedis.DebugParams;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisMonitor;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.PipelineBlock;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.TransactionBlock;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.util.Slowlog;

/**
 * Redis模板类,用于单条操作
 * @author badqiu
 *
 */
public class RedisTemplate implements InitializingBean {
	protected static final Logger log = LoggerFactory.getLogger(JedisPoolFactoryBean.class);
	
	private JedisPool jedisPool;
	
	private Jedis proxy;
	
	private static class JedisProxy extends Jedis {
		public JedisProxy() {
			super("create_by_proxy_host");
		}
	}
	
	public RedisTemplate(){
	}
	
	public RedisTemplate(JedisPool jedisPool) {
		super();
		setJedisPool(jedisPool);
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		Assert.notNull(jedisPool,"jedisPool must be not null");
		this.jedisPool = jedisPool;
		proxy = ProxyUtil.createProxy(new JedisProxy(), new JedisInteceptor(jedisPool));
	}

	public <T> T execute(RedisCallback<T> callback) {
		return execute(jedisPool,callback);
	}

	public static <T> T execute(JedisPool jedisPool,RedisCallback<T> callback) {
		Jedis jedis = jedisPool.getResource();
		try {
			return callback.doInRedis(jedis);
		}catch(Exception e) {
			jedisPool.returnBrokenResource(jedis);
			throw new RuntimeException("redis error",e);
		}finally {
			jedisPool.returnResource(jedis);
		}
	}
	
	public <T> T execute(RedisTransactionCallback<T> callback) {
		return execute(jedisPool,callback);
	}
	
	public static <T> T execute(JedisPool jedisPool,RedisTransactionCallback<T> callback) {
		Jedis jedis = jedisPool.getResource();
		try {
			Transaction tran = jedis.multi();
			T object = callback.doInTransaction(tran);
			return object;
		}catch(Exception e) {
			jedisPool.returnBrokenResource(jedis);
			throw new RuntimeException("redis error",e);
		}finally {
			jedisPool.returnResource(jedis);
		}
	}
	
	public String get(String key) {
		return proxy.get(key);
	}

	public byte[] get(byte[] key) {
		return proxy.get(key);
	}

	public Boolean exists(String key) {
		return proxy.exists(key);
	}

	public Boolean exists(byte[] key) {
		return proxy.exists(key);
	}

	public Long del(String... keys) {
		return proxy.del(keys);
	}

	public Long del(byte[]... keys) {
		return proxy.del(keys);
	}

	public String flushDB() {
		return proxy.flushDB();
	}

	public Long expire(String key, int seconds) {
		return proxy.expire(key, seconds);
	}

	public Long dbSize() {
		return proxy.dbSize();
	}

	public Long expire(byte[] key, int seconds) {
		return proxy.expire(key, seconds);
	}

	public Long expireAt(String key, long unixTime) {
		return proxy.expireAt(key, unixTime);
	}

	public Long expireAt(byte[] key, long unixTime) {
		return proxy.expireAt(key, unixTime);
	}

	public String flushAll() {
		return proxy.flushAll();
	}

	public String getSet(String key, String value) {
		return proxy.getSet(key, value);
	}

	public byte[] getSet(byte[] key, byte[] value) {
		return proxy.getSet(key, value);
	}

	public Long decrBy(String key, long integer) {
		return proxy.decrBy(key, integer);
	}

	public Long decrBy(byte[] key, long integer) {
		return proxy.decrBy(key, integer);
	}

	public Long decr(String key) {
		return proxy.decr(key);
	}

	public Long decr(byte[] key) {
		return proxy.decr(key);
	}

	public Long incrBy(String key, long integer) {
		return proxy.incrBy(key, integer);
	}

	public Long incrBy(byte[] key, long integer) {
		return proxy.incrBy(key, integer);
	}

	public Long incr(String key) {
		return proxy.incr(key);
	}

	public Long incr(byte[] key) {
		return proxy.incr(key);
	}

	public Long append(String key, String value) {
		return proxy.append(key, value);
	}

	public Long append(byte[] key, byte[] value) {
		return proxy.append(key, value);
	}

	public Long hset(String key, String field, String value) {
		return proxy.hset(key, field, value);
	}

	public String hget(String key, String field) {
		return proxy.hget(key, field);
	}

	public Long hset(byte[] key, byte[] field, byte[] value) {
		return proxy.hset(key, field, value);
	}

	public Long hsetnx(String key, String field, String value) {
		return proxy.hsetnx(key, field, value);
	}

	public byte[] hget(byte[] key, byte[] field) {
		return proxy.hget(key, field);
	}

	public String hmset(String key, Map<String, String> hash) {
		return proxy.hmset(key, hash);
	}

	public Long hsetnx(byte[] key, byte[] field, byte[] value) {
		return proxy.hsetnx(key, field, value);
	}

	public List<String> hmget(String key, String... fields) {
		return proxy.hmget(key, fields);
	}

	public String hmset(byte[] key, Map<byte[], byte[]> hash) {
		return proxy.hmset(key, hash);
	}

	public Long hincrBy(String key, String field, long value) {
		return proxy.hincrBy(key, field, value);
	}

	public List<byte[]> hmget(byte[] key, byte[]... fields) {
		return proxy.hmget(key, fields);
	}

	public Long hincrBy(byte[] key, byte[] field, long value) {
		return proxy.hincrBy(key, field, value);
	}

	public Boolean hexists(String key, String field) {
		return proxy.hexists(key, field);
	}

	public Long hdel(String key, String... fields) {
		return proxy.hdel(key, fields);
	}

	public Boolean hexists(byte[] key, byte[] field) {
		return proxy.hexists(key, field);
	}

	public Long hlen(String key) {
		return proxy.hlen(key);
	}

	public Long hdel(byte[] key, byte[]... fields) {
		return proxy.hdel(key, fields);
	}

	public Set<String> hkeys(String key) {
		return proxy.hkeys(key);
	}

	public Long hlen(byte[] key) {
		return proxy.hlen(key);
	}

	public List<String> hvals(String key) {
		return proxy.hvals(key);
	}

	public Set<byte[]> hkeys(byte[] key) {
		return proxy.hkeys(key);
	}

	public Map<String, String> hgetAll(String key) {
		return proxy.hgetAll(key);
	}

	public List<byte[]> hvals(byte[] key) {
		return proxy.hvals(key);
	}

	public Map<byte[], byte[]> hgetAll(byte[] key) {
		return proxy.hgetAll(key);
	}

	public void connect() {
		proxy.connect();
	}

	public void disconnect() {
		proxy.disconnect();
	}

	public List<String> blpop(int timeout, String... keys) {
		return proxy.blpop(timeout, keys);
	}

	public List<byte[]> blpop(int timeout, byte[]... keys) {
		return proxy.blpop(timeout, keys);
	}

	public List<String> brpop(int timeout, String... keys) {
		return proxy.brpop(timeout, keys);
	}

	public List<byte[]> brpop(int timeout, byte[]... keys) {
		return proxy.brpop(timeout, keys);
	}

	public String echo(String string) {
		return proxy.echo(string);
	}

	public String brpoplpush(String source, String destination, int timeout) {
		return proxy.brpoplpush(source, destination, timeout);
	}

	public Boolean getbit(String key, long offset) {
		return proxy.getbit(key, offset);
	}

	public String getrange(String key, long startOffset, long endOffset) {
		return proxy.getrange(key, startOffset, endOffset);
	}

	public List<String> configGet(String pattern) {
		return proxy.configGet(pattern);
	}

	public String bgsave() {
		return proxy.bgsave();
	}

	public String bgrewriteaof() {
		return proxy.bgrewriteaof();
	}

	public String configSet(String parameter, String value) {
		return proxy.configSet(parameter, value);
	}

	public Object eval(String script, int keyCount, String... params) {
		return proxy.eval(script, keyCount, params);
	}

	public Object eval(String script, List<String> keys, List<String> args) {
		return proxy.eval(script, keys, args);
	}

	public Object eval(String script) {
		return proxy.eval(script);
	}

	public String info() {
		return proxy.info();
	}

	public Object evalsha(String script) {
		return proxy.evalsha(script);
	}

	public Object evalsha(String sha1, List<String> keys, List<String> args) {
		return proxy.evalsha(sha1, keys, args);
	}

	public Object evalsha(String sha1, int keyCount, String... params) {
		return proxy.evalsha(sha1, keyCount, params);
	}

	public List<byte[]> configGet(byte[] pattern) {
		return proxy.configGet(pattern);
	}

	public String configResetStat() {
		return proxy.configResetStat();
	}

	public byte[] configSet(byte[] parameter, byte[] value) {
		return proxy.configSet(parameter, value);
	}

	public byte[] echo(byte[] string) {
		return proxy.echo(string);
	}

	public String debug(DebugParams params) {
		return proxy.debug(params);
	}

	public Client getClient() {
		return proxy.getClient();
	}

	public byte[] brpoplpush(byte[] source, byte[] destination, int timeout) {
		return proxy.brpoplpush(source, destination, timeout);
	}

	public Boolean getbit(byte[] key, long offset) {
		return proxy.getbit(key, offset);
	}

	public String getrange(byte[] key, long startOffset, long endOffset) {
		return proxy.getrange(key, startOffset, endOffset);
	}

	public Long getDB() {
		return proxy.getDB();
	}

	public Object eval(byte[] script, List<byte[]> keys, List<byte[]> args) {
		return proxy.eval(script, keys, args);
	}

	public Object eval(byte[] script, byte[] keyCount, byte[][] params) {
		return proxy.eval(script, keyCount, params);
	}
	
	

	public String ping() {
		return proxy.ping();
	}

	public String set(String key, String value) {
		return proxy.set(key, value);
	}

	public String set(byte[] key, byte[] value) {
		return proxy.set(key, value);
	}

	public String quit() {
		return proxy.quit();
	}

	public String type(String key) {
		return proxy.type(key);
	}

	public String type(byte[] key) {
		return proxy.type(key);
	}

	public Set<String> keys(String pattern) {
		return proxy.keys(pattern);
	}

	public Set<byte[]> keys(byte[] pattern) {
		return proxy.keys(pattern);
	}

	public String randomKey() {
		return proxy.randomKey();
	}

	public String rename(String oldkey, String newkey) {
		return proxy.rename(oldkey, newkey);
	}

	public byte[] randomBinaryKey() {
		return proxy.randomBinaryKey();
	}

	public String rename(byte[] oldkey, byte[] newkey) {
		return proxy.rename(oldkey, newkey);
	}

	public Long renamenx(String oldkey, String newkey) {
		return proxy.renamenx(oldkey, newkey);
	}

	public Long renamenx(byte[] oldkey, byte[] newkey) {
		return proxy.renamenx(oldkey, newkey);
	}

	public Long ttl(String key) {
		return proxy.ttl(key);
	}

	public String select(int index) {
		return proxy.select(index);
	}

	public Long ttl(byte[] key) {
		return proxy.ttl(key);
	}

	public Long move(String key, int dbIndex) {
		return proxy.move(key, dbIndex);
	}

	public Long move(byte[] key, int dbIndex) {
		return proxy.move(key, dbIndex);
	}

	public List<String> mget(String... keys) {
		return proxy.mget(keys);
	}

	public Long setnx(String key, String value) {
		return proxy.setnx(key, value);
	}

	public List<byte[]> mget(byte[]... keys) {
		return proxy.mget(keys);
	}

	public String setex(String key, int seconds, String value) {
		return proxy.setex(key, seconds, value);
	}

	public Long setnx(byte[] key, byte[] value) {
		return proxy.setnx(key, value);
	}

	public String mset(String... keysvalues) {
		return proxy.mset(keysvalues);
	}

	public String setex(byte[] key, int seconds, byte[] value) {
		return proxy.setex(key, seconds, value);
	}

	public String mset(byte[]... keysvalues) {
		return proxy.mset(keysvalues);
	}

	public Long msetnx(String... keysvalues) {
		return proxy.msetnx(keysvalues);
	}

	public Long msetnx(byte[]... keysvalues) {
		return proxy.msetnx(keysvalues);
	}

	public String substr(String key, int start, int end) {
		return proxy.substr(key, start, end);
	}

	public byte[] substr(byte[] key, int start, int end) {
		return proxy.substr(key, start, end);
	}

	public Long rpush(String key, String... strings) {
		return proxy.rpush(key, strings);
	}

	public Long lpush(String key, String... strings) {
		return proxy.lpush(key, strings);
	}

	public Long rpush(byte[] key, byte[]... strings) {
		return proxy.rpush(key, strings);
	}

	public Long llen(String key) {
		return proxy.llen(key);
	}

	public Long lpush(byte[] key, byte[]... strings) {
		return proxy.lpush(key, strings);
	}

	public List<String> lrange(String key, long start, long end) {
		return proxy.lrange(key, start, end);
	}

	public Long llen(byte[] key) {
		return proxy.llen(key);
	}

	public List<byte[]> lrange(byte[] key, int start, int end) {
		return proxy.lrange(key, start, end);
	}

	public String ltrim(String key, long start, long end) {
		return proxy.ltrim(key, start, end);
	}

	public String ltrim(byte[] key, int start, int end) {
		return proxy.ltrim(key, start, end);
	}

	public String lindex(String key, long index) {
		return proxy.lindex(key, index);
	}

	public String lset(String key, long index, String value) {
		return proxy.lset(key, index, value);
	}

	public byte[] lindex(byte[] key, int index) {
		return proxy.lindex(key, index);
	}

	public Long lrem(String key, long count, String value) {
		return proxy.lrem(key, count, value);
	}

	public String lset(byte[] key, int index, byte[] value) {
		return proxy.lset(key, index, value);
	}

	public Long lrem(byte[] key, int count, byte[] value) {
		return proxy.lrem(key, count, value);
	}

	public String lpop(String key) {
		return proxy.lpop(key);
	}

	public String rpop(String key) {
		return proxy.rpop(key);
	}

	public byte[] lpop(byte[] key) {
		return proxy.lpop(key);
	}

	public String rpoplpush(String srckey, String dstkey) {
		return proxy.rpoplpush(srckey, dstkey);
	}

	public byte[] rpop(byte[] key) {
		return proxy.rpop(key);
	}

	public byte[] rpoplpush(byte[] srckey, byte[] dstkey) {
		return proxy.rpoplpush(srckey, dstkey);
	}

	public Long sadd(String key, String... members) {
		return proxy.sadd(key, members);
	}

	public Set<String> smembers(String key) {
		return proxy.smembers(key);
	}

	public Long sadd(byte[] key, byte[]... members) {
		return proxy.sadd(key, members);
	}

	public Long srem(String key, String... members) {
		return proxy.srem(key, members);
	}

	public Set<byte[]> smembers(byte[] key) {
		return proxy.smembers(key);
	}

	public String spop(String key) {
		return proxy.spop(key);
	}

	public Long srem(byte[] key, byte[]... member) {
		return proxy.srem(key, member);
	}

	public Long smove(String srckey, String dstkey, String member) {
		return proxy.smove(srckey, dstkey, member);
	}

	public byte[] spop(byte[] key) {
		return proxy.spop(key);
	}

	public Long smove(byte[] srckey, byte[] dstkey, byte[] member) {
		return proxy.smove(srckey, dstkey, member);
	}

	public Long scard(String key) {
		return proxy.scard(key);
	}

	public Boolean sismember(String key, String member) {
		return proxy.sismember(key, member);
	}

	public Set<String> sinter(String... keys) {
		return proxy.sinter(keys);
	}

	public Long scard(byte[] key) {
		return proxy.scard(key);
	}

	public Boolean sismember(byte[] key, byte[] member) {
		return proxy.sismember(key, member);
	}

	public Set<byte[]> sinter(byte[]... keys) {
		return proxy.sinter(keys);
	}

	public Long sinterstore(String dstkey, String... keys) {
		return proxy.sinterstore(dstkey, keys);
	}

	public Set<String> sunion(String... keys) {
		return proxy.sunion(keys);
	}

	public Long sinterstore(byte[] dstkey, byte[]... keys) {
		return proxy.sinterstore(dstkey, keys);
	}

	public Long sunionstore(String dstkey, String... keys) {
		return proxy.sunionstore(dstkey, keys);
	}

	public Set<byte[]> sunion(byte[]... keys) {
		return proxy.sunion(keys);
	}

	public Set<String> sdiff(String... keys) {
		return proxy.sdiff(keys);
	}

	public Long sunionstore(byte[] dstkey, byte[]... keys) {
		return proxy.sunionstore(dstkey, keys);
	}

	public Long sdiffstore(String dstkey, String... keys) {
		return proxy.sdiffstore(dstkey, keys);
	}

	public Set<byte[]> sdiff(byte[]... keys) {
		return proxy.sdiff(keys);
	}

	public String srandmember(String key) {
		return proxy.srandmember(key);
	}

	public Long zadd(String key, double score, String member) {
		return proxy.zadd(key, score, member);
	}

	public Long sdiffstore(byte[] dstkey, byte[]... keys) {
		return proxy.sdiffstore(dstkey, keys);
	}

	public byte[] srandmember(byte[] key) {
		return proxy.srandmember(key);
	}

	public Long zadd(byte[] key, double score, byte[] member) {
		return proxy.zadd(key, score, member);
	}

	public Long zadd(String key, Map<Double, String> scoreMembers) {
		return proxy.zadd(key, scoreMembers);
	}

	public Set<String> zrange(String key, long start, long end) {
		return proxy.zrange(key, start, end);
	}

	public Long zrem(String key, String... members) {
		return proxy.zrem(key, members);
	}

	public Long zadd(byte[] key, Map<Double, byte[]> scoreMembers) {
		return proxy.zadd(key, scoreMembers);
	}

	public Double zincrby(String key, double score, String member) {
		return proxy.zincrby(key, score, member);
	}

	public Set<byte[]> zrange(byte[] key, int start, int end) {
		return proxy.zrange(key, start, end);
	}

	public Long zrem(byte[] key, byte[]... members) {
		return proxy.zrem(key, members);
	}

	public Double zincrby(byte[] key, double score, byte[] member) {
		return proxy.zincrby(key, score, member);
	}

	public Long zrank(String key, String member) {
		return proxy.zrank(key, member);
	}

	public Long zrevrank(String key, String member) {
		return proxy.zrevrank(key, member);
	}

	public Long zrank(byte[] key, byte[] member) {
		return proxy.zrank(key, member);
	}

	public Set<String> zrevrange(String key, long start, long end) {
		return proxy.zrevrange(key, start, end);
	}

	public Long zrevrank(byte[] key, byte[] member) {
		return proxy.zrevrank(key, member);
	}

	public Set<Tuple> zrangeWithScores(String key, long start, long end) {
		return proxy.zrangeWithScores(key, start, end);
	}

	public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {
		return proxy.zrevrangeWithScores(key, start, end);
	}

	public Long zcard(String key) {
		return proxy.zcard(key);
	}

	public Set<byte[]> zrevrange(byte[] key, int start, int end) {
		return proxy.zrevrange(key, start, end);
	}

	public Double zscore(String key, String member) {
		return proxy.zscore(key, member);
	}

	public Set<Tuple> zrangeWithScores(byte[] key, int start, int end) {
		return proxy.zrangeWithScores(key, start, end);
	}

	public Set<Tuple> zrevrangeWithScores(byte[] key, int start, int end) {
		return proxy.zrevrangeWithScores(key, start, end);
	}

	public String watch(String... keys) {
		return proxy.watch(keys);
	}

	public Long zcard(byte[] key) {
		return proxy.zcard(key);
	}

	public List<String> sort(String key) {
		return proxy.sort(key);
	}

	public Double zscore(byte[] key, byte[] member) {
		return proxy.zscore(key, member);
	}

	public List<String> sort(String key, SortingParams sortingParameters) {
		return proxy.sort(key, sortingParameters);
	}

	public Transaction multi() {
		return proxy.multi();
	}

	public List<Object> multi(TransactionBlock jedisTransaction) {
		return proxy.multi(jedisTransaction);
	}

	public String watch(byte[]... keys) {
		return proxy.watch(keys);
	}

	public String unwatch() {
		return proxy.unwatch();
	}

	public List<byte[]> sort(byte[] key) {
		return proxy.sort(key);
	}

	public List<byte[]> sort(byte[] key, SortingParams sortingParameters) {
		return proxy.sort(key, sortingParameters);
	}

	public Long sort(String key, SortingParams sortingParameters, String dstkey) {
		return proxy.sort(key, sortingParameters, dstkey);
	}

	public Long sort(String key, String dstkey) {
		return proxy.sort(key, dstkey);
	}

	public Long sort(byte[] key, SortingParams sortingParameters, byte[] dstkey) {
		return proxy.sort(key, sortingParameters, dstkey);
	}

	public Long sort(byte[] key, byte[] dstkey) {
		return proxy.sort(key, dstkey);
	}

	public String auth(String password) {
		return proxy.auth(password);
	}

	public void subscribe(JedisPubSub jedisPubSub, String... channels) {
		proxy.subscribe(jedisPubSub, channels);
	}

	public Long publish(String channel, String message) {
		return proxy.publish(channel, message);
	}

	public void psubscribe(JedisPubSub jedisPubSub, String... patterns) {
		proxy.psubscribe(jedisPubSub, patterns);
	}

	public Long zcount(String key, double min, double max) {
		return proxy.zcount(key, min, max);
	}

	public Long zcount(String key, String min, String max) {
		return proxy.zcount(key, min, max);
	}

	public Set<String> zrangeByScore(String key, double min, double max) {
		return proxy.zrangeByScore(key, min, max);
	}

	public List<Object> pipelined(PipelineBlock jedisPipeline) {
		return proxy.pipelined(jedisPipeline);
	}

	public Pipeline pipelined() {
		return proxy.pipelined();
	}

	public Long zcount(byte[] key, double min, double max) {
		return proxy.zcount(key, min, max);
	}

	public Long zcount(byte[] key, byte[] min, byte[] max) {
		return proxy.zcount(key, min, max);
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max) {
		return proxy.zrangeByScore(key, min, max);
	}

	public Set<String> zrangeByScore(String key, String min, String max) {
		return proxy.zrangeByScore(key, min, max);
	}

	public Set<String> zrangeByScore(String key, double min, double max,
			int offset, int count) {
		return proxy.zrangeByScore(key, min, max, offset, count);
	}

	public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max) {
		return proxy.zrangeByScore(key, min, max);
	}

	public Set<byte[]> zrangeByScore(byte[] key, double min, double max,
			int offset, int count) {
		return proxy.zrangeByScore(key, min, max, offset, count);
	}

	public Set<String> zrangeByScore(String key, String min, String max,
			int offset, int count) {
		return proxy.zrangeByScore(key, min, max, offset, count);
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
		return proxy.zrangeByScoreWithScores(key, min, max);
	}

	public Set<byte[]> zrangeByScore(byte[] key, byte[] min, byte[] max,
			int offset, int count) {
		return proxy.zrangeByScore(key, min, max, offset, count);
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min, double max) {
		return proxy.zrangeByScoreWithScores(key, min, max);
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {
		return proxy.zrangeByScoreWithScores(key, min, max);
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, double min,
			double max, int offset, int count) {
		return proxy.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min, byte[] max) {
		return proxy.zrangeByScoreWithScores(key, min, max);
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, double min,
			double max, int offset, int count) {
		return proxy.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	public Set<Tuple> zrangeByScoreWithScores(String key, String min,
			String max, int offset, int count) {
		return proxy.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	public Set<String> zrevrangeByScore(String key, double max, double min) {
		return proxy.zrevrangeByScore(key, max, min);
	}

	public Set<String> zrevrangeByScore(String key, String max, String min) {
		return proxy.zrevrangeByScore(key, max, min);
	}

	public Set<String> zrevrangeByScore(String key, double max, double min,
			int offset, int count) {
		return proxy.zrevrangeByScore(key, max, min, offset, count);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
			double min) {
		return proxy.zrevrangeByScoreWithScores(key, max, min);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, double max,
			double min, int offset, int count) {
		return proxy.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max,
			String min, int offset, int count) {
		return proxy.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	public Set<String> zrevrangeByScore(String key, String max, String min,
			int offset, int count) {
		return proxy.zrevrangeByScore(key, max, min, offset, count);
	}

	public Set<Tuple> zrangeByScoreWithScores(byte[] key, byte[] min,
			byte[] max, int offset, int count) {
		return proxy.zrangeByScoreWithScores(key, min, max, offset, count);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(String key, String max,
			String min) {
		return proxy.zrevrangeByScoreWithScores(key, max, min);
	}

	public Long zremrangeByRank(String key, long start, long end) {
		return proxy.zremrangeByRank(key, start, end);
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min) {
		return proxy.zrevrangeByScore(key, max, min);
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min) {
		return proxy.zrevrangeByScore(key, max, min);
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, double max, double min,
			int offset, int count) {
		return proxy.zrevrangeByScore(key, max, min, offset, count);
	}

	public Long zremrangeByScore(String key, double start, double end) {
		return proxy.zremrangeByScore(key, start, end);
	}

	public Set<byte[]> zrevrangeByScore(byte[] key, byte[] max, byte[] min,
			int offset, int count) {
		return proxy.zrevrangeByScore(key, max, min, offset, count);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max,
			double min) {
		return proxy.zrevrangeByScoreWithScores(key, max, min);
	}

	public Long zremrangeByScore(String key, String start, String end) {
		return proxy.zremrangeByScore(key, start, end);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, double max,
			double min, int offset, int count) {
		return proxy.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	public Long zunionstore(String dstkey, String... sets) {
		return proxy.zunionstore(dstkey, sets);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max,
			byte[] min) {
		return proxy.zrevrangeByScoreWithScores(key, max, min);
	}

	public Set<Tuple> zrevrangeByScoreWithScores(byte[] key, byte[] max,
			byte[] min, int offset, int count) {
		return proxy.zrevrangeByScoreWithScores(key, max, min, offset, count);
	}

	public Long zremrangeByRank(byte[] key, int start, int end) {
		return proxy.zremrangeByRank(key, start, end);
	}

	public Long zremrangeByScore(byte[] key, double start, double end) {
		return proxy.zremrangeByScore(key, start, end);
	}

	public Long zunionstore(String dstkey, ZParams params, String... sets) {
		return proxy.zunionstore(dstkey, params, sets);
	}

	public Long zremrangeByScore(byte[] key, byte[] start, byte[] end) {
		return proxy.zremrangeByScore(key, start, end);
	}

	public Long zunionstore(byte[] dstkey, byte[]... sets) {
		return proxy.zunionstore(dstkey, sets);
	}

	public Long zinterstore(String dstkey, String... sets) {
		return proxy.zinterstore(dstkey, sets);
	}

	public Long zunionstore(byte[] dstkey, ZParams params, byte[]... sets) {
		return proxy.zunionstore(dstkey, params, sets);
	}

	public Long zinterstore(String dstkey, ZParams params, String... sets) {
		return proxy.zinterstore(dstkey, params, sets);
	}

	public Long zinterstore(byte[] dstkey, byte[]... sets) {
		return proxy.zinterstore(dstkey, sets);
	}

	public Long strlen(String key) {
		return proxy.strlen(key);
	}

	public Long lpushx(String key, String string) {
		return proxy.lpushx(key, string);
	}

	public Long zinterstore(byte[] dstkey, ZParams params, byte[]... sets) {
		return proxy.zinterstore(dstkey, params, sets);
	}

	public Long persist(String key) {
		return proxy.persist(key);
	}

	public Long rpushx(String key, String string) {
		return proxy.rpushx(key, string);
	}

	public Long linsert(String key, LIST_POSITION where, String pivot,
			String value) {
		return proxy.linsert(key, where, pivot, value);
	}

	public Boolean setbit(String key, long offset, boolean value) {
		return proxy.setbit(key, offset, value);
	}

	public Long setrange(String key, long offset, String value) {
		return proxy.setrange(key, offset, value);
	}

	public String save() {
		return proxy.save();
	}

	public Long lastsave() {
		return proxy.lastsave();
	}

	public String shutdown() {
		return proxy.shutdown();
	}

	public Boolean scriptExists(String sha1) {
		return proxy.scriptExists(sha1);
	}

	public List<Boolean> scriptExists(String... sha1) {
		return proxy.scriptExists(sha1);
	}

	public String scriptLoad(String script) {
		return proxy.scriptLoad(script);
	}

	public void monitor(JedisMonitor jedisMonitor) {
		proxy.monitor(jedisMonitor);
	}

	public List<Slowlog> slowlogGet() {
		return proxy.slowlogGet();
	}

	public List<Slowlog> slowlogGet(long entries) {
		return proxy.slowlogGet(entries);
	}

	public Long objectRefcount(String string) {
		return proxy.objectRefcount(string);
	}

	public String objectEncoding(String string) {
		return proxy.objectEncoding(string);
	}

	public String slaveof(String host, int port) {
		return proxy.slaveof(host, port);
	}

	public Long objectIdletime(String string) {
		return proxy.objectIdletime(string);
	}

	public String slaveofNoOne() {
		return proxy.slaveofNoOne();
	}

	public boolean isConnected() {
		return proxy.isConnected();
	}

	public Long strlen(byte[] key) {
		return proxy.strlen(key);
	}

	public void sync() {
		proxy.sync();
	}

	public Long lpushx(byte[] key, byte[] string) {
		return proxy.lpushx(key, string);
	}

	public Long persist(byte[] key) {
		return proxy.persist(key);
	}

	public Long rpushx(byte[] key, byte[] string) {
		return proxy.rpushx(key, string);
	}

	public Long linsert(byte[] key, LIST_POSITION where, byte[] pivot,
			byte[] value) {
		return proxy.linsert(key, where, pivot, value);
	}

	public Boolean setbit(byte[] key, long offset, byte[] value) {
		return proxy.setbit(key, offset, value);
	}

	public Long setrange(byte[] key, long offset, byte[] value) {
		return proxy.setrange(key, offset, value);
	}

	public Long publish(byte[] channel, byte[] message) {
		return proxy.publish(channel, message);
	}

	public void subscribe(BinaryJedisPubSub jedisPubSub, byte[]... channels) {
		proxy.subscribe(jedisPubSub, channels);
	}

	public void psubscribe(BinaryJedisPubSub jedisPubSub, byte[]... patterns) {
		proxy.psubscribe(jedisPubSub, patterns);
	}

	public byte[] scriptFlush() {
		return proxy.scriptFlush();
	}

	public List<Long> scriptExists(byte[]... sha1) {
		return proxy.scriptExists(sha1);
	}

	public byte[] scriptLoad(byte[] script) {
		return proxy.scriptLoad(script);
	}

	public byte[] scriptKill() {
		return proxy.scriptKill();
	}

	public byte[] slowlogReset() {
		return proxy.slowlogReset();
	}

	public long slowlogLen() {
		return proxy.slowlogLen();
	}

	public List<byte[]> slowlogGetBinary() {
		return proxy.slowlogGetBinary();
	}

	public List<byte[]> slowlogGetBinary(long entries) {
		return proxy.slowlogGetBinary(entries);
	}

	public Long objectRefcount(byte[] key) {
		return proxy.objectRefcount(key);
	}

	public byte[] objectEncoding(byte[] key) {
		return proxy.objectEncoding(key);
	}

	public Long objectIdletime(byte[] key) {
		return proxy.objectIdletime(key);
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(jedisPool,"jedisPool must be not null");
	}
	
}
