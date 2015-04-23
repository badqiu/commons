package com.github.rapid.common.redis;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;
/**
 * 用于spring生成JedisPool的FactoryBean
 * 
 * @author badqiu
 *
 */
public class JedisPoolFactoryBean implements FactoryBean<JedisPool>, InitializingBean,DisposableBean {
	protected static final Logger log = LoggerFactory.getLogger(JedisPoolFactoryBean.class);
	
	
	private int timeout = Protocol.DEFAULT_TIMEOUT;
	private int database = Protocol.DEFAULT_DATABASE;
	
	private int port = Protocol.DEFAULT_PORT;
	private String host = null;
	private final String password = null;

	private JedisPoolConfig poolConfig = new JedisPoolConfig();
	
	private JedisPool jedisPool;

	public JedisPool getObject() throws Exception {
		return jedisPool;
	}

	public Class<JedisPool> getObjectType() {
		return JedisPool.class;
	}

	public boolean isSingleton() {
		return true;
	}
	
	public void setPoolConfig(JedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public int getMaxIdle() {
		return poolConfig.getMaxIdle();
	}

	public void setMaxIdle(int maxIdle) {
		poolConfig.setMaxIdle(maxIdle);
	}

	public int getMinIdle() {
		return poolConfig.getMinIdle();
	}

	public void setMinIdle(int minIdle) {
		poolConfig.setMinIdle(minIdle);
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		poolConfig.setTestOnBorrow(testOnBorrow);
	}

	public void setTestOnReturn(boolean testOnReturn) {
		poolConfig.setTestOnReturn(testOnReturn);
	}
	
	

	public boolean getBlockWhenExhausted() {
		return poolConfig.getBlockWhenExhausted();
	}

	public String getEvictionPolicyClassName() {
		return poolConfig.getEvictionPolicyClassName();
	}

	public boolean getFairness() {
		return poolConfig.getFairness();
	}

	public boolean getJmxEnabled() {
		return poolConfig.getJmxEnabled();
	}

	public String getJmxNameBase() {
		return poolConfig.getJmxNameBase();
	}

	public String getJmxNamePrefix() {
		return poolConfig.getJmxNamePrefix();
	}

	public boolean getLifo() {
		return poolConfig.getLifo();
	}

	public int getMaxTotal() {
		return poolConfig.getMaxTotal();
	}

	public long getMaxWaitMillis() {
		return poolConfig.getMaxWaitMillis();
	}

	public boolean getTestOnBorrow() {
		return poolConfig.getTestOnBorrow();
	}

	public boolean getTestOnCreate() {
		return poolConfig.getTestOnCreate();
	}

	public boolean getTestOnReturn() {
		return poolConfig.getTestOnReturn();
	}

	public boolean getTestWhileIdle() {
		return poolConfig.getTestWhileIdle();
	}

	public void setBlockWhenExhausted(boolean blockWhenExhausted) {
		poolConfig.setBlockWhenExhausted(blockWhenExhausted);
	}

	public void setEvictionPolicyClassName(String evictionPolicyClassName) {
		poolConfig.setEvictionPolicyClassName(evictionPolicyClassName);
	}

	public void setFairness(boolean fairness) {
		poolConfig.setFairness(fairness);
	}

	public void setJmxEnabled(boolean jmxEnabled) {
		poolConfig.setJmxEnabled(jmxEnabled);
	}

	public void setJmxNameBase(String jmxNameBase) {
		poolConfig.setJmxNameBase(jmxNameBase);
	}

	public void setJmxNamePrefix(String jmxNamePrefix) {
		poolConfig.setJmxNamePrefix(jmxNamePrefix);
	}

	public void setLifo(boolean lifo) {
		poolConfig.setLifo(lifo);
	}

	public void setMaxTotal(int maxTotal) {
		poolConfig.setMaxTotal(maxTotal);
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		poolConfig.setMaxWaitMillis(maxWaitMillis);
	}

	public void setTestOnCreate(boolean testOnCreate) {
		poolConfig.setTestOnCreate(testOnCreate);
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		poolConfig.setTestWhileIdle(testWhileIdle);
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return poolConfig.getTimeBetweenEvictionRunsMillis();
	}

	public void setTimeBetweenEvictionRunsMillis(
			long timeBetweenEvictionRunsMillis) {
		poolConfig
				.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
	}

	public int getNumTestsPerEvictionRun() {
		return poolConfig.getNumTestsPerEvictionRun();
	}

	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		poolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
	}

	public long getMinEvictableIdleTimeMillis() {
		return poolConfig.getMinEvictableIdleTimeMillis();
	}

	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		poolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
	}

	public long getSoftMinEvictableIdleTimeMillis() {
		return poolConfig.getSoftMinEvictableIdleTimeMillis();
	}

	public void setSoftMinEvictableIdleTimeMillis(
			long softMinEvictableIdleTimeMillis) {
		poolConfig
				.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
	}

	public void setServer(String server) {
		String[] array = StringUtils.split(StringUtils.trim(server),":");
		if(array.length == 2) {
			setHost(array[0]);
			setPort(Integer.parseInt(array[1]));
		}else if(array.length == 1) {
			setHost(array[0]);
		}else {
			throw new IllegalArgumentException("invalid redis server:"+server+" example value = 127.0.0.1:6737 ");
		}
	}

	public void afterPropertiesSet() throws Exception {
		Assert.notNull(poolConfig,"poolConfig must be not null");
		jedisPool = new JedisPool(poolConfig, host, port, timeout,password, database);
		log.info("created JedisPool "+this);
	}

	@Override
	public String toString() {
		return " [host=" + host + ", port=" + port + " timeout=" + timeout + ", database="+ database + " poolConfig:" + ToStringBuilder.reflectionToString(poolConfig) + "]";
	}

	public void destroy() throws Exception {
		jedisPool.destroy();
	}

}
