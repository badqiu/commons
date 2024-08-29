package com.github.rapid.common.util;

import static org.junit.Assert.assertEquals;

import java.time.Duration;

import org.junit.Test;

public class DurationUtilTest {

	@Test
	public void parseDuration() {
		Duration duration = DurationUtil.parseDuration("1s");
		System.out.println(duration);
		assertEquals("PT1S", duration.toString());
		
		duration = DurationUtil.parseDuration("PT1s");
		System.out.println(duration);
		assertEquals("PT1S", duration.toString());
		
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

}
