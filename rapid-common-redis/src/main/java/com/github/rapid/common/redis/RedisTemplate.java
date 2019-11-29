package com.github.rapid.common.redis;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.BitPosParams;
import redis.clients.jedis.Client;
import redis.clients.jedis.ClusterReset;
import redis.clients.jedis.DebugParams;
import redis.clients.jedis.GeoCoordinate;
import redis.clients.jedis.GeoRadiusResponse;
import redis.clients.jedis.GeoUnit;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisMonitor;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolAbstract;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ListPosition;
import redis.clients.jedis.Module;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.StreamEntry;
import redis.clients.jedis.StreamEntryID;
import redis.clients.jedis.StreamPendingEntry;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.ZParams;
import redis.clients.jedis.commands.ProtocolCommand;
import redis.clients.jedis.params.ClientKillParams;
import redis.clients.jedis.params.GeoRadiusParam;
import redis.clients.jedis.params.MigrateParams;
import redis.clients.jedis.params.SetParams;
import redis.clients.jedis.params.ZAddParams;
import redis.clients.jedis.params.ZIncrByParams;
import redis.clients.jedis.util.Slowlog;

/**
 * Redis妯℃澘绫�,鐢ㄤ簬鍗曟潯鎿嶄綔
 * 
 * @author badqiu
 *
 */
public class RedisTemplate implements InitializingBean {
	protected static final Logger log = LoggerFactory
			.getLogger(JedisPoolFactoryBean.class);

	private JedisPool jedisPool;

	private Jedis proxy;

	private static class JedisProxy extends Jedis {
		public JedisProxy() {
			super("create_by_proxy_host");
		}
	}

	public RedisTemplate() {
	}

	public RedisTemplate(JedisPool jedisPool) {
		super();
		setJedisPool(jedisPool);
	}

	public JedisPool getJedisPool() {
		return jedisPool;
	}

	public void setJedisPool(JedisPool jedisPool) {
		Assert.notNull(jedisPool, "jedisPool must be not null");
		this.jedisPool = jedisPool;
		proxy = ProxyUtil.createProxy(new JedisProxy(), new JedisInteceptor(
				jedisPool));
	}

	public <T> T execute(RedisCallback<T> callback) {
		return execute(jedisPool, callback);
	}

	public static <T> T execute(JedisPool jedisPool, RedisCallback<T> callback) {
		Jedis jedis = jedisPool.getResource();
		try {
			T result = callback.doInRedis(jedis);
			return result;
		} finally {
			jedis.close();
		}
	}

	public <T> T execute(RedisTransactionCallback<T> callback) {
		return execute(jedisPool, callback);
	}

