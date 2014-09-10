package com.duowan.common.rpc;

import java.util.Map;

public class RPCRequest {
	
	private String method;
	
	private String format;
	
	private Map<String,Object> argumentsMap;
	
	private Object[] arguments;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public Map<String, Object> getArgumentsMap() {
		return argumentsMap;
	}

	public void setArgumentsMap(Map<String, Object> argumentsMap) {
		this.argumentsMap = argumentsMap;
	}

	public Object[] getArguments() {
		return arguments;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}
	
//	private Class[] argumentTypes;
	
	
	
}
