package com.duowan.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;
/**
 * @author badqiu
 */
public class DateConvertUtils {
	
	public static java.util.Date parse(String dateString,String dateFormat) {
		return parse(dateString, dateFormat,java.util.Date.class);
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends java.util.Date> T parse(String dateString,String dateFormat,Class<T> targetResultType) {
		if(StringUtils.isEmpty(dateString))
			return null;
		DateFormat df = new SimpleDateFormat(dateFormat);
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
	public static Date extract(Date date,String datePattern) throws IllegalArgumentException {
		if(datePattern == null) throw new IllegalArgumentException("'datePattern' must be not null");
		if(date == null) return null;
		
		return DateConvertUtils.parse(DateConvertUtils.format(date, datePattern),datePattern);
	}
}
