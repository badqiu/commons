package com.github.rapid.common.util.result;

public class StdResultException extends RuntimeException {
	static final long serialVersionUID = 1;
	
    private final Object error;

    public StdResultException(Object error, String message) {
        super(message);
        this.error = error;
    }

    public Object getError() {
        return error;
    }
    
}

