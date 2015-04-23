package com.github.rapid.common.exception;

/**
 * 通用异常:
 * 包含错误码(ErrorCode)的异常，通过errorCode可以对异常进行针地性的处理。
 * 
 * @author badqiu
 *
 */
public class ErrorCodeException extends RuntimeException{
	
	private static final long serialVersionUID = 5585767749205633804L;
	
	private String errorCode;
	
	// 构造方法 START
	
	public ErrorCodeException(){
	}
	
	public ErrorCodeException(String errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public ErrorCodeException(Integer errorCode) {
		super();
		this.errorCode = String.valueOf(errorCode);
	}
	
	public ErrorCodeException(String errorCode,String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ErrorCodeException(Integer errorCode,String message, Throwable cause) {
		super(message, cause);
		this.errorCode = String.valueOf(errorCode);
	}
	
	public ErrorCodeException(String errorCode,String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ErrorCodeException(Integer errorCode,String message) {
		super(message);
		this.errorCode = String.valueOf(errorCode);
	}
	
	public ErrorCodeException(String errorCode,Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public ErrorCodeException(Integer errorCode,Throwable cause) {
		super(cause);
		this.errorCode = String.valueOf(errorCode);
	}
	// 构造方法 END
	
	public String getErrorCode() {
		return errorCode;
	}
	
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String toString() {
		return "errorCode:"+errorCode+",message:"+getMessage();
	}
	
}
