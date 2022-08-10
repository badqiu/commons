package com.github.rapid.common.util;

import java.sql.Timestamp;

import com.github.rapid.common.util.DateConvertUtil;

import junit.framework.TestCase;
/**
 * @author badqiu
 */
public class DateConvertUtilTest extends TestCase {

	public void testConvertString2Date() {
		java.util.Date d = DateConvertUtil.parse("1999-09-09","yyyy-MM-dd",java.util.Date.class);
		Timestamp t = DateConvertUtil.parse("1999-09-09","yyyy-MM-dd",Timestamp.class);
		java.sql.Date sd = DateConvertUtil.parse("1999-09-09","yyyy-MM-dd",java.sql.Date.class);
		java.sql.Time st = DateConvertUtil.parse("1999-09-09","yyyy-MM-dd",java.sql.Time.class);
	}
	
	public void test_parse() {
		java.util.Date d = DateConvertUtil.parse("1999-01-02 12:13:14","yyyy-MM-dd HH:mm:ss",java.util.Date.class);
		System.out.println(d);
	}
	
	public void testFormat() {
		java.util.Date d = DateConvertUtil.parse("1999-09-09","yyyy-MM-dd",java.util.Date.class);
		String r = DateConvertUtil.format(d, "MM/dd/yy");
		assertEquals(r,"09/09/99");
	}
}
