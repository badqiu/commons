package com.github.rapid.common.util;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FunctionUtil {

	public static <T> Function<T,Void> toFunction(Consumer<T> func) {
		Function<T,Void> result = new Function<T,Void>() {
			public Void apply(T t) {
				func.accept(t);
				return null;
			}
		};
		return result;
	}
	
	public static <T> Function<Void,T> toFunction(Supplier<T> func) {
		Function<Void,T> result = new Function<Void,T>() {
			public T apply(Void t) {
				return func.get();
			}
		};
		return result;
	}
	
	public static <T> Function<T,Boolean> toFunction(Predicate<T> func) {
		Function<T,Boolean> result = new Function<T,Boolean>() {
			public Boolean apply(T t) {
				return func.test(t);
			}
		};
		return result;
	}
	
	public static Function<Void,Void> toFunction(Runnable func) {
		Function<Void,Void> result = new Function<Void,Void>() {
			public Void apply(Void t) {
				func.run();
				return null;
			}
		};
		return result;
	}
	
	public static <T> Function<Void,T> toFunction(Callable<T> func) {
		Function<Void,T> result = new Function<Void,T>() {
			public T apply(Void t) {
				try {
					return func.call();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		
		return result;
	}
	
}
