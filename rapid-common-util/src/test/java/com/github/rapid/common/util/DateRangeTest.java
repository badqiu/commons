package com.github.rapid.common.util;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class DateRangeTest {

	@Test
	public void test_isWithinRange() {
		DateRange dr = new DateRange();
		assertFalse(dr.isWithinRange(new Date()));
	}

}
