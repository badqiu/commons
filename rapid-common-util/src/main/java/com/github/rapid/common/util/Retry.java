package com.github.rapid.common.util;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

/**
 * 对任务,出错重试的工具类,可以设置重试次数,重试间隔
 * @author badqiu
 *
 */
public class Retry<T>{
	private static Logger logger = LoggerFactory.getLogger(Retry.class);
	
	private Callable<T> cmd;
	private int retryTimes;  // 重试次数
	private long retryInterval;// 重试间隔(毫秒)
	private long retryTimeout; //超时时间(毫秒)
	private Predicate<Exception> retryTestFunction; //是否重试,返回false不重试
	private Class<? extends Exception> retryFor; //批量的异常才重试
	
	private int useRetryTimes;
	private Exception lastException;
	
	
	public Retry(int retryTimes, long retryInterval,long retryTimeout) {
		this(retryTimes,retryInterval,retryTimeout,null);
	}
	
	public Retry(int retryTimes, long retryInterval,long retryTimeout,Callable<T> cmd) {
		if(retryTimes > 0) {
			Assert.isTrue(retryInterval > 0,"retryInterval > 0 must be true ");
		}
		this.cmd = cmd;
		this.retryTimes = retryTimes;
		this.retryInterval = retryInterval;
		this.retryTimeout = retryTimeout;
	}

	public Retry(int retryTimes, long retryInterval, Callable<T> cmd) {
		this(retryTimes,retryInterval,0,cmd);
	}

	public void setRetryTestFunction(Predicate<Exception> retryTestFunction) {
		this.retryTestFunction = retryTestFunction;
	}
	
	public void setRetryFor(Class<? extends Exception> retryFor) {
		this.retryFor = retryFor;
	}

	public int getUseRetryTimes() {
		return useRetryTimes;
	}

	public Exception getLastException() {
		return lastException;
	}

	public T exec() throws RetryException{
		return exec(cmd);
	}
	
	public <R> R exec(Callable<R> command) throws RetryException{
		long start = 0;
		if(retryTimeout > 0) {
			start = System.currentTimeMillis();
		}
		
		while(true) {
			try {
				R result = command.call();
				return result;
			} catch (Exception e) {
				logger.warn("occer error,retry execute: useRetryTimes:"+useRetryTimes+" retryTimes:"+retryTimes+" retryInterval:"+retryInterval,e);
				
				if(retryTestFunction != null) {
					if(!retryTestFunction.test(e)) {
						throw new RuntimeException(e);
					}
				}
				
				if(retryFor != null) {
					if(!retryFor.equals(e.getClass())) {
						throw new RuntimeException(e);
					}
				}
				
				lastException = e;
				useRetryTimes++;
				if(useRetryTimes > retryTimes) {
					break;
				}
				
				if(retryTimeout > 0) {
					long costTime = System.currentTimeMillis() - start;
					if(costTime > retryTimeout) {
						throw new RetryException(useRetryTimes,"retry timeout error,retryTimeout:"+retryTimeout,lastException);
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
		
		throw new RetryException(useRetryTimes,"retry error",lastException);
	}
	
	public static <T> T retry(int retryTimes,long retryIntervalMills,Callable<T> cmd) {
		return new Retry<T>(retryTimes,retryIntervalMills,cmd).exec();
	}
	
	public static <T> T retry(int retryTimes,long retryIntervalMills,long retryTimeoutMills,Callable<T> cmd) {
		return new Retry<T>(retryTimes,retryIntervalMills,retryTimeoutMills,cmd).exec();
	}

	public static <T> T retry(int retryTimes,Duration retryInterval,Callable<T> cmd) {
		return retry(retryTimes,retryInterval.toMillis(),cmd);
	}
	
	public static <T> T retry(int retryTimes,Duration retryInterval,Duration retryTimeout,Callable<T> cmd) {
		return retry(retryTimes,retryInterval.toMillis(),retryTimeout.toMillis(),cmd);
	}
	
	public static <T> T retry(int retryTimes,String retryInterval,Callable<T> cmd) {
		return retry(retryTimes,DurationUtil.parseDuration(retryInterval),cmd);
	}
	
	public static <T> T retry(int retryTimes,String retryInterval,String retryTimeout,Callable<T> cmd) {
		return retry(retryTimes,DurationUtil.parseDuration(retryInterval),DurationUtil.parseDuration(retryTimeout),cmd);
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
