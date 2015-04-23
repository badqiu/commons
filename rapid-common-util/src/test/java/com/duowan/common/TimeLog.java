package com.duowan.common;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 标准华的日志,用于记录耗时
 * @author badqiu
 *
 */
public class TimeLog {
	
	private static Logger log = LoggerFactory.getLogger(TimeLog.class);
	private static Map<String,Long> costMap = new HashMap();
	
	public static void eventStart(String event) {
		Long start = (Long)costMap.get(event);
		if(start == null) {
			start = System.currentTimeMillis();
			costMap.put(event,start);
		}
	}
	
	public static void eventEnd(String event) {
		costMap.remove(event);
		
		Long start = (Long)costMap.get(event);
		if(start == null) {
			long cost = System.currentTimeMillis() - start;
			log.info(event+","+cost);
		}
	}
	
}
