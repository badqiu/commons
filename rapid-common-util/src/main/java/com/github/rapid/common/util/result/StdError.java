package com.github.rapid.common.util.result;

public class StdError {
    final int errorCode;
    final String errorMsg;

    StdError(int code, String msg) {
        errorCode = code;
        errorMsg = msg;
    }

	public int getErrorCode() {
		return errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}
    
    
}
