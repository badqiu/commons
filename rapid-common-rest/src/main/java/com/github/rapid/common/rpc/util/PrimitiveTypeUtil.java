package com.github.rapid.common.rpc.util;

public class PrimitiveTypeUtil {
	
	public static Object parsePrimitiveValue(Class<?> parameterType,String s) {
		if (parameterType.equals(int.class)) {
			return Integer.parseInt(s);
		} else if (parameterType.equals(long.class)) {
			return Long.parseLong(s);
		} else if (parameterType.equals(float.class)) {
			return Float.parseFloat(s);
		} else if (parameterType.equals(double.class)) {
			return Double.parseDouble(s);
		} else if (parameterType.equals(char.class)) {
			return s.charAt(0);
		} else if (parameterType.equals(boolean.class)) {
			return Boolean.parseBoolean(s);
		} else if (parameterType.equals(byte.class)) {
			return Byte.parseByte(s);
		} else if (parameterType.equals(short.class)) {
			return Short.parseShort(s);
		} else {
			throw new IllegalArgumentException(parameterType.toString());
		}
	}
	
	public static Object getPrimitiveDefaultValue(Class<?> parameterType) {
		if (parameterType.equals(int.class)) {
			return 0;
		} else if (parameterType.equals(long.class)) {
			return 0L;
		} else if (parameterType.equals(float.class)) {
			return 0f;
		} else if (parameterType.equals(double.class)) {
			return 0d;
		} else if (parameterType.equals(char.class)) {
			return (char) 0;
		} else if (parameterType.equals(boolean.class)) {
			return false;
		} else if (parameterType.equals(byte.class)) {
			return (byte) 0;
		} else if (parameterType.equals(short.class)) {
			return (short) 0;
		} else {
			throw new IllegalArgumentException(parameterType.toString());
		}
	}
}