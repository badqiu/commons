package com.github.rapid.common.log;

import java.util.Date;
import java.util.Map;

/**
 * 标准化日志，覆盖5W: 何时，何地，何人，何事，何因
 * 
 * @author badqiu
 *
 */
public class LoggerMsg {
	private String eventType; // 错误事件类型,如Project
	private String eventAction; // 错误事件动作,如 update,delete,write,read,run
	private String message; // 错误摘要信息
	private String fullLog; // 错误完整日志
	private Throwable exception; // 错误原因
	private String operator; // 错误操作人
	//private Map params; //参数？？
	
	private long loopCount; // 循环执行大小，一般用于计算TPS
	private long resultSize; // 结果集大小
	private Date startTime = new Date(); // 开始时间
	private long duration; // 时长
	
	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getEventAction() {
		return eventAction;
	}

	public void setEventAction(String eventAction) {
		this.eventAction = eventAction;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFullLog() {
		return fullLog;
	}

	public void setFullLog(String fullLog) {
		this.fullLog = fullLog;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public long getLoopCount() {
		return loopCount;
	}

	public void setLoopCount(long loopCount) {
		this.loopCount = loopCount;
	}

	public long getResultSize() {
		return resultSize;
	}

	public void setResultSize(long resultSize) {
		this.resultSize = resultSize;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	public LoggerMsg eventType(Class eventType) {
		this.eventType = eventType.getSimpleName();
		return this;
	}
	public LoggerMsg eventType(String eventType) {
		this.eventType = eventType;
		return this;
	}
	public LoggerMsg eventAction(String eventAction) {
		this.eventAction = eventAction;
		return this;
	}
	public LoggerMsg message(String message) {
		this.message = message;
		return this;
	}
	public LoggerMsg fullLog(String fullLog) {
		this.fullLog = fullLog;
		return this;
	}
	public LoggerMsg exception(Throwable exception) {
		this.exception = exception;
		return this;
	}
	public LoggerMsg operator(String operator) {
		this.operator = operator;
		return this;
	}
	public LoggerMsg loopCount(long loopCount) {
		this.loopCount = loopCount;
		return this;
	}
	public LoggerMsg resultSize(long resultSize) {
		this.resultSize = resultSize;
		return this;
	}
	public LoggerMsg startTime(Date startTime) {
		this.startTime = startTime;
		return this;
	}
	public LoggerMsg duration(long duration) {
		this.duration = duration;
		return this;
	}

	

}
