package com.github.rapid.common.util;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 判断是否超时的工具类
 * 
 * @author badqiu
 *
 */
public class Timeout {

	private long lastTime = 0; //最后更新时间
	private long timeoutDuration; //超时时间
	
	public Timeout() {
		updateLastTime();
	}
	
	public Timeout(Duration timeoutDuration) {
		this(timeoutDuration,systemCurrentTimeMillis());
	}

	public Timeout(Duration timeoutDuration,long lastTime) {
		Objects.requireNonNull(timeoutDuration,"timeoutDuration must be not null");
		this.timeoutDuration = timeoutDuration.toMillis();
		this.lastTime = lastTime;
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}
	
	public long updateLastTime() {
		lastTime = systemCurrentTimeMillis();
		return lastTime;
	}

	public boolean isTimeout(long duration,TimeUnit timeUnit) {
		return isTimeoutByTimeMills(timeUnit.toMillis(duration));
	}
	
	public boolean isTimeout(Duration duration) {
		return isTimeoutByTimeMills(duration.toMillis());
	}
	
	public boolean isTimeout() {
		return isTimeoutByTimeMills(timeoutDuration);
	}
	
	public boolean isTimeoutAfterUpdateLastTime() {
		Objects.requireNonNull(timeoutDuration,"timeoutDuration must be not null");
		
		boolean result = isTimeoutByTimeMills(timeoutDuration);
		if(result) {
			updateLastTime();
		}
		return result;
	}

	private boolean isTimeoutByTimeMills(long durationMills) {
		long interval = systemCurrentTimeMillis() - lastTime;
		return Math.abs(interval) >= durationMills;
	}
	
	private static long systemCurrentTimeMillis() {
		return SystemTimer.currentTimeMillis();
	}
}
