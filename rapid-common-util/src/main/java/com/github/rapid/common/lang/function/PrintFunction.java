package com.github.rapid.common.lang.function;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.function.Function;

public class PrintFunction<T,R> implements Function<T,R>{
	private String prefix = "";
	private PrintStream out = System.out;
	
	public PrintFunction() {
	}
	
	public PrintFunction(OutputStream out, String prefix) {
		setOut(out);
		this.prefix = prefix;
	}
	
	public PrintFunction(PrintStream out) {
		setOut(out);
	}

	public OutputStream getOut() {
		return out;
	}

	public void setOut(OutputStream out) {
		Objects.requireNonNull(out, "out must be not null");
		this.out = new PrintStream(out);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public R apply(T t) {
		if(t == null) return null;
		
		String string = prefix + t;
		out.println(string);
		return null;
		
	}
	
}
