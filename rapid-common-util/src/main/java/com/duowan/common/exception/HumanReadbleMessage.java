package com.duowan.common.exception;
/**
 * 
 * 用于异常实现的接口,
 * 该异常的message将用于直接展示信息给用户。
 * message信息必须是可以直接展示给用户才可以抛出该异常
 * 
 * @author badqiu
 *
 */
public interface HumanReadbleMessage {

	public String getMessage();
	
}
