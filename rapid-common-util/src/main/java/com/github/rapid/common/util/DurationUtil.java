package com.github.rapid.common.util;

import java.time.DateTimeException;
import java.time.Duration;
import java.time.format.DateTimeParseException;

import org.apache.commons.lang3.StringUtils;

public class DurationUtil {

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
