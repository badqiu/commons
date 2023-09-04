package com.github.rapid.common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Currency;
import java.util.Locale;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Assert;
import org.junit.Test;

import com.github.rapid.common.util.Money;


public class MoneyTest extends Assert{
	@Test
	public void test_toString() throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		new Money();
		System.out.println(new Money("19182182"));
		System.out.println(new Money(Long.MAX_VALUE));
		System.out.println(BeanUtils.describe(new Money(Long.MAX_VALUE)));
		System.out.println(BeanUtils.describe(new Money(Long.MAX_VALUE,Currency.getInstance(Locale.JAPAN))));
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
