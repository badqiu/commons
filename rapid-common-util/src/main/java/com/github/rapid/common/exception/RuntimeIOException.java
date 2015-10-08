package com.github.rapid.common.exception;

public class RuntimeIOException extends RuntimeException {

	private static final long serialVersionUID = 6967398691592484191L;

	public RuntimeIOException() {
		super();
	}
	
	public RuntimeIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public RuntimeIOException(String message) {
		super(message);
	}

	public RuntimeIOException(Throwable cause) {
		super(cause);
	}
	
}
