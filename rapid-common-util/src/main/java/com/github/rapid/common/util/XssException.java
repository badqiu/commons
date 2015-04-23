package com.github.rapid.common.util;
/**
 * XSS攻击异常
 * 
 * @author badqiu
 *
 */
public class XssException extends SecurityException{

	private static final long serialVersionUID = 7016967341270813085L;

	public XssException() {
		super();
	}

	public XssException(String message, Throwable cause) {
		super(message, cause);
	}

	public XssException(String s) {
		super(s);
	}

	public XssException(Throwable cause) {
		super(cause);
	}
	
	
}
