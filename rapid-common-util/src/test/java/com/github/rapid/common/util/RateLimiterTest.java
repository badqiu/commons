package com.github.rapid.common.util;

import org.junit.Test;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterTest {

	@Test
	public void test() {
		RateLimiter limiter = RateLimiter.create(5);
		System.out.println(limiter.acquire());
		System.out.println(limiter.acquire());
		System.out.println(limiter.acquire());
		System.out.println(limiter.acquire());
		System.out.println(limiter.acquire());
		System.out.println(limiter.acquire());
	}
}
