package com.github.rapid.common.web.util;

import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

import com.github.rapid.common.exception.MessageException;
import com.github.rapid.common.util.ValidationErrorsUtil;
import com.github.rapid.common.web.scope.Flash;

import jakarta.validation.ConstraintViolationException;

public class ControllerUtil {

	private static Logger logger = LoggerFactory.getLogger(ControllerUtil.class);
	
	public static <T> boolean execForErrors(Consumer<T> consumer,T entity,BindingResult errors) {
		try {
			consumer.accept(entity);
		}catch(ConstraintViolationException e) {
			ValidationErrorsUtil.convert(e, errors);
			return  false;
		}catch(MessageException e) {
			Flash.current().error(e.getMessage());
			return  false;
		}
		return true;
	}
	
	/**
	 * 用items循环执行Consumer,执行成功数及失败数
	 * @param items
	 * @param consumer
	 */
	public static <T> BatchStat execForCounts(Consumer<T> consumer,List<T> items) {
		int successCount = 0;
		int errorCount = 0;
		Exception lastException = null;
		for(T item : items) {
			if(item == null) continue;
			
			try {
				consumer.accept(item);
				successCount++;
			}catch(Exception e) {
				errorCount++;
				logger.info("accept_error,item:"+item,e);
				lastException = e;
			}
		}
		
		BatchStat batchStat = new BatchStat(successCount,errorCount);
		batchStat.setLastException(lastException);
		
		return batchStat;
	}
	
	public static class BatchStat {
		int successCount;
		int errorCount;
		Exception lastException;
		
		public BatchStat() {
		}
		
		public BatchStat(int successCount, int errorCount) {
			this.successCount = successCount;
			this.errorCount = errorCount;
		}

		public int getSuccessCount() {
			return successCount;
		}
		
		public void setSuccessCount(int successCount) {
			this.successCount = successCount;
		}
		
		public int getErrorCount() {
			return errorCount;
		}
		
		public void setErrorCount(int errorCount) {
			this.errorCount = errorCount;
		}

		public Exception getLastException() {
			return lastException;
		}

		public void setLastException(Exception lastException) {
			this.lastException = lastException;
		}
		
	}
	
}
