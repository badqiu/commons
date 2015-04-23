package com.github.rapid.common.util;

import java.util.concurrent.Callable;

import org.springframework.util.Assert;

/**
 * 对任务,出错重试的工具类,可以设置重试次数,重试间隔
 * @author badqiu
 *
 */
public class Retry {
	
	private Callable cmd;
	private int retryTimes;  // 重试次数
	private int retryInterval;// 重试间隔
//	private int failovers; //failover retry次数
//	private int timeout; //超时时间
	private int useRetryTimes;
	private Exception exception;
	
	public Retry(int retryTimes, int retryInterval,Callable cmd) {
		super();
		if(retryTimes > 0) {
			Assert.isTrue(retryInterval > 0,"retryInterval > 0 must be true ");
		}
		this.cmd = cmd;
		this.retryTimes = retryTimes;
		this.retryInterval = retryInterval;
	}

//	private boolean retryByIsNetworkException(Exception e) {
//		return false;
//	}
//	
//	private boolean shouldRetry(Exception e, int retries, int failovers) throws Exception {
//		return false;
//	}
	
	public Object exec() {
		
		while(true) {
			try {
				Object result = cmd.call();
				return result;
			} catch (Exception e) {
				exception = e;
				useRetryTimes++;
				if(useRetryTimes > retryTimes) {
					break;
				}
				Assert.isTrue(retryInterval > 0 ,"retryInterval must be true");
				try {
					Thread.sleep(retryInterval);
				} catch (InterruptedException e1) {
					throw new RuntimeException("sleep InterruptedException",e1);
				}
				
			}
		}
		if(exception instanceof RuntimeException) {
			throw (RuntimeException)exception;
		}else {
			throw new RuntimeException("retry error,useRetryTimes:"+useRetryTimes,exception);
		}
	}
	
	public static Object retry(int retryTimes,int retryInterval,Callable cmd) {
		return new Retry(retryTimes,retryInterval,cmd).exec();
	}
	
}
