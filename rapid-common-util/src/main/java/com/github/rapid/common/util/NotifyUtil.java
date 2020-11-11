package com.github.rapid.common.util;

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
	public static synchronized boolean isTooManySameNotify(String notifyIntervalKey,int intervalSeconds) {
		Long lastNotifyTime = notifyInterval.get(notifyIntervalKey);
		if(lastNotifyTime != null && (System.currentTimeMillis() - lastNotifyTime) < 1000 * intervalSeconds) {
			return true;
		}
		notifyInterval.put(notifyIntervalKey, System.currentTimeMillis());
		return false;
	}
	
}
