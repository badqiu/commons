package com.duowan.common.log;

import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.duowan.common.util.Profiler;


/**
 * 用于启动Profiler的代理Runnable,
 * 通过代理实现Profiler.start(),Profiler.release()并记录日志ProfilerLogger.info()
 * 
 * @author badqiu
 *
 */
public class ProfilerRunnable implements Runnable{
	private Runnable delegate;
	private String profilerMessage;
	
	/**
	 * @param delegate 代理Runnable
	 * @param profilerMessage profiler消息
	 */
	public ProfilerRunnable(Runnable delegate) {
		this(delegate,null);
	}
	
	/**
	 * @param delegate 代理Runnable
	 * @param profilerMessage profiler消息
	 */
	public ProfilerRunnable(Runnable delegate, String profilerMessage) {
		if(delegate == null) throw new IllegalArgumentException("'delegate' Runnable must be not null");
		
		this.delegate = delegate;
		this.profilerMessage = defaultProfilerMessage(profilerMessage);
	}

	public void run() {
		if(ProfilerLogger.isInfoEnabled()) {
			Profiler.start(profilerMessage);
		}
		
		Exception exception = null;
		try {
			delegate.run();
		}catch(RuntimeException e) {
			exception = e;
			throw e;
		}finally {
			if(ProfilerLogger.isInfoEnabled()) {
				Profiler.release(exception);
				ProfilerLogger.infoDigestLogAndDump();
			}
		}
	}
	static Pattern p = Pattern.compile("\\d+");
	static String defaultProfilerMessage(String profilerMessage) {
		if(StringUtils.isBlank(profilerMessage)) {
			return p.matcher(Thread.currentThread().getName()).replaceAll("");
		}
		return profilerMessage;
	}

}
