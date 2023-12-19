package com.github.rapid.common.util;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class NotifyUtil {

	private static Map<String,Long> notifyInterval = new HashMap<String,Long>();
	
	/**
	 * 避免发送太多相同消息
	 * @param notifyIntervalKey 判断相同消息的key
	 * @param intervalSeconds 间隔秒数
	 * @return
	 */
	public static synchronized boolean isTooManySameNotify(String notifyIntervalKey,long intervalMills) {
		Long lastNotifyTime = notifyInterval.get(notifyIntervalKey);
		
		long currentTimeMillis = System.currentTimeMillis();
		if(lastNotifyTime == null) {
			notifyInterval.put(notifyIntervalKey, currentTimeMillis);
			return false;
		}
		
		long costIntervalMills = currentTimeMillis - lastNotifyTime;
		if(costIntervalMills <= intervalMills) {
			return true;
		}
		
		notifyInterval.put(notifyIntervalKey, currentTimeMillis);
		return false;
	}
	
	public static synchronized boolean isTooManySameNotify(String notifyIntervalKey,Duration intervalDuration) {
		return isTooManySameNotify(notifyIntervalKey,intervalDuration.toMillis());
	}
	
}
