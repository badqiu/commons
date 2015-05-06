package com.github.rapid.common.rpc;

import java.io.Serializable;

public class RPCResponse <T> implements Serializable{
	
	/**
	 * 返回结果
	 */
	private T result;
	
	/**
	 * 错误码
	 */
	private String errCode;
	
	/**
	 * 具体错误消息
	 */
	private String errMsg;
	
	public RPCResponse() {
	}
	
	public RPCResponse(T result) {
		super();
		this.result = result;
	}

	public RPCResponse(String errorCode, String errorMessage) {
		super();
		this.errCode = errorCode;
		this.errMsg = errorMessage;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getErrCode() {
		return errCode;
	}

	public void setErrCode(String errorCode) {
		this.errCode = errorCode;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errorMessage) {
		this.errMsg = errorMessage;
	}
	
}
