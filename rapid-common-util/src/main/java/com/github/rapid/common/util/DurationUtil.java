package com.github.rapid.common.util;

import java.time.Duration;
import java.time.format.DateTimeParseException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class DurationUtil {

	public static Duration parseDuration(String value) {
		if(StringUtils.isBlank(value)) return null;
		
		value = value.trim();
		
		try {
			String text = toIsoFormatDurationText(value);
			Duration duration = Duration.parse(text);
			return duration;
		}catch(DateTimeParseException e) {
			throw new DateTimeParseException("parse error,value:"+value+" exception:"+e, value, 0);
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
