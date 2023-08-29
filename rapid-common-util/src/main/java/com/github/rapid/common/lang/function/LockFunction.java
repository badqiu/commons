package com.github.rapid.common.lang.function;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.poi.ss.formula.functions.T;
import org.slf4j.LoggerFactory;

public class LockFunction<T,R> extends ProxyFunction<T,R>{

	private String lockGroup = "default";
	private String lockId;
	
	private Lock _lock = null;
	
	@Override
	public R apply(T t) {
		_lock.lock();
		try {
			return proxy.apply(t);
		}finally {
			_lock.unlock();
		}
	}
	
	public void init() {
		if(_lock == null) {
			_lock = new ReentrantLock();
		}
	}
	
}
