package com.github.rapid.common.retry;

public class Retry {
	
	private int retryTimes;  // 重试次数,retry forever
	private int retryInterval;// 重试间隔
	private int failovers; //failover retry次数
	private int timeout; //超时时间
	
	public boolean retryByIsNetworkException(Exception e) {
		return false;
	}
	
	public boolean shouldRetry(Exception e, int retries, int failovers) throws Exception {
		return false;
	}
}
