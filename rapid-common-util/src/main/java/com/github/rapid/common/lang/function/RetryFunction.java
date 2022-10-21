package com.github.rapid.common.lang.function;

import java.util.function.Function;

import org.springframework.util.Assert;

import com.github.rapid.common.util.Retry;

public class RetryFunction<T,R> extends ProxyFunction<T,R> implements Function<T,R> {
	private int retryTimes = 0; //重试次数
	private long retryIntervalMills = 3000; //重试间隔(毫秒)
	private long retryTimeoutMills = 0; //重试超时时间

	public RetryFunction() {
	}

	public RetryFunction(Function<T,R> proxy) {
		super(proxy);
	}

	public int getRetryTimes() {
		return retryTimes;
	}

	public void setRetryTimes(int retryTimes) {
		Assert.isTrue(retryTimes > 0 ,"retryTimes > 0 must be true");
		this.retryTimes = retryTimes;
	}

	public void setRetryIntervalMinutes(long value) {
		setRetryIntervalMills(value * 1000 * 60);
	}
	
	public void setRetryIntervalSeconds(long value) {
		setRetryIntervalMills(value * 1000);
	}
	
	public void setRetryIntervalMills(long retryIntervalMills) {
		Assert.isTrue(retryIntervalMills > 0 ,"retryIntervalMills > 0 must be true");
		this.retryIntervalMills = retryIntervalMills;
	}
	
	public void setRetryTimeoutMills(long retryTimeoutMills) {
		this.retryTimeoutMills = retryTimeoutMills;
	}

	public void setRetryTimeoutSeconds(long value) {
		setRetryTimeoutMills(value * 1000);
	}
	
	public void setRetryTimeoutMinutes(long value) {
		setRetryTimeoutMills(value * 1000 * 60);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public R apply(T t) {
		return (R)Retry.retry(retryTimes, retryIntervalMills,retryTimeoutMills, () -> {
			super.apply(t);
			return null;
		});
	}

}
