package com.github.rapid.common.lang.function;

import java.util.function.Function;

public class SyncFunction<T,R> extends ProxyFunction<T, R> {

	public SyncFunction() {
		super();
	}

	public SyncFunction(Function<T, R> proxy) {
		super(proxy);
	}
	
	@Override
	public synchronized R apply(T t) {
		return super.apply(t);
	}
	
}
