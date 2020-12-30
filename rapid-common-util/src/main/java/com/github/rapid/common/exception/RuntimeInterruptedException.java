package com.github.rapid.common.exception;

/**
 * 运行时异常,等同于: InterruptedException
 * 
 * @author badqiu
 *
 */
public class RuntimeInterruptedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RuntimeInterruptedException() {
		super();
	}

	public RuntimeInterruptedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RuntimeInterruptedException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuntimeInterruptedException(String message) {
		super(message);
	}

	public RuntimeInterruptedException(Throwable cause) {
		super(cause);
	}

}
