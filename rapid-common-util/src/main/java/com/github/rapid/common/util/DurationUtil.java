package com.github.rapid.common.util;

import java.time.Duration;
import java.time.format.DateTimeParseException;

import org.apache.commons.lang3.StringUtils;

public class DurationUtil {

	public static Duration parseDuration(String value) {
		if(StringUtils.isBlank(value)) return null;
		
		value = value.trim();
		
		String text = null;
		try {
			text = toIsoFormatDurationText(value);
			Duration duration = Duration.parse(text);
			return duration;
		}catch(DateTimeParseException e) {
			throw new IllegalArgumentException("parse error,input:"+value+ " ios format_text:"+text+" exception:"+e,e);
		}
	}

	private static String toIsoFormatDurationText(String value) {
		String valueLower = value.toLowerCase();
		String text = valueLower;
		boolean isIsoFormat = valueLower.contains("p");
		if (isIsoFormat) {
			text = value;
		} else {
			if(valueLower.contains("t")) {
				text = "P"+value;
			}else {
				text = "PT"+value;
			}
		}
		
		
		return text;
	}

}
