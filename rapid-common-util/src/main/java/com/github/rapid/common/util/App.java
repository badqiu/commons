package com.github.rapid.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于自动装载classpath:app.properties在类中.
 * 
 * app.properties使用UTF-8编码.
 * 
 * 单元测试过程中，可以使用 {@link App#setContextProperties(Properties)} 设置应用属性的上下文，避免多线程测试互相影响 
 * 
 * @author badqiu
 * @see #setContextProperties(Properties)
 * 
 */
@Deprecated
public class App {
	private static Logger logger = LoggerFactory.getLogger(App.class);
	private static PropertiesHelper properties;
	private static ThreadLocal<Properties> contextProperties = new ThreadLocal<Properties>();
	
	static {
		loadApplicationProperties();
	}

	private static void loadApplicationProperties() {
		Properties props = new Properties();
		props.putAll(load("app.properties"));
		properties = new PropertiesHelper(props);
	}

	private static Properties load(String resourceName) {
		Properties props = new Properties();
		InputStream input = null;
		try {
			input = getDefaultClassLoader().getResourceAsStream(resourceName);
			if(input == null) {
				logger.warn("not found classpath:"+resourceName);
			}else {
				props.load(new InputStreamReader(input,"UTF-8"));
				logger.info("load classpath:"+resourceName+" success ,props:"+props);
			}
		}catch(IOException e) {
			logger.error("load classpath:"+resourceName+" fail,error:"+e,e);
		}finally {
			IOUtils.closeQuietly(input);
		}
		return props;
	}
	
	private static ClassLoader getDefaultClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			// Cannot access thread context ClassLoader - falling back to system class loader...
		}
		if (cl == null) {
			// No thread context class loader -> use class loader of this class.
			cl = App.class.getClassLoader();
		}
		return cl;
	}
	
	public static void reload() {
		loadApplicationProperties();
	}
	
	/**
	 * 得到应用名称
	 * @return
	 */
	public static String getAppName() {
		return getPropertiesHelper().getProperty("appName");
	}

	/**
	 * 得到应用版本
	 * @return
	 */
	public static String getAppVersion() {
		return getPropertiesHelper().getProperty("appVersion");
	}

	/**
	 * 得到应用的当前模式,所有模式为:
	 * dev: 开发模式
	 * prod: 生产环境模式
	 * test: 测试模式
	 * perf: 性能测试环境模式
	 * 
	 * @return
	 */
	public static String getAppMode() {
		return getPropertiesHelper().getProperty("appMode");
	}
	
	/**
	 * 是否是开发模式
	 * @return
	 */
	public static boolean isDevMode() {
		return "dev".equals(getAppMode());
	}

	// public static boolean isPerfMode() {
	// return "perf".equals(getAppMode());
	// }
	//
	// public static boolean isTestMode() {
	// return "test".equals(getAppMode());
	// }
	//
	// public static boolean isProdMode() {
	// return "prod".equals(getAppMode());
	// }

	public static void setContextProperties(Properties props) {
		contextProperties.set(props);
	}
	
	public static void clearContextProperties() {
		contextProperties.set(null);
	}
	
	private static PropertiesHelper getPropertiesHelper() {
		Properties props = contextProperties.get();
		if(props == null) {
			return properties;
		}
		return new PropertiesHelper(props);
	}
	
	public static Properties getProperties() {
		return getPropertiesHelper().getProperties();
	}

	public static void setProperties(Properties props) {
		getPropertiesHelper().setProperties(props);
	}

	public static String getRequiredProperty(String key) throws IllegalStateException {
		return getPropertiesHelper().getRequiredProperty(key);
	}

	public static String getNullIfBlank(String key) {
		return getPropertiesHelper().getNullIfBlank(key);
	}

	public static String getNullIfEmpty(String key) {
		return getPropertiesHelper().getNullIfEmpty(key);
	}

	public static Integer getInteger(String key) {
		return getPropertiesHelper().getInteger(key);
	}

	public static int getInt(String key, int defaultValue) {
		return getPropertiesHelper().getInt(key, defaultValue);
	}

	public static int getRequiredInt(String key) throws IllegalStateException {
		return getPropertiesHelper().getRequiredInt(key);
	}

	public static Long getLong(String key) {
		return getPropertiesHelper().getLong(key);
	}

	public static long getLong(String key, long defaultValue) {
		return getPropertiesHelper().getLong(key, defaultValue);
	}

	public static long getRequiredLong(String key) throws IllegalStateException {
		return getPropertiesHelper().getRequiredLong(key);
	}

	public static Boolean getBoolean(String key) {
		return getPropertiesHelper().getBoolean(key);
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return getPropertiesHelper().getBoolean(key, defaultValue);
	}

	public static boolean getRequiredBoolean(String key) throws IllegalStateException {
		return getPropertiesHelper().getRequiredBoolean(key);
	}

	public static Float getFloat(String key) {
		return getPropertiesHelper().getFloat(key);
	}

	public static float getFloat(String key, float defaultValue) {
		return getPropertiesHelper().getFloat(key, defaultValue);
	}

	public static float getRequiredFloat(String key) throws IllegalStateException {
		return getPropertiesHelper().getRequiredFloat(key);
	}

	public static Double getDouble(String key) {
		return getPropertiesHelper().getDouble(key);
	}

	public static double getDouble(String key, double defaultValue) {
		return getPropertiesHelper().getDouble(key, defaultValue);
	}

	public static double getRequiredDouble(String key) throws IllegalStateException {
		return getPropertiesHelper().getRequiredDouble(key);
	}

	public static URL getURL(String key) throws IllegalArgumentException {
		return getPropertiesHelper().getURL(key);
	}

	public static Object getClassInstance(String key) throws IllegalArgumentException {
		return getPropertiesHelper().getClassInstance(key);
	}

	public static Object getClassInstance(String key, Object defaultinstance)
			throws IllegalArgumentException {
		return getPropertiesHelper().getClassInstance(key, defaultinstance);
	}

	public static String[] getStringArray(String key) {
		return getPropertiesHelper().getStringArray(key);
	}

	public static int[] getIntArray(String key) {
		return getPropertiesHelper().getIntArray(key);
	}

	public static Properties getStartsWithProperties(String prefix) {
		return getPropertiesHelper().getStartsWithProperties(prefix);
	}

	public static Object setProperty(String key, int value) {
		return getPropertiesHelper().setProperty(key, value);
	}

	public static Object setProperty(String key, long value) {
		return getPropertiesHelper().setProperty(key, value);
	}

	public static Object setProperty(String key, float value) {
		return getPropertiesHelper().setProperty(key, value);
	}

	public static Object setProperty(String key, double value) {
		return getPropertiesHelper().setProperty(key, value);
	}

	public static Object setProperty(String key, boolean value) {
		return getPropertiesHelper().setProperty(key, value);
	}

	public static String getProperty(String key, String defaultValue) {
		return getPropertiesHelper().getProperty(key, defaultValue);
	}

	public static String getProperty(String key) {
		return getPropertiesHelper().getProperty(key);
	}

	public static Object setProperty(String key, String value) {
		return getPropertiesHelper().setProperty(key, value);
	}

}