	public static <T> T execute(JedisPool jedisPool,
			RedisTransactionCallback<T> callback) {
		Jedis jedis = jedisPool.getResource();
		try {
			Transaction tran = jedis.multi();
			T object = callback.doInTransaction(tran);
			return object;
		} finally {
			jedis.close();
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

	public byte[] getrange(byte[] key, long startOffset, long endOffset) {
		return proxy.getrange(key, startOffset, endOffset);
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

	public Long zadd(String key, Map<String, Double> scoreMembers) {
		return proxy.zadd(key, scoreMembers);
	}

	public Set<String> zrange(String key, long start, long end) {
		return proxy.zrange(key, start, end);
	}

	public Long zrem(String key, String... members) {
		return proxy.zrem(key, members);
	}

	public Long zadd(byte[] key, Map<byte[], Double> scoreMembers) {
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

	public String scriptFlush() {
		return proxy.scriptFlush();
	}

	public List<Long> scriptExists(byte[]... sha1) {
		return proxy.scriptExists(sha1);
	}

	public byte[] scriptLoad(byte[] script) {
		return proxy.scriptLoad(script);
	}

	public String scriptKill() {
		return proxy.scriptKill();
	}

	public String slowlogReset() {
		return proxy.slowlogReset();
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

	public String asking() {
		return proxy.asking();
	}

	public Long bitcount(byte[] key, long start, long end) {
		return proxy.bitcount(key, start, end);
	}

	public Long bitcount(byte[] key) {
		return proxy.bitcount(key);
	}

	public Long bitcount(String key, long start, long end) {
		return proxy.bitcount(key, start, end);
	}

	public Long bitcount(String key) {
		return proxy.bitcount(key);
	}

	public Long bitop(BitOP op, byte[] destKey, byte[]... srcKeys) {
		return proxy.bitop(op, destKey, srcKeys);
	}

	public Long bitop(BitOP op, String destKey, String... srcKeys) {
		return proxy.bitop(op, destKey, srcKeys);
	}

	public Long bitpos(byte[] key, boolean value, BitPosParams params) {
		return proxy.bitpos(key, value, params);
	}

	public Long bitpos(byte[] key, boolean value) {
		return proxy.bitpos(key, value);
	}

	public Long bitpos(String key, boolean value, BitPosParams params) {
		return proxy.bitpos(key, value, params);
	}

	public Long bitpos(String key, boolean value) {
		return proxy.bitpos(key, value);
	}

	public List<byte[]> blpop(byte[]... args) {
		return proxy.blpop(args);
	}

	public List<String> blpop(int timeout, String key) {
		return proxy.blpop(timeout, key);
	}

	public List<String> blpop(String... args) {
		return proxy.blpop(args);
	}

	public List<byte[]> brpop(byte[]... args) {
		return proxy.brpop(args);
	}

	public List<String> brpop(int timeout, String key) {
		return proxy.brpop(timeout, key);
	}

	public List<String> brpop(String... args) {
		return proxy.brpop(args);
	}

	public String clientGetname() {
		return proxy.clientGetname();
	}

	public String clientKill(byte[] client) {
		return proxy.clientKill(client);
	}

	public String clientKill(String client) {
		return proxy.clientKill(client);
	}

	public String clientList() {
		return proxy.clientList();
	}

	public String clientSetname(byte[] name) {
		return proxy.clientSetname(name);
	}

	public String clientSetname(String name) {
		return proxy.clientSetname(name);
	}

	public void close() {
		proxy.close();
	}

	public String clusterAddSlots(int... slots) {
		return proxy.clusterAddSlots(slots);
	}

	public Long clusterCountKeysInSlot(int slot) {
		return proxy.clusterCountKeysInSlot(slot);
	}

	public String clusterDelSlots(int... slots) {
		return proxy.clusterDelSlots(slots);
	}

	public String clusterFailover() {
		return proxy.clusterFailover();
	}

	public String clusterFlushSlots() {
		return proxy.clusterFlushSlots();
	}

	public String clusterForget(String nodeId) {
		return proxy.clusterForget(nodeId);
	}

	public List<String> clusterGetKeysInSlot(int slot, int count) {
		return proxy.clusterGetKeysInSlot(slot, count);
	}

	public String clusterInfo() {
		return proxy.clusterInfo();
	}

	public Long clusterKeySlot(String key) {
		return proxy.clusterKeySlot(key);
	}

	public String clusterMeet(String ip, int port) {
		return proxy.clusterMeet(ip, port);
	}

	public String clusterNodes() {
		return proxy.clusterNodes();
	}

	public String clusterReplicate(String nodeId) {
		return proxy.clusterReplicate(nodeId);
	}

	public String clusterSaveConfig() {
		return proxy.clusterSaveConfig();
	}

	public String clusterSetSlotImporting(int slot, String nodeId) {
		return proxy.clusterSetSlotImporting(slot, nodeId);
	}

	public String clusterSetSlotMigrating(int slot, String nodeId) {
		return proxy.clusterSetSlotMigrating(slot, nodeId);
	}

	public String clusterSetSlotNode(int slot, String nodeId) {
		return proxy.clusterSetSlotNode(slot, nodeId);
	}

	public String clusterSetSlotStable(int slot) {
		return proxy.clusterSetSlotStable(slot);
	}

	public List<String> clusterSlaves(String nodeId) {
		return proxy.clusterSlaves(nodeId);
	}

	public List<Object> clusterSlots() {
		return proxy.clusterSlots();
	}

	public Long del(byte[] key) {
		return proxy.del(key);
	}

	public Long del(String key) {
		return proxy.del(key);
	}

	public byte[] dump(byte[] key) {
		return proxy.dump(key);
	}

	public byte[] dump(String key) {
		return proxy.dump(key);
	}

	public Object eval(byte[] script, int keyCount, byte[]... params) {
		return proxy.eval(script, keyCount, params);
	}

	public Object eval(byte[] script) {
		return proxy.eval(script);
	}

	public Object evalsha(byte[] sha1, int keyCount, byte[]... params) {
		return proxy.evalsha(sha1, keyCount, params);
	}

	public Object evalsha(byte[] sha1, List<byte[]> keys, List<byte[]> args) {
		return proxy.evalsha(sha1, keys, args);
	}

	public Object evalsha(byte[] sha1) {
		return proxy.evalsha(sha1);
	}

	public Double hincrByFloat(byte[] key, byte[] field, double value) {
		return proxy.hincrByFloat(key, field, value);
	}

	public Double hincrByFloat(String key, String field, double value) {
		return proxy.hincrByFloat(key, field, value);
	}

	public ScanResult<Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor,
			ScanParams params) {
		return proxy.hscan(key, cursor, params);
	}

	public ScanResult<Entry<byte[], byte[]>> hscan(byte[] key, byte[] cursor) {
		return proxy.hscan(key, cursor);
	}

	public ScanResult<Entry<String, String>> hscan(String key, String cursor,
			ScanParams params) {
		return proxy.hscan(key, cursor, params);
	}

	public ScanResult<Entry<String, String>> hscan(String key, String cursor) {
		return proxy.hscan(key, cursor);
	}

	public Double incrByFloat(byte[] key, double integer) {
		return proxy.incrByFloat(key, integer);
	}

	public Double incrByFloat(String key, double value) {
		return proxy.incrByFloat(key, value);
	}

	public String info(String section) {
		return proxy.info(section);
	}

	public byte[] lindex(byte[] key, long index) {
		return proxy.lindex(key, index);
	}

	public Long lpushx(byte[] key, byte[]... string) {
		return proxy.lpushx(key, string);
	}

	public Long lpushx(String key, String... string) {
		return proxy.lpushx(key, string);
	}

	public List<byte[]> lrange(byte[] key, long start, long end) {
		return proxy.lrange(key, start, end);
	}

	public Long lrem(byte[] key, long count, byte[] value) {
		return proxy.lrem(key, count, value);
	}

	public String lset(byte[] key, long index, byte[] value) {
		return proxy.lset(key, index, value);
	}

	public String ltrim(byte[] key, long start, long end) {
		return proxy.ltrim(key, start, end);
	}

	public String migrate(String host, int port, String key, int destinationDb,
			int timeout) {
		return proxy.migrate(host, port, key, destinationDb, timeout);
	}

	public Long pexpire(byte[] key, long milliseconds) {
		return proxy.pexpire(key, milliseconds);
	}

	public Long pexpire(String key, long milliseconds) {
		return proxy.pexpire(key, milliseconds);
	}

	public Long pexpireAt(byte[] key, long millisecondsTimestamp) {
		return proxy.pexpireAt(key, millisecondsTimestamp);
	}

	public Long pexpireAt(String key, long millisecondsTimestamp) {
		return proxy.pexpireAt(key, millisecondsTimestamp);
	}

	public Long pfadd(byte[] key, byte[]... elements) {
		return proxy.pfadd(key, elements);
	}

	public Long pfadd(String key, String... elements) {
		return proxy.pfadd(key, elements);
	}

	public Long pfcount(byte[]... keys) {
		return proxy.pfcount(keys);
	}

	public long pfcount(byte[] key) {
		return proxy.pfcount(key);
	}

	public long pfcount(String... keys) {
		return proxy.pfcount(keys);
	}

	public long pfcount(String key) {
		return proxy.pfcount(key);
	}

	public String pfmerge(byte[] destkey, byte[]... sourcekeys) {
		return proxy.pfmerge(destkey, sourcekeys);
	}

	public String pfmerge(String destkey, String... sourcekeys) {
		return proxy.pfmerge(destkey, sourcekeys);
	}

	public String psetex(byte[] key, long milliseconds, byte[] value) {
		return proxy.psetex(key, milliseconds, value);
	}

	public String psetex(String key, long milliseconds, String value) {
		return proxy.psetex(key, milliseconds, value);
	}

	public Long pttl(byte[] key) {
		return proxy.pttl(key);
	}

	public Long pttl(String key) {
		return proxy.pttl(key);
	}

	public List<String> pubsubChannels(String pattern) {
		return proxy.pubsubChannels(pattern);
	}

	public Long pubsubNumPat() {
		return proxy.pubsubNumPat();
	}

	public Map<String, String> pubsubNumSub(String... channels) {
		return proxy.pubsubNumSub(channels);
	}

	public void resetState() {
		proxy.resetState();
	}

	public String restore(byte[] key, int ttl, byte[] serializedValue) {
		return proxy.restore(key, ttl, serializedValue);
	}

	public String restore(String key, int ttl, byte[] serializedValue) {
		return proxy.restore(key, ttl, serializedValue);
	}

	public Long rpushx(byte[] key, byte[]... string) {
		return proxy.rpushx(key, string);
	}

	public Long rpushx(String key, String... string) {
		return proxy.rpushx(key, string);
	}

	public ScanResult<byte[]> scan(byte[] cursor, ScanParams params) {
		return proxy.scan(cursor, params);
	}

	public ScanResult<byte[]> scan(byte[] cursor) {
		return proxy.scan(cursor);
	}

	public ScanResult<String> scan(String arg0, ScanParams arg1) {
		return proxy.scan(arg0, arg1);
	}

	public ScanResult<String> scan(String cursor) {
		return proxy.scan(cursor);
	}

	public String sentinelFailover(String masterName) {
		return proxy.sentinelFailover(masterName);
	}

	public List<String> sentinelGetMasterAddrByName(String masterName) {
		return proxy.sentinelGetMasterAddrByName(masterName);
	}

	public List<Map<String, String>> sentinelMasters() {
		return proxy.sentinelMasters();
	}

	public String sentinelMonitor(String masterName, String ip, int port,
			int quorum) {
		return proxy.sentinelMonitor(masterName, ip, port, quorum);
	}

	public String sentinelRemove(String masterName) {
		return proxy.sentinelRemove(masterName);
	}

	public Long sentinelReset(String pattern) {
		return proxy.sentinelReset(pattern);
	}

	public String sentinelSet(String arg0, Map<String, String> arg1) {
		return proxy.sentinelSet(arg0, arg1);
	}

	public List<Map<String, String>> sentinelSlaves(String arg0) {
		return proxy.sentinelSlaves(arg0);
	}

	public Boolean setbit(byte[] key, long offset, boolean value) {
		return proxy.setbit(key, offset, value);
	}

	public Boolean setbit(String key, long offset, String value) {
		return proxy.setbit(key, offset, value);
	}

	public Set<byte[]> spop(byte[] key, long count) {
		return proxy.spop(key, count);
	}

	public Set<String> spop(String key, long count) {
		return proxy.spop(key, count);
	}

	public List<byte[]> srandmember(byte[] key, int count) {
		return proxy.srandmember(key, count);
	}

	public List<String> srandmember(String key, int count) {
		return proxy.srandmember(key, count);
	}

	public ScanResult<byte[]> sscan(byte[] key, byte[] cursor, ScanParams params) {
		return proxy.sscan(key, cursor, params);
	}

	public ScanResult<byte[]> sscan(byte[] key, byte[] cursor) {
		return proxy.sscan(key, cursor);
	}

	public ScanResult<String> sscan(String arg0, String arg1, ScanParams arg2) {
		return proxy.sscan(arg0, arg1, arg2);
	}

	public ScanResult<String> sscan(String key, String cursor) {
		return proxy.sscan(key, cursor);
	}

	public List<String> time() {
		return proxy.time();
	}

	public Long waitReplicas(int replicas, long timeout) {
		return proxy.waitReplicas(replicas, timeout);
	}

	public Long zlexcount(byte[] key, byte[] min, byte[] max) {
		return proxy.zlexcount(key, min, max);
	}

	public Long zlexcount(String key, String min, String max) {
		return proxy.zlexcount(key, min, max);
	}

	public Set<byte[]> zrange(byte[] key, long start, long end) {
		return proxy.zrange(key, start, end);
	}

	public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max,
			int offset, int count) {
		return proxy.zrangeByLex(key, min, max, offset, count);
	}

	public Set<byte[]> zrangeByLex(byte[] key, byte[] min, byte[] max) {
		return proxy.zrangeByLex(key, min, max);
	}

	public Set<String> zrangeByLex(String key, String min, String max,
			int offset, int count) {
		return proxy.zrangeByLex(key, min, max, offset, count);
	}

	public Set<String> zrangeByLex(String key, String min, String max) {
		return proxy.zrangeByLex(key, min, max);
	}

	public Set<Tuple> zrangeWithScores(byte[] key, long start, long end) {
		return proxy.zrangeWithScores(key, start, end);
	}

	public Long zremrangeByLex(byte[] key, byte[] min, byte[] max) {
		return proxy.zremrangeByLex(key, min, max);
	}

	public Long zremrangeByLex(String key, String min, String max) {
		return proxy.zremrangeByLex(key, min, max);
	}

	public Long zremrangeByRank(byte[] key, long start, long end) {
		return proxy.zremrangeByRank(key, start, end);
	}

	public Set<byte[]> zrevrange(byte[] key, long start, long end) {
		return proxy.zrevrange(key, start, end);
	}

	public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min,
			int offset, int count) {
		return proxy.zrevrangeByLex(key, max, min, offset, count);
	}

	public Set<byte[]> zrevrangeByLex(byte[] key, byte[] max, byte[] min) {
		return proxy.zrevrangeByLex(key, max, min);
	}

	public Set<String> zrevrangeByLex(String key, String max, String min,
			int offset, int count) {
		return proxy.zrevrangeByLex(key, max, min, offset, count);
	}

	public Set<String> zrevrangeByLex(String key, String max, String min) {
		return proxy.zrevrangeByLex(key, max, min);
	}

	public Set<Tuple> zrevrangeWithScores(byte[] key, long start, long end) {
		return proxy.zrevrangeWithScores(key, start, end);
	}

	public ScanResult<Tuple> zscan(byte[] key, byte[] cursor, ScanParams params) {
		return proxy.zscan(key, cursor, params);
	}

	public ScanResult<Tuple> zscan(byte[] key, byte[] cursor) {
		return proxy.zscan(key, cursor);
	}

	public ScanResult<Tuple> zscan(String key, String cursor, ScanParams params) {
		return proxy.zscan(key, cursor, params);
	}

	public ScanResult<Tuple> zscan(String key, String cursor) {
		return proxy.zscan(key, cursor);
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(jedisPool, "jedisPool must be not null");
	}

	public List<Long> bitfield(byte[] key, byte[]... arguments) {
		return proxy.bitfield(key, arguments);
	}

	public List<Long> bitfield(String key, String... arguments) {
		return proxy.bitfield(key, arguments);
	}

	public boolean equals(Object obj) {
		return proxy.equals(obj);
	}

	public Long exists(byte[]... keys) {
		return proxy.exists(keys);
	}

	public Long exists(String... keys) {
		return proxy.exists(keys);
	}

	public Long geoadd(byte[] key, double longitude, double latitude,
			byte[] member) {
		return proxy.geoadd(key, longitude, latitude, member);
	}

	public Long geoadd(byte[] key,
			Map<byte[], GeoCoordinate> memberCoordinateMap) {
		return proxy.geoadd(key, memberCoordinateMap);
	}

	public Long geoadd(String key, double longitude, double latitude,
			String member) {
		return proxy.geoadd(key, longitude, latitude, member);
	}

	public Long geoadd(String key,
			Map<String, GeoCoordinate> memberCoordinateMap) {
		return proxy.geoadd(key, memberCoordinateMap);
	}

	public Double geodist(byte[] key, byte[] member1, byte[] member2,
			GeoUnit unit) {
		return proxy.geodist(key, member1, member2, unit);
	}

	public Double geodist(byte[] key, byte[] member1, byte[] member2) {
		return proxy.geodist(key, member1, member2);
	}

	public Double geodist(String key, String member1, String member2,
			GeoUnit unit) {
		return proxy.geodist(key, member1, member2, unit);
	}

	public Double geodist(String key, String member1, String member2) {
		return proxy.geodist(key, member1, member2);
	}

	public List<byte[]> geohash(byte[] key, byte[]... members) {
		return proxy.geohash(key, members);
	}

	public List<String> geohash(String key, String... members) {
		return proxy.geohash(key, members);
	}

	public List<GeoCoordinate> geopos(byte[] key, byte[]... members) {
		return proxy.geopos(key, members);
	}

	public List<GeoCoordinate> geopos(String key, String... members) {
		return proxy.geopos(key, members);
	}

	public List<GeoRadiusResponse> georadius(byte[] key, double longitude,
			double latitude, double radius, GeoUnit unit) {
		return proxy.georadius(key, longitude, latitude, radius, unit);
	}

	public List<GeoRadiusResponse> georadius(String key, double longitude,
			double latitude, double radius, GeoUnit unit) {
		return proxy.georadius(key, longitude, latitude, radius, unit);
	}

	public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member,
			double radius, GeoUnit unit) {
		return proxy.georadiusByMember(key, member, radius, unit);
	}

	public List<GeoRadiusResponse> georadiusByMember(String key, String member,
			double radius, GeoUnit unit) {
		return proxy.georadiusByMember(key, member, radius, unit);
	}

	public int hashCode() {
		return proxy.hashCode();
	}

	public String readonly() {
		return proxy.readonly();
	}

	public Long scriptExists(byte[] sha1) {
		return proxy.scriptExists(sha1);
	}

	public Long slowlogLen() {
		return proxy.slowlogLen();
	}

	public String ping(String message) {
		return proxy.ping(message);
	}

	public String set(String key, String value, SetParams params) {
		return proxy.set(key, value, params);
	}

	public byte[] ping(byte[] message) {
		return proxy.ping(message);
	}

	public Long unlink(String... keys) {
		return proxy.unlink(keys);
	}

	public String set(byte[] key, byte[] value, SetParams params) {
		return proxy.set(key, value, params);
	}

	public Long unlink(String key) {
		return proxy.unlink(key);
	}

	public String toString() {
		return proxy.toString();
	}

	public Long unlink(byte[]... keys) {
		return proxy.unlink(keys);
	}

	public Long unlink(byte[] key) {
		return proxy.unlink(key);
	}

	public Long touch(String... keys) {
		return proxy.touch(keys);
	}

	public Long touch(String key) {
		return proxy.touch(key);
	}

	public Long touch(byte[]... keys) {
		return proxy.touch(keys);
	}

	public Long touch(byte[] key) {
		return proxy.touch(key);
	}

	public String swapDB(int index1, int index2) {
		return proxy.swapDB(index1, index2);
	}

	public Long hset(String key, Map<String, String> hash) {
		return proxy.hset(key, hash);
	}

	public Long hset(byte[] key, Map<byte[], byte[]> hash) {
		return proxy.hset(key, hash);
	}

	public Long zadd(String key, double score, String member, ZAddParams params) {
		return proxy.zadd(key, score, member, params);
	}

	public Long zadd(String key, Map<String, Double> scoreMembers,
			ZAddParams params) {
		return proxy.zadd(key, scoreMembers, params);
	}

	public Double zincrby(String key, double increment, String member,
			ZIncrByParams params) {
		return proxy.zincrby(key, increment, member, params);
	}

	public Long zadd(byte[] key, double score, byte[] member, ZAddParams params) {
		return proxy.zadd(key, score, member, params);
	}

	public Long zadd(byte[] key, Map<byte[], Double> scoreMembers,
			ZAddParams params) {
		return proxy.zadd(key, scoreMembers, params);
	}

	public Double zincrby(byte[] key, double increment, byte[] member,
			ZIncrByParams params) {
		return proxy.zincrby(key, increment, member, params);
	}

	public Long linsert(String key, ListPosition where, String pivot,
			String value) {
		return proxy.linsert(key, where, pivot, value);
	}

	public List<Slowlog> slowlogGet() {
		return proxy.slowlogGet();
	}

	public List<Slowlog> slowlogGet(long entries) {
		return proxy.slowlogGet(entries);
	}

	public String restoreReplace(String key, int ttl, byte[] serializedValue) {
		return proxy.restoreReplace(key, ttl, serializedValue);
	}

	public String configRewrite() {
		return proxy.configRewrite();
	}

	public String migrate(String host, int port, int destinationDB,
			int timeout, MigrateParams params, String... keys) {
		return proxy.migrate(host, port, destinationDB, timeout, params, keys);
	}

	public Long linsert(byte[] key, ListPosition where, byte[] pivot,
			byte[] value) {
		return proxy.linsert(key, where, pivot, value);
	}

	public String clusterReset(ClusterReset resetType) {
		return proxy.clusterReset(resetType);
	}

	public int getDB() {
		return proxy.getDB();
	}

	public void setDataSource(JedisPoolAbstract jedisPool) {
		proxy.setDataSource(jedisPool);
	}

	public String restoreReplace(byte[] key, int ttl, byte[] serializedValue) {
		return proxy.restoreReplace(key, ttl, serializedValue);
	}

	public List<GeoRadiusResponse> georadiusReadonly(String key,
			double longitude, double latitude, double radius, GeoUnit unit) {
		return proxy.georadiusReadonly(key, longitude, latitude, radius, unit);
	}

	public List<GeoRadiusResponse> georadius(String key, double longitude,
			double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
		return proxy.georadius(key, longitude, latitude, radius, unit, param);
	}

	public List<GeoRadiusResponse> georadiusReadonly(String key,
			double longitude, double latitude, double radius, GeoUnit unit,
			GeoRadiusParam param) {
		return proxy.georadiusReadonly(key, longitude, latitude, radius, unit,
				param);
	}

	public List<GeoRadiusResponse> georadiusByMemberReadonly(String key,
			String member, double radius, GeoUnit unit) {
		return proxy.georadiusByMemberReadonly(key, member, radius, unit);
	}

	public List<GeoRadiusResponse> georadiusByMember(String key, String member,
			double radius, GeoUnit unit, GeoRadiusParam param) {
		return proxy.georadiusByMember(key, member, radius, unit, param);
	}

	public byte[] memoryDoctorBinary() {
		return proxy.memoryDoctorBinary();
	}

	public List<GeoRadiusResponse> georadiusByMemberReadonly(String key,
			String member, double radius, GeoUnit unit, GeoRadiusParam param) {
		return proxy
				.georadiusByMemberReadonly(key, member, radius, unit, param);
	}

	public String clientKill(String ip, int port) {
		return proxy.clientKill(ip, port);
	}

	public String moduleLoad(String path) {
		return proxy.moduleLoad(path);
	}

	public Long clientKill(ClientKillParams params) {
		return proxy.clientKill(params);
	}

	public String moduleUnload(String name) {
		return proxy.moduleUnload(name);
	}

	public byte[] clientGetnameBinary() {
		return proxy.clientGetnameBinary();
	}

	public List<Module> moduleList() {
		return proxy.moduleList();
	}

	public byte[] clientListBinary() {
		return proxy.clientListBinary();
	}

	public Long hstrlen(String key, String field) {
		return proxy.hstrlen(key, field);
	}

	public String clientPause(long timeout) {
		return proxy.clientPause(timeout);
	}

	public String memoryDoctor() {
		return proxy.memoryDoctor();
	}

	public String migrate(String host, int port, byte[] key, int destinationDb,
			int timeout) {
		return proxy.migrate(host, port, key, destinationDb, timeout);
	}

	public StreamEntryID xadd(String key, StreamEntryID id,
			Map<String, String> hash) {
		return proxy.xadd(key, id, hash);
	}

	public StreamEntryID xadd(String key, StreamEntryID id,
			Map<String, String> hash, long maxLen, boolean approximateLength) {
		return proxy.xadd(key, id, hash, maxLen, approximateLength);
	}

	public String migrate(String host, int port, int destinationDB,
			int timeout, MigrateParams params, byte[]... keys) {
		return proxy.migrate(host, port, destinationDB, timeout, params, keys);
	}

	public Long xlen(String key) {
		return proxy.xlen(key);
	}

	public List<StreamEntry> xrange(String key, StreamEntryID start,
			StreamEntryID end, int count) {
		return proxy.xrange(key, start, end, count);
	}

	public List<StreamEntry> xrevrange(String key, StreamEntryID end,
			StreamEntryID start, int count) {
		return proxy.xrevrange(key, end, start, count);
	}

	public List<Entry<String, List<StreamEntry>>> xread(int count, long block,
			Entry<String, StreamEntryID>... streams) {
		return proxy.xread(count, block, streams);
	}

	public long xack(String key, String group, StreamEntryID... ids) {
		return proxy.xack(key, group, ids);
	}

	public String xgroupCreate(String key, String groupname, StreamEntryID id,
			boolean makeStream) {
		return proxy.xgroupCreate(key, groupname, id, makeStream);
	}

	public String xgroupSetID(String key, String groupname, StreamEntryID id) {
		return proxy.xgroupSetID(key, groupname, id);
	}

	public long xgroupDestroy(String key, String groupname) {
		return proxy.xgroupDestroy(key, groupname);
	}

	public String xgroupDelConsumer(String key, String groupname,
			String consumerName) {
		return proxy.xgroupDelConsumer(key, groupname, consumerName);
	}

	public long xdel(String key, StreamEntryID... ids) {
		return proxy.xdel(key, ids);
	}

	public long xtrim(String key, long maxLen, boolean approximateLength) {
		return proxy.xtrim(key, maxLen, approximateLength);
	}

	public List<Entry<String, List<StreamEntry>>> xreadGroup(String groupname,
			String consumer, int count, long block, boolean noAck,
			Entry<String, StreamEntryID>... streams) {
		return proxy.xreadGroup(groupname, consumer, count, block, noAck,
				streams);
	}

	public List<StreamPendingEntry> xpending(String key, String groupname,
			StreamEntryID start, StreamEntryID end, int count,
			String consumername) {
		return proxy.xpending(key, groupname, start, end, count, consumername);
	}

	public List<StreamEntry> xclaim(String key, String group,
			String consumername, long minIdleTime, long newIdleTime,
			int retries, boolean force, StreamEntryID... ids) {
		return proxy.xclaim(key, group, consumername, minIdleTime, newIdleTime,
				retries, force, ids);
	}

	public Object sendCommand(ProtocolCommand cmd, String... args) {
		return proxy.sendCommand(cmd, args);
	}

	public List<GeoRadiusResponse> georadiusReadonly(byte[] key,
			double longitude, double latitude, double radius, GeoUnit unit) {
		return proxy.georadiusReadonly(key, longitude, latitude, radius, unit);
	}

	public List<GeoRadiusResponse> georadius(byte[] key, double longitude,
			double latitude, double radius, GeoUnit unit, GeoRadiusParam param) {
		return proxy.georadius(key, longitude, latitude, radius, unit, param);
	}

	public List<GeoRadiusResponse> georadiusReadonly(byte[] key,
			double longitude, double latitude, double radius, GeoUnit unit,
			GeoRadiusParam param) {
		return proxy.georadiusReadonly(key, longitude, latitude, radius, unit,
				param);
	}

	public List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] key,
			byte[] member, double radius, GeoUnit unit) {
		return proxy.georadiusByMemberReadonly(key, member, radius, unit);
	}

	public List<GeoRadiusResponse> georadiusByMember(byte[] key, byte[] member,
			double radius, GeoUnit unit, GeoRadiusParam param) {
		return proxy.georadiusByMember(key, member, radius, unit, param);
	}

	public List<GeoRadiusResponse> georadiusByMemberReadonly(byte[] key,
			byte[] member, double radius, GeoUnit unit, GeoRadiusParam param) {
		return proxy
				.georadiusByMemberReadonly(key, member, radius, unit, param);
	}

	public Long hstrlen(byte[] key, byte[] field) {
		return proxy.hstrlen(key, field);
	}

	public List<byte[]> xread(int count, long block, Map<byte[], byte[]> streams) {
		return proxy.xread(count, block, streams);
	}

	public List<byte[]> xreadGroup(byte[] groupname, byte[] consumer,
			int count, long block, boolean noAck, Map<byte[], byte[]> streams) {
		return proxy.xreadGroup(groupname, consumer, count, block, noAck,
				streams);
	}

	public byte[] xadd(byte[] key, byte[] id, Map<byte[], byte[]> hash,
			long maxLen, boolean approximateLength) {
		return proxy.xadd(key, id, hash, maxLen, approximateLength);
	}

	public Long xlen(byte[] key) {
		return proxy.xlen(key);
	}

	public List<byte[]> xrange(byte[] key, byte[] start, byte[] end, long count) {
		return proxy.xrange(key, start, end, count);
	}

	public List<byte[]> xrevrange(byte[] key, byte[] end, byte[] start,
			int count) {
		return proxy.xrevrange(key, end, start, count);
	}

	public Long xack(byte[] key, byte[] group, byte[]... ids) {
		return proxy.xack(key, group, ids);
	}

	public String xgroupCreate(byte[] key, byte[] consumer, byte[] id,
			boolean makeStream) {
		return proxy.xgroupCreate(key, consumer, id, makeStream);
	}

	public String xgroupSetID(byte[] key, byte[] consumer, byte[] id) {
		return proxy.xgroupSetID(key, consumer, id);
	}

	public Long xgroupDestroy(byte[] key, byte[] consumer) {
		return proxy.xgroupDestroy(key, consumer);
	}

	public String xgroupDelConsumer(byte[] key, byte[] consumer,
			byte[] consumerName) {
		return proxy.xgroupDelConsumer(key, consumer, consumerName);
	}

	public Long xdel(byte[] key, byte[]... ids) {
		return proxy.xdel(key, ids);
	}

	public Long xtrim(byte[] key, long maxLen, boolean approximateLength) {
		return proxy.xtrim(key, maxLen, approximateLength);
	}

	public List<byte[]> xpending(byte[] key, byte[] groupname, byte[] start,
			byte[] end, int count, byte[] consumername) {
		return proxy.xpending(key, groupname, start, end, count, consumername);
	}

	public List<byte[]> xclaim(byte[] key, byte[] groupname,
			byte[] consumername, long minIdleTime, long newIdleTime,
			int retries, boolean force, byte[][] ids) {
		return proxy.xclaim(key, groupname, consumername, minIdleTime,
				newIdleTime, retries, force, ids);
	}

	public Object sendCommand(ProtocolCommand cmd, byte[]... args) {
		return proxy.sendCommand(cmd, args);
	}

	public Object sendCommand(ProtocolCommand cmd) {
		return proxy.sendCommand(cmd);
	}
	
	

}
