package com.duowan.common.redis;

import org.aopalliance.aop.Advice;
import org.springframework.aop.framework.ProxyFactory;

class ProxyUtil {

//	@SuppressWarnings("unchecked")
//	public static <T> T createProxy(Class<T> targetClass,Advice... advices) {
//		ProxyFactory factory = new ProxyFactory();
//		factory.setTargetClass(targetClass);
//		factory.setOptimize(true);
//		for(Advice advice : advices) {
//			factory.addAdvice(advice);
//		}
//		return (T)factory.getProxy();
//	}
	
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(T target,Advice... advices) {
		ProxyFactory factory = new ProxyFactory();
		factory.setTarget(target);
		factory.setOptimize(true);
		for(Advice advice : advices) {
			factory.addAdvice(advice);
		}
		return (T)factory.getProxy();
	}
	
}
