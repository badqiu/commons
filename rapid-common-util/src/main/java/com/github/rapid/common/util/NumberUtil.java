package com.github.rapid.common.util;

import org.apache.commons.lang3.StringUtils;

public class NumberUtil {

	public static Number toNumber(Object v) {
		if(v == null) {
			return null;
		}else if(v instanceof Number) {
			return (Number)v;
		}else if(v instanceof String) {
			String str = (String)v;
			if(StringUtils.isBlank(str)) {
				return null;
			}
			return Double.parseDouble(str);
		}else {
			String str = v.toString();
			return Double.parseDouble(str);
		}
	}
	
	public static Long toLong(Object v) {
		Number num = toNumber(v);
		return v == null ? null : num.longValue();
	}
	
	public static long toLong(Object v,long defaultValue) {
		Number num = toNumber(v);
		return v == null ? defaultValue : num.longValue();
	}
	
	public static Integer toInteger(Object v) {
		Number num = toNumber(v);
		return v == null ? null : num.intValue();
	}
	
	public static int toInteger(Object v,int defaultValue) {
		Number num = toNumber(v);
		return v == null ? defaultValue : num.intValue();
	}
	
	public static int toInt(Object v,int defaultValue) {
		return toInteger(v,defaultValue);
	}
	
	public static Double toDouble(Object v) {
		Number num = toNumber(v);
		return v == null ? null : num.doubleValue();
	}
	
	public static double toDouble(Object v,double defaultValue) {
		Number num = toNumber(v);
		return v == null ? defaultValue : num.doubleValue();
	}
	
	public static Float toFloat(Object v) {
		Number num = toNumber(v);
		return v == null ? null : num.floatValue();
	}
	
	public static float toFloat(Object v,float defaultValue) {
		Number num = toNumber(v);
		return v == null ? defaultValue : num.floatValue();
	}
	
	public static Short toShort(Object v) {
		Number num = toNumber(v);
		return v == null ? null : num.shortValue();
	}
	
	public static short toShort(Object v,short defaultValue) {
		Number num = toNumber(v);
		return v == null ? defaultValue : num.shortValue();
	}
	
	public static Byte toByte(Object v) {
		Number num = toNumber(v);
		return v == null ? null : num.byteValue();
	}

	public static byte toByte(Object v,byte defaultValue) {
		Number num = toNumber(v);
		return v == null ? defaultValue : num.byteValue();
	}
	
	public static Boolean toBoolean(Object v) {
		if(v == null) return null;
		if(v instanceof Boolean) return (Boolean)v;
		
		if(v instanceof Number) {
			Number num = (Number)v;
			return num.longValue() == 0 ? false : true;
		}
		
		String str = String.valueOf(v);
		return Boolean.parseBoolean(str);
	}
	
	public static boolean toBoolean(Object v,boolean defaultValue) {
		Boolean r = toBoolean(v);
		return r == null ? defaultValue : r;
	}
	
}
