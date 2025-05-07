package com.github.rapid.common.util.result;


/**
 * 本类在关注性能时使用，用于代替异常时使用
 * 
 * @param <TResult>
 * @param <TError>
 */
public class StdResult<TResult, TError> {
	private static String DEFAULT_ERROR_MSG = "StdResult has error, check getError()";
	
    private final TResult result;
    private final TError error;
    private final String errorMsg;
    private final boolean success;

    StdResult(TResult result, TError error, boolean success,String errorMsg) {
        this.result = result;
        this.error = error;
        this.success = success;
        this.errorMsg = errorMsg;
    }

    // 构造成功结果
    public static <TResult, TError> StdResult<TResult, TError> success(TResult result) {
        return new StdResult<>(result, null, true,null);
    }

    // 构造错误结果
    public static <TResult, TError> StdResult<TResult, TError> error(TError error,String errorMsg) {
        return new StdResult<>(null, error, false,errorMsg);
    }
    
    public static <TResult, TError> StdResult<TResult, TError> error(TError error) {
        return error(error,DEFAULT_ERROR_MSG);
    }

    // 直接判断成功
    public boolean isSuccess() {
        return success;
    }
    
    public boolean isError() {
        return !isSuccess();
    }

    public TError getError() {
        return error;
    }
    
    public String getErrorMsg() {
		return errorMsg;
	}

	public void throwExceptionIfError() {
    	if (isError()) {
            throw new StdResultException(
                error,
                errorMsg
            );
        }
    }
    
    public TResult getResultOrThrowException() {
    	throwExceptionIfError();
        return result;
    }

    public TResult getResultOrDefault(TResult defaultValue) {
    	if(isError()) {
    		return defaultValue;
    	}
        return result == null ? defaultValue : result;
    }


}