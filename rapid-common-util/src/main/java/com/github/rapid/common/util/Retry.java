package com.github.rapid.common.util;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 对任务,出错重试的工具类,可以设置重试次数,重试间隔
 * @author badqiu
 *
 */
public class Retry {
	private static Logger logger = LoggerFactory.getLogger(Retry.class);
	
	private Callable cmd;
	private int retryTimes;  // 重试次数
	private long retryInterval;// 重试间隔(毫秒)
	private long retryTimeout; //超时时间(毫秒)
	
//	private int failovers; //failover retry次数
	private int useRetryTimes;
	private Exception exception;
	
	private Predicate<Exception> retryTestFunction; //是否重试,返回false不重试
	
	public Retry(int retryTimes, long retryInterval,long retryTimeout,Callable cmd) {
		if(retryTimes > 0) {
			Assert.isTrue(retryInterval > 0,"retryInterval > 0 must be true ");
		}
		this.cmd = cmd;
		this.retryTimes = retryTimes;
		this.retryInterval = retryInterval;
		this.retryTimeout = retryTimeout;
	}

	public Retry(int retryTimes, long retryInterval, Callable cmd) {
		this(retryTimes,retryInterval,0,cmd);
	}

	public void setRetryTestFunction(Predicate<Exception> retryTestFunction) {
		this.retryTestFunction = retryTestFunction;
	}

	public Object exec() throws RetryException{
		long start = 0;
		if(retryTimeout > 0) {
			start = System.currentTimeMillis();
		}
		
		while(true) {
			try {
				Object result = cmd.call();
				return result;
			} catch (Exception e) {
				logger.warn("occer error,retry execute: useRetryTimes:"+useRetryTimes+" retryTimes:"+retryTimes+" retryInterval:"+retryInterval,e);
				
				if(retryTestFunction != null) {
					if(!retryTestFunction.test(e)) {
						throw new RuntimeException(e);
					}
				}
				
				exception = e;
				useRetryTimes++;
				if(useRetryTimes > retryTimes) {
					break;
				}
				
				if(retryTimeout > 0) {
					long costTime = System.currentTimeMillis() - start;
					if(costTime > retryTimeout) {
						throw new RetryException(useRetryTimes,"retry timeout error,retryTimeout:"+retryTimeout,exception);
					}
				}
				
				Assert.isTrue(retryInterval > 0 ,"retryInterval must be true");
				try {
					Thread.sleep(retryInterval);
				} catch (InterruptedException e1) {
					throw new RetryException(useRetryTimes,"sleep InterruptedException",e1);
				}
				
			}
		}
		
		throw new RetryException(useRetryTimes,"retry error",exception);
	}
	
	public static Object retry(int retryTimes,long retryInterval,Callable cmd) {
		return new Retry(retryTimes,retryInterval,cmd).exec();
	}
	
	public static Object retry(int retryTimes,Duration retryInterval,Callable cmd) {
		return new Retry(retryTimes,retryInterval.toMillis(),cmd).exec();
	}
	
	public static Object retry(int retryTimes,long retryInterval,long retryTimeout,Callable cmd) {
		return new Retry(retryTimes,retryInterval,retryTimeout,cmd).exec();
	}
	
	public static Object retry(int retryTimes,Duration retryInterval,Duration retryTimeout,Callable cmd) {
		return new Retry(retryTimes,retryInterval.toMillis(),retryTimeout.toMillis(),cmd).exec();
	}
	
	
	public static class RetryException extends RuntimeException {
		private static final long serialVersionUID = 7417563344396226320L;
		
		private int useRetryTimes;

		public RetryException(int useRetryTimes) {
			super();
			this.useRetryTimes = useRetryTimes;
		}

		public RetryException(int useRetryTimes,String message, Throwable cause) {
			super(message, cause);
			this.useRetryTimes = useRetryTimes;
		}

		public RetryException(int useRetryTimes,String message) {
			super(message);
			this.useRetryTimes = useRetryTimes;
		}

		public RetryException(int useRetryTimes,Throwable cause) {
			super(cause);
			this.useRetryTimes = useRetryTimes;
		}

		public int getUseRetryTimes() {
			return useRetryTimes;
		}
		
	}
}
