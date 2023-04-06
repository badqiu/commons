package com.github.rapid.common.util;

import java.util.Date;

public class DateUtil {
	
	/** 是否超时下线 */
	public static boolean isOfflineTimeout(Date offlineTime) {
		if(offlineTime == null) return false;
		
		Date now = new Date();
		if(offlineTime.before(now) || offlineTime.equals(now)) return true;
		
		return false;
	}
	
}
