package com.github.rapid.common.lang.function;

import java.util.function.Function;

public class NullFunction<T,R> implements Function<T,R>{

	@Override
	public R apply(T t) {
		return null;
	}

}
