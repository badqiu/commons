package com.github.rapid.common.rpc;

import java.io.Serializable;
import java.util.Map;


public class RPCRequest implements Serializable{
	/**
	 * 要调用的方法
	 */
	private String method;
	
	/**
	 * 方法参数
	 */
	private Object[] arguments;
	
	/**
	 * 方法返回值类型: json,java...
	 */
	private String format;
	
	private Map<String,String> headers; //HTTP头

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		if(method == null || method.isEmpty()) {
			throw new IllegalArgumentException("method must be not empty");
		}
		this.method = method;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		if(format == null || format.isEmpty()) {
			throw new IllegalArgumentException("format must be not empty");
		}
		this.format = format;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	
//	private Class[] argumentTypes;
	
	
}
