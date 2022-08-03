package com.github.rapid.common.rpc;



/**
 * 远程调用异常,服务端可抛出该异常,客户端可直接捕捉到
 * 
 * HTTP协议代码区间: 
 * 	200 ~ 299  正常响应
 *  300 ~ 399
 *  400 ~ 499 客户端异常
 *  500 ~ 599 服务端异常
 */
public class WebServiceException extends RuntimeException {
	
	
	private static final long serialVersionUID = -411406336325148633L;
	
	/** 未知错误  as HTTP 500 */
	public static String UNKNOW_ERROR = "UNKNOW_ERROR"; 
	
	/** 非法参数  as HTTP SC_BAD_REQUEST:400  */
	public static String ILLEGAL_ARGUMENT = "ILLEGAL_ARGUMENT";
	
	/** 安全错误  */
	public static String SECURITY_ERROR = "SECURITY_ERROR";
	
	/**
	 * 错误码
	 */
	private String errorNo;
	
	/**
	 * 发生错误的调用的服务ID
	 */
	private String serviceId;
	/**
	 * 发生错误的方法名称
	 */
	private String method;
	
	public WebServiceException(String errorNo) {
		super();
		this.errorNo = errorNo;
	}

	public WebServiceException(String errorNo,String message, Throwable cause) {
		super(message, cause);
		this.errorNo = errorNo;
	}

	public WebServiceException(String errorNo,String message) {
		super(message);
		this.errorNo = errorNo;
	}

	public WebServiceException(Throwable cause) {
		super(cause);
		this.errorNo = UNKNOW_ERROR;
	}
	
	public WebServiceException(String errorNo,String message, String serviceId, String method,Throwable cause) {
		super(message,cause);
		this.errorNo = errorNo;
		this.serviceId = serviceId;
		this.method = method;
	}

	public WebServiceException(String errorNo,String message, String serviceId, String method) {
		super(message);
		this.errorNo = errorNo;
		this.serviceId = serviceId;
		this.method = method;
	}
	
	public String getErrorNo() {
		return errorNo;
	}
	
	public String getServiceId() {
		return serviceId;
	}

	public String getMethod() {
		return method;
	}

	public String toString() {
		return "WebServiceException [errorNo=" + errorNo +", message="+getMessage() + (serviceId == null ? "" : ", serviceId="+serviceId) + (method == null ? "" : ", method="+method) + "]";
	}
	
	
}
