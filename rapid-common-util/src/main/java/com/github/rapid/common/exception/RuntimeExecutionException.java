package com.github.rapid.common.exception;

/**
 * for replace java.util.concurrent.ExecutionException
 */
public class RuntimeExecutionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RuntimeExecutionException() {
		super();
	}

	public RuntimeExecutionException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RuntimeExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuntimeExecutionException(String message) {
		super(message);
	}

	public RuntimeExecutionException(Throwable cause) {
		super(cause);
	}

}
