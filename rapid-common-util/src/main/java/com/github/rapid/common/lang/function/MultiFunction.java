package com.github.rapid.common.lang.function;

import java.io.Flushable;
import java.io.IOException;
import java.util.function.Function;

import com.github.rapid.common.util.ObjectUtil;

public class MultiFunction <T,R> implements Function<T,R>,AutoCloseable,Flushable{
	
	private Function<T,R>[] functions;
	
	public MultiFunction() {
	}

	public MultiFunction(Function<T, R>... functions) {
		super();
		setFunctions(functions);
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

	@Override
	public R apply(T t) {
		R last = null;
		for(Function<T,R> func : functions) {
			last = func.apply(t);
		}
		return last;
	}

	@Override
	public void flush() throws IOException {
		ObjectUtil.flushAll(functions);
	}

	@Override
	public void close() throws Exception {
		ObjectUtil.closeAll(functions);
	}
	
}
