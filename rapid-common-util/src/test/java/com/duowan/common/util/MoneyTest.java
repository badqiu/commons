package com.duowan.common.util;

import org.junit.Assert;
import org.junit.Test;


public class MoneyTest extends Assert{
	@Test
	public void test_toString() {
		new Money();
		System.out.println(new Money("19182182"));
		System.out.println(new Money(Long.MAX_VALUE));
	}
	
	@Test public void tets() {
		assertTrue(new Money(0.01).greaterThanEquals(new Money(0.011)));
		assertFalse(new Money(0.01).greaterThan(new Money(0.011)));
		assertTrue(new Money(0.02).greaterThan(new Money(0.01)));
		
		assertTrue(new Money(0.01).lessThanEquals(new Money(0.011)));
		assertFalse(new Money(0.01).lessThan(new Money(0.011)));
		assertTrue(new Money(0.01).lessThan(new Money(0.02)));
	}
}
