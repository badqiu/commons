package com.github.rapid.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

import com.github.rapid.common.util.Profiler;

public class FastDateFormatPerfTest {

	@Test
	public void test_parse() throws ParseException {
		FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
		Date date = dateFormat.parse("2020-02-02 02:02:02");
		
		System.out.println(new Timestamp(date.getTime()));
	}
	
	long loopCount = 10000000;
	@Test
	public void test_perf_FastDateFormat() throws ParseException {
		FastDateFormat dateFormat = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
		Profiler.start("FastDateFormat",loopCount);
		for(int i = 0; i < loopCount; i++) {
			Date date = dateFormat.parse("2020-02-02 02:02:02");
		}
		Profiler.release();
		System.out.println(Profiler.dump());
	}
	
	@Test
	public void test_perf_SimpleDateFormat() throws ParseException {
		java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Profiler.start("SimpleDateFormat",loopCount);
		for(int i = 0; i < loopCount; i++) {
			Date date = dateFormat.parse("2020-02-02 02:02:02");
		}
		Profiler.release();
		System.out.println(Profiler.dump());
	}
	
	@Test
	public void test_perf__new_SimpleDateFormat() throws ParseException {
		Profiler.start("new SimpleDateFormat by loop",loopCount);
		for(int i = 0; i < loopCount; i++) {
			java.text.SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = dateFormat.parse("2020-02-02 02:02:02");
		}
		Profiler.release();
		System.out.println(Profiler.dump());
	}
}
