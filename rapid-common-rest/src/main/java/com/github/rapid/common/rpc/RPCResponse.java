package com.github.rapid.common.rpc;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class RPCResponse <T> implements Serializable{
	
	/**
	 * 返回结果
	 */
	private T result;
	
	/**
	 * 错误码
	 */
	private String errorCode;
	
	/**
	 * 具体消息(如错误消息)
	 */
	private String message;
	
	/**
	 * 错误完整错误堆栈日志，可以用于在测试环境展示，方便快速查错
	 */
	private String errorLog;

	/**
	 * 请求跟踪ID
	 */
	private String traceId;
	
	
	public RPCResponse() {
	}
	
	public RPCResponse(T result) {
		this.result = result;
	}

	public RPCResponse(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.message = errorMessage;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getErrorLog() {
		return errorLog;
	}

	public void setErrorLog(String errorLog) {
		this.errorLog = errorLog;
	}

	public String getTraceId() {
		return traceId;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
	}

	public boolean isSuccess() {
		return StringUtils.isBlank(errorCode);
	}
	
}
