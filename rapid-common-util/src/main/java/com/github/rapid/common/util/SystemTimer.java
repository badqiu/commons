package com.github.rapid.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 主要功能: 通过缓存系统时间,提升System.currentTimeMillis()的效率
 * 通过设置时间刷新间隔可以提升系统时间精度
 * 
 * 应用场景：耗时统计，timeout时间计算
 *
 * 性能对比数据: 
 * [totalCost:589ms, all:5.7%, loopCount:1,000,000,000, TPS:1,697,792,869] - SystemTimer.currentTimeMillis
 * [totalCost:4,534ms, all:43.9%, loopCount:1,000,000,000, TPS:220,555,800] - System.currentTimeMillis
 * 
 * 性能提升7.6倍
 * 
 * 
 * 
 * @author badqiu
 *
 */
public class SystemTimer {
	private static Logger logger = LoggerFactory.getLogger(SystemTimer.class);
	
	private static int refreshTimeInterval = 50;
	
	private static volatile long cachedCurrentTimeMillis = System.currentTimeMillis();
	
	/**
	 * 设置时间刷新间隔
	 * @param refreshInterval
	 */
	public static void setRefreshTimeInterval(int refreshInterval) {
		if(refreshInterval <= 0) {
			throw new IllegalArgumentException("refreshInterval > 0 must be true");
		}
		SystemTimer.refreshTimeInterval = refreshInterval;
	}
	
	/**
	 * 得到系统当前时间
	 * @return
	 */
	public static long currentTimeMillis() {
		return cachedCurrentTimeMillis;
	}
	
	static Thread systemTimerFreshThread = new Thread("SystemTimerFresh") {
		@Override
		public void run() {
			logger.info("SystemTimer refresh thread started,refreshTimeInterval:"+refreshTimeInterval);
			while(true) {
				cachedCurrentTimeMillis = System.currentTimeMillis();
				try {
					Thread.sleep(refreshTimeInterval);
				} catch (InterruptedException e) {
					logger.info("WARN SystemTimer refresh thread exit by InterruptedException",e);
					throw new RuntimeException(e);
				}
			}
		}
	};
	
	static {
		systemTimerFreshThread.setDaemon(true);
		systemTimerFreshThread.setPriority(6);
		systemTimerFreshThread.start();
	}
	
}
