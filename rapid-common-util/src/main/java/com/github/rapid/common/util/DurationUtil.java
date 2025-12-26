package com.github.rapid.common.util;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.format.DateTimeParseException;

import org.apache.commons.lang3.StringUtils;

public class DurationUtil {

	/**
	 * 可以解析数据: 5.1s=5.1秒, 1m1s=61秒 2d1h20m1s=2天1小时20分1秒
	 * 
	 * @param value
	 * @return
	 */
	public static Duration parseDuration(String value) {
		if(StringUtils.isBlank(value)) return null;
		
		value = value.trim();
		
		String isoFormatText = null;
		try {
			isoFormatText = toIsoFormatDurationText(value);
			Duration duration = Duration.parse(isoFormatText);
			return duration;
		}catch(DateTimeParseException e) {
			throw new DateTimeException("parse error,input:"+value+ " isoFormatText:"+isoFormatText+" exception:"+e,e);
		}
	}

	/**
	 * 变成ISO格式。 日期要有前缀P， 时间部分用前缀T
	 * @param value
	 * @return
	 */
	private static String toIsoFormatDurationText(String value) {
		String valueLower = value.toLowerCase();
		String text = valueLower;
		boolean isIsoFormat = valueLower.contains("p");
		if (isIsoFormat) {
			return value;
		} 
		if(valueLower.contains("t")) {
			return "P"+value;
		}
		
		//有时间部分，需要T
		if(valueLower.contains("s") || valueLower.contains("m")  || valueLower.contains("h") ) {
			return "PT"+value;
		}
		
		//P覆盖日期
		return "P"+value;
		
	}

}
