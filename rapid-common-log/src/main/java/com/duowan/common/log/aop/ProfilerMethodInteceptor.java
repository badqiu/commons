package com.duowan.common.log.aop;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;

import com.duowan.common.log.Profiled;
import com.duowan.common.util.Profiler;
/**
 * 用Profiler计时方法拦截器
 * 实现Profiler.enter() Profiler.release();
 * 
 * @author badqiu
 *
 */
public class ProfilerMethodInteceptor implements MethodInterceptor{

	public Object invoke(MethodInvocation invocation) throws Throwable {
		
		profilerEnter(invocation);
		
		Throwable exception = null;
		Object result = null;
		try {
			result = invocation.proceed();
			return result;
		} catch(Throwable e) {
			exception = e;
			throw e;
		} finally {
			Profiler.release(exception,0,result);
		}
	}

	private static void profilerEnter(MethodInvocation invocation) {
		String clazzName = invocation.getThis().getClass().getSimpleName();
		Profiled profiled = invocation.getMethod().getAnnotation(Profiled.class);
		int loopCount = getLoopCount(invocation, profiled);
		String message = getMessage(invocation, clazzName, profiled);
		
		Profiler.enter(message,loopCount);
	}

	private static int getLoopCount(MethodInvocation invocation, Profiled profiled) {
		if(invocation.getArguments().length == 0) {
			return -1;
		}
		int loopCountParamIndex = getLoopCountParamIndex(invocation, profiled);
		if(loopCountParamIndex < 0) {
			return -1;
		}
		
		Object loopCountObject = invocation.getArguments()[loopCountParamIndex];
		if(loopCountObject instanceof Number) {
			return ((Number)loopCountObject).intValue();
		}else {
			return getSequenceLength(loopCountObject);
		}
	}
	
	private static Map<Method, Integer> loopCountParamIndexCache = new HashMap<Method, Integer>();
	private static int getLoopCountParamIndex(MethodInvocation invocation,
			Profiled profiled) {
		Method method = invocation.getMethod();
		Integer loopCountParamIndex = loopCountParamIndexCache.get(method);
		if(loopCountParamIndex == null) {
			if(profiled == null) {
				for(int i = 0; i < invocation.getArguments().length; i++) {
					int length = getSequenceLength(invocation.getArguments()[i]);
					if(length >= 0) {
						loopCountParamIndex = i;
					}
				}
			}else {
				loopCountParamIndex = profiled.loopCountParamIndex();
			}
			loopCountParamIndex = loopCountParamIndex == null ? -1 : loopCountParamIndex;
			loopCountParamIndexCache.put(method,loopCountParamIndex );
		}
		return loopCountParamIndex;
	}

	private static String getMessage(MethodInvocation invocation, String clazzName,
			Profiled profiled) {
		String message = profiled == null ? null : profiled.message();
		if(StringUtils.isBlank(message)) {
			return clazzName+"."+invocation.getMethod().getName();
		}else {
			return String.format(message, invocation.getArguments());
		}
	}

	private static int getSequenceLength(Object list) {
		if(list == null) return -1;
		
		if(list instanceof Collection) {
			return ((Collection)list).size();
		} else if(list.getClass().isArray()) {
			return Array.getLength(list);
		} else if(list instanceof Map) {
			return ((Map)list).size();
		} else {
			return -1;
		}
	}

}
