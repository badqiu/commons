package com.github.rapid.common.util;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

import org.junit.Test;
import org.springframework.util.Assert;

import junit.framework.TestCase;
/**
 * @author badqiu
 */
public class DateConvertUtilTest extends TestCase {

	@Test
	public void testConvertString2Date() {
		java.util.Date d = DateConvertUtil.parse("1999-09-09","yyyy-MM-dd",java.util.Date.class);
		Timestamp t = DateConvertUtil.parse("1999-09-09","yyyy-MM-dd",Timestamp.class);
		java.sql.Date sd = DateConvertUtil.parse("1999-09-09","yyyy-MM-dd",java.sql.Date.class);
		java.sql.Time st = DateConvertUtil.parse("1999-09-09","yyyy-MM-dd",java.sql.Time.class);
	}
	
	@Test
	public void test_parse() {
		java.util.Date d = DateConvertUtil.parse("1999-01-02 12:13:14","yyyy-MM-dd HH:mm:ss",java.util.Date.class);
		System.out.println(d);
	}
	
	@Test
	public void test_smart_parse() {
		java.util.Date d = DateConvertUtil.smartParse("1999-01-02 12:13:14");
		Assert.notNull(d,"not null");
		
		d = DateConvertUtil.smartParse(" ");
		Assert.isNull(d,"must is null");
		System.out.println(d);
	}
	
	@Test
	public void testFormat() {
		java.util.Date d = DateConvertUtil.parse("1999-09-09","yyyy-MM-dd",java.util.Date.class);
		String r = DateConvertUtil.format(d, "MM/dd/yy");
		assertEquals(r,"09/09/99");
	}
	
	@Test
	public void testToDate() {
		DateConvertUtil.toDate(LocalDateTime.now());
		DateConvertUtil.toDate(LocalDate.now());
		DateConvertUtil.toDate(LocalTime.now());
		DateConvertUtil.toDate(OffsetDateTime.now());
		DateConvertUtil.toDate(OffsetTime.now());
	}
	
}
