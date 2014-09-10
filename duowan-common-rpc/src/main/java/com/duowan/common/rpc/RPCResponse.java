package com.duowan.common.rpc;

public class RPCResponse <T>{
	
	/**
	 * 返回结果
	 */
	private T result;
	
	/**
	 * 错误码
	 */
	private String errno;
	
	/**
	 * 具体错误消息
	 */
	private String error;
	
	public RPCResponse() {
	}
	
	public RPCResponse(T result) {
		super();
		this.result = result;
	}

	public RPCResponse(String errorCode, String errorMessage) {
		super();
		this.errno = errorCode;
		this.error = errorMessage;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getErrno() {
		return errno;
	}

	public void setErrno(String errorCode) {
		this.errno = errorCode;
	}

	public String getError() {
		return error;
	}

	public void setError(String errorMessage) {
		this.error = errorMessage;
	}
	
}
