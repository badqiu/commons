package com.github.rapid.common.util;

import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.MDC;

/**
 * 系统运行时打印方便调试与追踪信息的工具类.
 * 
 * 使用MDC存储traceID, 一次trace中所有日志都自动带有该ID,
 * 可以方便的用grep命令在日志文件中提取该trace的所有日志.
 * 
 * 需要在log4j.properties中将ConversionPattern添加%X{traceId},如:
 * log4j.appender.stdout.layout.ConversionPattern=%d [%c] %X{traceId}-%m%n
 * 
 * @authro badqiu
 */
public class LogTraceUtil {

	public static final String TRACE_ID_KEY = "traceId";

	/**
	 * 开始Trace, 如果已经存在该traceId则返回,不存在则生成UUID并放入MDC.
	 * @return traceId
	 */
	public static String beginTrace() {
		return beginTrace(null);
	}
	
	/**
	 * 开始Trace, 如果已经存在该traceId则返回,不存在则生成UUID并放入MDC.
	 * @return traceId
	 */
	public static String beginTrace(String traceId) {
		if(traceId == null) {
			traceId = (String)MDC.get(TRACE_ID_KEY);
		}
		if(traceId == null) {
			traceId = newTraceId();
			MDC.put(TRACE_ID_KEY, traceId);
		}
		return traceId;
	}

	public static String newTraceId() {
		String uuid = StringUtils.remove(UUID.randomUUID().toString(),'-');
		String date = DateFormatUtils.format(new Date(), "MMddHHmmss");
		return date + uuid;
	}

	/**
	 * 结束一次Trace.
	 * 清除traceId.
	 */
	public static void endTrace() {
		MDC.remove(TRACE_ID_KEY);
	}
	
	public static String getTraceId() {
		return MDC.get(TRACE_ID_KEY);
	}
	
	/**
	 * 结束一次Trace.
	 * 清除traceId.
	 */
	public static void trace(Runnable cmd) {
		beginTrace();
		try {
			cmd.run();
		}finally {
			endTrace();
		}
	}
}