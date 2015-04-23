package com.duowan.common.rpc.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * 用于生成示例值
 * @author badqiu
 *
 */
public class SampleValueUtil {
	
	public static List<Object> getSampleValue(Class[] types) {
		List<Object> result = new ArrayList(types.length);
		for(Class type : types) {
			result.add(getSampleValue(type));
		}
		return result;
	}
	
	public static Object getSampleValue(Class<?> parameterType) {
		if(parameterType.equals(String.class)) {
			return "string";
		}
		
		if (parameterType.equals(int.class) || parameterType.equals(Integer.class)) {
			return 0;
		} else if (parameterType.equals(long.class) || parameterType.equals(Long.class)) {
			return 0L;
		} else if (parameterType.equals(float.class) || parameterType.equals(Float.class)) {
			return 0f;
		} else if (parameterType.equals(double.class) || parameterType.equals(Double.class)) {
			return 0d;
		} else if (parameterType.equals(char.class) || parameterType.equals(Character.class)) {
			return (char) 0;
		} else if (parameterType.equals(boolean.class) || parameterType.equals(Boolean.class)) {
			return false;
		} else if (parameterType.equals(byte.class) || parameterType.equals(Byte.class)) {
			return (byte) 0;
		} else if (parameterType.equals(short.class) || parameterType.equals(Short.class)) {
			return (short) 0;
		} else if (Date.class.isAssignableFrom(parameterType)) {
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
			return df.format(new Date());			
		} else {
			return "null";
		}
	}
}
