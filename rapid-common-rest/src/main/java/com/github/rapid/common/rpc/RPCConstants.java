package com.github.rapid.common.rpc;

public class RPCConstants {
	/**
	 * jsonp回调key
	 */
	public final static String JSONCALLBACK_KEY = "__jsoncallback";
	
	/**
	 * 是否包装返回结果(true则返回 RPCResponse,false直接返回结果),默认值是false
	 */
	public static final String KEY_NO_WRAP = "__noWrapResult"; 
	
	
	public static final String KEY_FORMAT = "__format"; // 协议返回值格式: json,xml
	public static final String KEY_PROTOCOL = "__protocol"; // 通讯协议,用于决定参数的传递方式 
	public static final String KEY_PARAMETERS = "__params"; // 参数值列表
	
	// 参数协议类型
	public static final String PROTOCOL_KEYVALUE = "kv"; // param1Name=param2Value&param2Name=param2Value
	public static final String PROTOCOL_ARRAY = "array"; // __params=param1Value;param2Value
	public static final String PROTOCOL_JSON = "json";  //  __params=["param1Value",param2Value]
	public static final String PROTOCOL_JAVA = "java";  //  __params=["param1Value",param2Value]
	
	public static final String NULL_VALUE = "null";
	public static final String UNDEFINED_VALUE = "undefined";
	public static final String NaN_VALUE = "NaN";
	
}
