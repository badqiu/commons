package com.github.rapid.common.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD,ElementType.TYPE,ElementType.CONSTRUCTOR,ElementType.PACKAGE})
public @interface Profiled {
	/**
	 * Profiler message消息,message可以引用方法的参数,语法采用String.format()
	 * 
	 * 方法: public void executeTask(String taskId,Date taskDate)
	 * 示例: executeTask.%s
	 * 
	 * @return
	 */
	String message() default "";
	/**
	 * Profiler loopCount的参数位置下标,下标从 0 开始
	 * @return
	 */
	int loopCountParamIndex() default -1;
}
