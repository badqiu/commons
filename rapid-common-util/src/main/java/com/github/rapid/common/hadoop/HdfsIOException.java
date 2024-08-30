package com.github.rapid.common.hadoop;
/**
 * @author badqiu
 */
public class HdfsIOException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public HdfsIOException() {
		super();
	}

	public HdfsIOException(String message, Throwable cause) {
		super(message, cause);
	}

	public HdfsIOException(String message) {
		super(message);
	}

	public HdfsIOException(Throwable cause) {
		super(cause);
	}
	
}
