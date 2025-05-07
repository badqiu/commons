package com.github.rapid.common.util.result;
	
public class StdResult<TResult, TError> {
    private final TResult result;
    private final TError error;
    private final boolean success;

    StdResult(TResult result, TError error, boolean success) {
        this.result = result;
        this.error = error;
        this.success = success;
    }

    // 构造成功结果
    public static <TResult, TError> StdResult<TResult, TError> success(TResult result) {
        return new StdResult<>(result, null, true);
    }

    // 构造错误结果
    public static <TResult, TError> StdResult<TResult, TError> error(TError error) {
        return new StdResult<>(null, error, false);
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
    
    public void throwExceptionIfError() {
    	if (isError()) {
            throw new StdResultException(
                error,
                "StdResult has error, check getError()"
            );
        }
    }
    
    // 获取结果或抛出异常
    public TResult getResultOrThrowException() {
    	throwExceptionIfError();
        return result;
    }

    // 获取结果或返回默认
    public TResult getResultOrDefault(TResult defaultValue) {
    	if(isError()) {
    		return defaultValue;
    	}
        return result == null ? defaultValue : result;
    }


}