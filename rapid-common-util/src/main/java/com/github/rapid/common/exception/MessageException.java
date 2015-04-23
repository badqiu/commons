package com.github.rapid.common.exception;
/**
 * 通用异常:
 * 抛出该类的异常信息直接展示给用户显示
 * 
 * @author badqiu
 *
 */
public class MessageException extends RuntimeException implements HumanReadbleMessage {

	private static final long serialVersionUID = 4747783786739591192L;

	public MessageException() {
		super();
	}

	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

	public MessageException(String message) {
		super(message);
	}

	public MessageException(Throwable cause) {
		super(cause);
	}

}
