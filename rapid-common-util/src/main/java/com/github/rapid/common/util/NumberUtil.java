package com.github.rapid.common.util;

import org.apache.commons.lang3.StringUtils;

public class NumberUtil {

	public static Number toNumber(Object v) {
		return toNumber(v,false);
	}
	
	public static Number toNumber(Object v,Number defaultValue) {
		Number r = toNumber(v,false);
		if(r == null) {
			return defaultValue;
		}
		return r;
	}
	
	public static Number toNumber(Object v,boolean extractNumberFromString) {
		if(v == null) {
			return null;
		}else if(v instanceof Number) {
			return (Number)v;
		}else if(v instanceof String) {
			String str = (String)v;
			return toNumberFromString(str, extractNumberFromString);
		}else {
			return toNumberFromString(v.toString(), extractNumberFromString);
		}
	}

	private static Number toNumberFromString(String str, boolean extractNumberFromString) {
		if(StringUtils.isBlank(str)) {
			return null;
		}
		
		if(extractNumberFromString) {
			str = extractNumber(str);
		}
		
		if(StringUtils.isBlank(str)) {
			return null;
		}
		return Double.parseDouble(str);
	}
	
    /**
     * 提取字符串中的第一个出现的数字（支持负号和小数点）并返回作为字符串
     * @param str 输入的字符串
     * @return 第一个出现的数字作为字符串，如果没有数字则返回null
     */
    public static String extractNumber(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }

        StringBuilder number = new StringBuilder(str.length());
        boolean foundNumber = false;
        boolean hasDot = false;
        boolean hasNegativeSign = false;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);

            // 处理负号
            if (c == '-' && !foundNumber && !hasNegativeSign) {
                number.append(c);
                hasNegativeSign = true;
                continue;
            }

            // 处理数字
            if (Character.isDigit(c)) {
                number.append(c);
                foundNumber = true;
            } else if (c == '.' && !hasDot) {
                // 处理小数点
                number.append(c);
                hasDot = true;
                foundNumber = true;
            } else if (foundNumber) {
                // 一旦找到过数字并且遇到了非数字和非小数点字符，结束记录
                break;
            }
        }

        // 如果只有负号部分（例如："-"），或者没有数字
        if (number.length() == 0 || (number.length() == 1 && number.charAt(0) == '-')) {
            return null;
        }

        return number.toString();
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
	
	public static int toInt(Object v) {
		return toInteger(v,0);
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
