package com.github.rapid.common.util;

import java.text.DecimalFormat;

public class SmartFormat {
	static DecimalFormat format5 = new DecimalFormat("#.#####");
	static DecimalFormat format4 = new DecimalFormat("#.####");
	static DecimalFormat format3 = new DecimalFormat("#.###");
	static DecimalFormat format2 = new DecimalFormat("#.##");
	static DecimalFormat format1 = new DecimalFormat("#.#");
	static DecimalFormat format0 = new DecimalFormat("#");

	public static String format(Long value) {
		if(value == null) return "";
		return format(value.doubleValue());
	}
	
	public static String format(long value) {
		return format((double)value);
	}
	
	public static String format(Double value) {
		if(value == null) return "";
		return format(value.doubleValue());
	}
	
	public static String format(double value) {
		if(value > 1000) {
			return format0.format(value);
		}else if(value > 100) {
			return format1.format(value);
		}else if(value > 10) {
			return format2.format(value);
		}else if(value > 1) {
			return format3.format(value);
		}else if(value > 0.1) {
			return format4.format(value);
		}else if(value > 0.01) {
			return format4.format(value);
		}else if(value > 0.001) {
			return format5.format(value);			
		}else {
			return String.valueOf(value);
		}
	}
}
