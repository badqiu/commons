package com.github.rapid.common.web.util;

import java.util.List;
import java.util.function.Consumer;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;

import com.github.rapid.common.exception.MessageException;
import com.github.rapid.common.util.ValidationErrorsUtil;
import com.github.rapid.common.web.scope.Flash;

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
	 * 用items循环执行Consumer,执行成功数及失败数: [successCount,errorCount]
	 * @param items
	 * @param consumer
	 * @return [successCount,errorCount]
	 */
	public static <T> int[] execForCounts(Consumer<T> consumer,List<T> items) {
		int successCount = 0;
		int errorCount = 0;
		for(T item : items) {
			if(item == null) continue;
			
			try {
				consumer.accept(item);
				successCount++;
			}catch(Exception e) {
				errorCount++;
				logger.info("accept_error,item:"+item,e);
			}
		}
		
		return new int[]{successCount,errorCount};
	}
	
}
