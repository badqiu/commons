package com.github.rapid.common.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;


public class PingUtilTest {
	
	@Test
	public void test() {
		boolean r = PingUtil.socketPing("www.baidu.com", 80);
		assertTrue(r);
	}

	@Test
	public void test_error() {
		long start = System.currentTimeMillis();
		boolean r = PingUtil.socketPing("www.baidu.com", 12922);
		assertFalse(r);
		
		long cost = System.currentTimeMillis() - start;
		assertTrue(cost >= PingUtil.defaultTimeout);
	}

	@Test
	public void test_urlPing() throws URISyntaxException, Exception, InvocationTargetException, NoSuchMethodException {
		boolean r = PingUtil.urlPing("http://wwww.baidu.com");
		assertTrue(r);
		
		r = PingUtil.urlPing("https://wwww.baidu.com");
		assertTrue(r);
		
		URI uri = new URI("redis://www.baidu.com:443/echo?timeout=100&name=super");
		System.out.println(BeanUtils.describe(uri));
	}
	
	@Test
	public void test_uri_ping() throws URISyntaxException, Exception, InvocationTargetException, NoSuchMethodException {
		boolean r = PingUtil.uriPing("redis://wwww.baidu.com:80");
		assertTrue(r);
	}
	
}
