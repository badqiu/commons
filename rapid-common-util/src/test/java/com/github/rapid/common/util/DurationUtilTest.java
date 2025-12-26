package com.github.rapid.common.util;

import static org.junit.Assert.assertEquals;

import java.time.DateTimeException;
import java.time.Duration;

import org.junit.Test;

public class DurationUtilTest {

	@Test
	public void parseDuration() {
		Duration duration = DurationUtil.parseDuration("1s");
		System.out.println(duration);
		assertEquals("PT1S", duration.toString());
		
		duration = DurationUtil.parseDuration("10s");
		System.out.println(duration);
		assertEquals("PT10S", duration.toString());
		
		duration = DurationUtil.parseDuration("1d");
		System.out.println(duration);
		assertEquals("PT24H", duration.toString());
		
		
//		duration = DurationUtil.parseDuration("1w1d");
//		System.out.println(duration);
//		assertEquals("PT1D", duration.toString());
		
		duration = DurationUtil.parseDuration("601s");
		System.out.println(duration);
		assertEquals("PT10M1S", duration.toString());
		
		duration = DurationUtil.parseDuration("1h20m50s");
		System.out.println(duration);
		assertEquals("PT1H20M50S", duration.toString());
		
		duration = DurationUtil.parseDuration("PT1s");
		System.out.println(duration);
		assertEquals("PT1S", duration.toString());
		
		duration = DurationUtil.parseDuration("PT0.25s");
		System.out.println(duration);
		assertEquals("PT0.25S", duration.toString());
		
		duration = DurationUtil.parseDuration("3m1S");
		System.out.println(duration);
		assertEquals("PT3M1S", duration.toString());
		
		duration = DurationUtil.parseDuration("PT1M5s");
		System.out.println(duration);
		assertEquals("PT1M5S", duration.toString());
		
		duration = DurationUtil.parseDuration("3H4M");
		System.out.println(duration);
		assertEquals("PT3H4M", duration.toString());
		
		duration = DurationUtil.parseDuration("2dT3h");
		System.out.println(duration);
		assertEquals("PT51H", duration.toString());
		
		duration = DurationUtil.parseDuration("pt1m2s");
		System.out.println(duration);
		assertEquals("PT1M2S", duration.toString());
	}

	@Test(expected=DateTimeException.class)
	public void parseDurationError() {
		Duration duration = DurationUtil.parseDuration("2D1s");
		System.out.println(duration);
		assertEquals("PT1S", duration.toString());
	}
}
