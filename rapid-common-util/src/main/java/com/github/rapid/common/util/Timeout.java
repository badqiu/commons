package com.github.rapid.common.util;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * 判断是否超时的工具类
 * 
 * @author badqiu
 *
 */
public class Timeout {

	private long lastTime = 0;
	
	public Timeout() {
		lastTime = SystemTimer.currentTimeMillis();
	}
	
	public Timeout(long lastTime) {
		this.lastTime = lastTime;
	}
	
	public void updateLastTime() {
		lastTime = SystemTimer.currentTimeMillis();
	}
	
	public boolean isTimeout(long duration,TimeUnit timeUnit) {
		return isTimeoutByTimeMills(timeUnit.toMillis(duration));
	}
	
	public boolean isTimeout(Duration duration) {
		return isTimeoutByTimeMills(duration.toMillis());
	}

	private boolean isTimeoutByTimeMills(long durationMills) {
		long interval = SystemTimer.currentTimeMillis() - lastTime;
		return interval >= durationMills;
	}
	
}
