package com.github.rapid.common.rpc.client;

import java.util.HashMap;
import java.util.Map;

public class RPCClientContext {

	private static Map<String,String> globalParams = new HashMap<String,String>();
	private static ThreadLocal<Map<String,String>> paramsContext = new ThreadLocal<Map<String,String>>(); 
	
	public static Map<String,String> getGlobalParams() {
		return globalParams;
	}

	public static void clearParams() {
		paramsContext.set(null);
	}
	
	public static Map<String,String> getParams() {
		Map<String,String> result = paramsContext.get();
		if(result == null) {
			result = new HashMap<String,String>();
			setParams(result);
		}
		return result;
	}
	
	static Map<String,String> getOrNull() {
		return paramsContext.get();
	}

	public static void setParams(Map<String, String> result) {
		paramsContext.set(result);
	}
	
	public static void putParam(String key,String value) {
		getParams().put(key, value);
	}
	
}
