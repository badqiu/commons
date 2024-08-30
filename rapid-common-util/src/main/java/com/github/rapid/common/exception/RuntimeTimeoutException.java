package com.github.rapid.common.exception;

/**
 * for replace java.util.concurrent.TimeoutException
 */
public class RuntimeTimeoutException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RuntimeTimeoutException() {
		super();
	}

	public RuntimeTimeoutException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RuntimeTimeoutException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuntimeTimeoutException(String message) {
		super(message);
	}

	public RuntimeTimeoutException(Throwable cause) {
		super(cause);
	}

}
