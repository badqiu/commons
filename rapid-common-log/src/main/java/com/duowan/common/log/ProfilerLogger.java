package com.duowan.common.log;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.duowan.common.util.Profiler;
/**
 * 
 * 用于记录Profiler日志,
 * 通过Profiler.info()打印日志
 * 
 * @author badqiu
 *
 */
public class ProfilerLogger {
	private static String PROFILERLOGDIR_KEY = "profilerLogDir";
	
	private static Logger logger = LoggerFactory.getLogger(ProfilerLogger.class);
	private static Logger digestlogger = LoggerFactory.getLogger("profiler.digest");
	private static Logger dumpLogger = LoggerFactory.getLogger("profiler.dump");
	
	private static Properties properties = loadProperties();

	static {
		try {
			initLoggerConfig();
		}catch(Exception e) {
			logger.error("error on init ProfilerLogger",e);
		}
	}
	
	/**
	 * 设置摘要日志的输出目录
	 * @param dir
	 */
	public static void setProfilerLogDir(String dir) {
		if(dir == null) throw new IllegalArgumentException("profilerLogDir must be not null");
		properties.setProperty(PROFILERLOGDIR_KEY, dir);
		initLoggerConfig();
	}
	
	private static void initLoggerConfig() {
		logger.info("profilerLogDir="+properties.getProperty(PROFILERLOGDIR_KEY)+",you can override 'profilerLogDir' by System Properties");
		PropertyConfigurator.configure(properties);
	}

	private static Properties loadProperties() {
		Properties properties = new Properties();
		try {
			properties.load(ProfilerLogger.class.getResourceAsStream("/profiler-log4j.properties"));
		} catch (IOException e) {
			logger.error("load profiler-log4j.properties error",e);
		}
		properties.putAll(System.getProperties());
		properties.putAll(System.getenv());
		logger.info("loaded /profiler-log4j.properties for ProfilerLogger)");
		return properties;
	}
	
	/**
	 * 打印info级别日志
	 */
	public static void infoDigestLogAndDump() {
		infoDigestLog();
		infoDump();
	}

	public static void infoDump() {
		if(dumpLogger.isInfoEnabled()) {
			dumpLogger.info(Profiler.dump());
		}
	}

	public static void infoDigestLog() {
		infoDigestLog("");
	}
	
	public static void infoDigestLog(String appendDigestLog) {
		if(digestlogger.isInfoEnabled()) {
			digestlogger.info(ProfilerDigestLog.getDigestLog(appendDigestLog));
		}
	}
	
	public static boolean isInfoEnabled() {
		return dumpLogger.isInfoEnabled() || digestlogger.isInfoEnabled();
	}
	
}
