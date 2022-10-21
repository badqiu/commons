package com.github.rapid.common.lang.function;

import java.io.Flushable;
import java.io.IOException;
import java.util.function.Function;

public class ProxyFunction <T,R> implements Function<T,R>,AutoCloseable,Flushable{
	private Function<T,R> proxy;

	public ProxyFunction() {
	}
	
	public ProxyFunction(Function<T,R> proxy) {
		super();
		this.proxy = proxy;
	}

	public Function<T,R> getProxy() {
		return proxy;
	}

	public void setProxy(Function<T,R> proxy) {
		this.proxy = proxy;
	}

	@Override
	public R apply(T t) {
		return proxy.apply(t);
	}

	@Override
	public void close() throws Exception {
		flush();
		
		if(proxy instanceof AutoCloseable) {
			((AutoCloseable)proxy).close();
		}
	}

	@Override
	public void flush() throws IOException {
		if(proxy instanceof Flushable) {
			((Flushable)proxy).flush();
		}
	}


}
