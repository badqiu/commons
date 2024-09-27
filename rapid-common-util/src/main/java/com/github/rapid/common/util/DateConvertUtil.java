package com.github.rapid.common.util;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.FastDateFormat;
/**
 * @author badqiu
 */
public class DateConvertUtil {
	
	public static java.util.Date parse(String dateString,String dateFormat) {
		return parse(dateString, dateFormat,java.util.Date.class);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends java.util.Date> T parse(String dateString,String dateFormat,Class<T> targetResultType) {
		if(StringUtils.isBlank(dateString))
			return null;
		FastDateFormat df = FastDateFormat.getInstance(dateFormat);
		try {
			long time = df.parse(dateString).getTime();
			java.util.Date t = targetResultType.getConstructor(long.class).newInstance(time);
			return (T)t;
		} catch (ParseException e) {
			String errorInfo = "cannot use dateformat:"+dateFormat+" parse datestring:"+dateString;
			throw new IllegalArgumentException(errorInfo,e);
		} catch (Exception e) {
			throw new IllegalArgumentException("error targetResultType:"+targetResultType.getName(),e);
		}
	}
	
	public static String format(java.util.Date date,String dateFormat) {
		 if(date == null)
			 return null;
		 return FastDateFormat.getInstance(dateFormat).format(date);
	}
	
	/**
	 * 根据datePattern抽取date的部分内容.示例如下:
	 * extract(date,"yyyyMMdd") 则只抽取日期部分的内容
	 * extract(date,"HHmmss") 则只抽取时间部分的内容
	 * 
	 * @return 
	 */
	public static Date extract(Date date,String dateFormat) throws IllegalArgumentException {
		if(dateFormat == null) throw new IllegalArgumentException("'dateFormat' must be not null");
		if(date == null) return null;
		
		return DateConvertUtil.parse(DateConvertUtil.format(date, dateFormat),dateFormat);
	}
	
	public static Date toDate(OffsetDateTime time) {
		if(time == null) return null;
		return Date.from(time.toInstant());
	}
	
	public static java.sql.Time toDate(OffsetTime time) {
		if(time == null) return null;
		
		return toDate(time.toLocalTime());
	}
	
	public static Date toDate(LocalDateTime time) {
		if(time == null) return null;
		ZoneId zoneId = ZoneId.systemDefault();
		return toDate(time, zoneId);
	}

	public static Date toDate(LocalDateTime time, ZoneId zoneId) {
		if(time == null) return null;
		ZonedDateTime zoneTime = time.atZone(zoneId);
		Date date = Date.from(zoneTime.toInstant());
		return date;
	}
	
	public static java.sql.Date toDate(LocalDate time) {
		if(time == null) return null;
		return java.sql.Date.valueOf(time);
	}
	
	public static java.sql.Time toDate(LocalTime time) {
		if(time == null) return null;
		return java.sql.Time.valueOf(time);
	}

	public static String smartGuessDateTimeFormat(String value) {
		if(StringUtils.isBlank(value)) {
			return null;
		}
		
		String dateFormat = smartGuessDateFormat(value);
		String timeFormat = smartGuessTimeFormat(value);
		
		if(StringUtils.isNotBlank(dateFormat) && StringUtils.isNotBlank(timeFormat)) {
			return dateFormat + " " + timeFormat;
		}
		return dateFormat + timeFormat;
	}

	public static String smartGuessTimeFormat(String value) {
		if(StringUtils.isBlank(value)) {
			return null;
		}
		String timeFormat = "";
		if(value.contains(":")) {
			timeFormat = "HH:mm:ss";
		}
		return timeFormat;
	}

	public static String smartGuessDateFormat(String value) {
		if(StringUtils.isBlank(value)) {
			return null;
		}
		String dateFormat = "";
		if(value.contains("-")) {
			dateFormat = "yyyy-MM-dd";
		}else if(value.contains("/")) {
			dateFormat = "yyyy/MM/dd";
		}
		return dateFormat;
	}
	
}
