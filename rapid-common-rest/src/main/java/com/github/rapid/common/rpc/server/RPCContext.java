package com.github.rapid.common.rpc.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * RPC应用上下文，可以通过ThreadLocal的方式得到HttpServletRequest,HttpServletResponse
 * @author badqiu
 *
 */
public class RPCContext {
	
	private static ThreadLocal<HttpServletRequest> requestContext = new ThreadLocal<HttpServletRequest>();
	private static ThreadLocal<HttpServletResponse> responseContext = new ThreadLocal<HttpServletResponse>();
	private static ThreadLocal<String> msgContext = new ThreadLocal<String>();
	
	public static HttpServletRequest getRequest() {
		return requestContext.get();
	}
	
	public static HttpServletResponse getResponse() {
		return responseContext.get();
	}
	
	public static String getMessage() {
		return msgContext.get();
	}
	
	public static void setRequest(HttpServletRequest request) {
		requestContext.set(request);
	}
	
	public static void setResponse(HttpServletResponse response) {
		responseContext.set(response);
	}
	
	public static void setMessage(String msg) {
		msgContext.set(msg);
	}
	
	public static void clear() {
		requestContext.set(null);
		responseContext.set(null);
		msgContext.set(null);
	}
	
}
