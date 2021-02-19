package com.github.rapid.common.log;

/**
 * 用于用于Exception摘要的接口
 * 
 * @see ProfilerDigestLog#setExceptionDigest(ExceptionDigest)
 * 
 * @author badqiu
 *
 */
public interface ExceptionDigest {
	
	public String getErrorCode(Throwable e);
	
}