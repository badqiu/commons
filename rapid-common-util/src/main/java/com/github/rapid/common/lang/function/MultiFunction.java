package com.github.rapid.common.lang.function;

import java.io.Flushable;
import java.io.IOException;
import java.util.function.Function;

import org.apache.commons.lang.math.RandomUtils;

import com.github.rapid.common.util.ObjectUtil;

public class MultiFunction <T,R> implements Function<T,R>,AutoCloseable,Flushable{
	
	private Function<T,R>[] functions;
	
	private LoadBalancing loadBalancing = LoadBalancing.ALL;
	
	public MultiFunction() {
	}

	public MultiFunction(Function<T, R>... functions) {
		super();
		setFunctions(functions);
	}
	
	public MultiFunction(LoadBalancing loadBalancing,Function<T, R>... functions) {
		super();
		setFunctions(functions);
		setLoadBalancing(loadBalancing);
	}

	public Function<T, R>[] getFunctions() {
		return functions;
	}

	public void setFunctions(Function<T, R>... functions) {
		this.functions = functions;
	}

	public void setFunction(Function<T, R> func) {
		setFunctions(func);
	}

	public LoadBalancing getLoadBalancing() {
		return loadBalancing;
	}

	public void setLoadBalancing(LoadBalancing loadBalancing) {
		this.loadBalancing = loadBalancing;
	}

	@Override
	public R apply(T t) {
		if(loadBalancing == LoadBalancing.ALL) {
			return toAll(t);
		}else if(loadBalancing == LoadBalancing.RANDOM) {
			Function<T, R> func = randomOneFunction();
			return func.apply(t);
		}else if(loadBalancing == LoadBalancing.ROUND_ROBIN) {
			Function<T, R> func = roundRobinOneFunction();
			return func.apply(t);
		}else if(loadBalancing == LoadBalancing.HASH) {
			Function<T, R> func = hashOneFunction(t);
			return func.apply(t);			
		}else {
			throw new RuntimeException("error loadBalancing:"+loadBalancing);
		}
	}

	private Function<T, R> hashOneFunction(T t) {
		int length = functions.length;
		int index = Math.abs(t.hashCode()) % length;
		
		Function<T,R> func = functions[index];
		return func;
	}

	private R toAll(T t) {
		R last = null;
		for(Function<T,R> func : functions) {
			last = func.apply(t);
		}
		return last;
	}
	
	long count = 0;
	private Function<T, R> roundRobinOneFunction() {
		int length = functions.length;
		int index = (int)(count++ % length);
		
		Function<T,R> func = functions[index];
		
		if(count >= Long.MAX_VALUE) {
			count = 0;
		}
		return func;
	}
	
	private Function<T, R> randomOneFunction() {
		int index = RandomUtils.nextInt(functions.length);
		Function<T,R> func = functions[index];
		return func;
	}

	@Override
	public void flush() throws IOException {
		ObjectUtil.flushAll(functions);
	}

	@Override
	public void close() throws Exception {
		ObjectUtil.closeAll(functions);
	}
	
	public static enum LoadBalancing {
		ALL,
		RANDOM,
		ROUND_ROBIN,
		HASH // IP_HASH,URL_HASH,USER_HASH
//		WEIGHT,
	}
	
}
