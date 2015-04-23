package com.github.rapid.common.rpc;

/**
 * 序列化异常类
 * 
 * @author shixiangwen
 * 
 */
public class SerializeException extends RuntimeException {

	private static final long serialVersionUID = 1640412385677081247L;

	public SerializeException() {
		super();
	}

	public SerializeException(String message, Throwable cause) {
		super(message, cause);
	}

	public SerializeException(String message) {
		super(message);
	}

	public SerializeException(Throwable cause) {
		super(cause);
	}


}
