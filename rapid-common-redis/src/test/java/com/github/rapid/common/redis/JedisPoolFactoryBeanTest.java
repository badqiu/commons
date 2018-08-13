package com.github.rapid.common.redis;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.util.Assert;

public class JedisPoolFactoryBeanTest {

	@Test
	public void test() throws Exception {
		JedisPoolFactoryBean f = new JedisPoolFactoryBean();
		f.setServer("redis://jf02oO0Oo:user@120.132.77.166:6379/1");
		f.afterPropertiesSet();
		
		Object pool = f.getObject();
		Assert.notNull(pool);
	}

}
