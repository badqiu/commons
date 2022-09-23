package com.github.rapid.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.rapid.common.util.ObjectUtil;



public class ObjectUtilsTest {
	HashMap NOT_EMPTY_MAP = new HashMap();
	@Before
	public void setUp() {
		NOT_EMPTY_MAP.put("1", 1);
	}
	@Test
	public void testIsEmpty() {
		Assert.assertTrue(ObjectUtil.isEmpty(null));
		Assert.assertTrue(ObjectUtil.isEmpty(""));
		Assert.assertTrue(ObjectUtil.isEmpty(new ArrayList()));
		Assert.assertTrue(ObjectUtil.isEmpty(new HashMap()));
		Assert.assertTrue(ObjectUtil.isEmpty(new Object[]{}));
		
		Assert.assertTrue(!ObjectUtil.isEmpty(" "));
		Assert.assertTrue(!ObjectUtil.isEmpty(Arrays.asList(1,2,3)));
		Assert.assertTrue(!ObjectUtil.isEmpty(NOT_EMPTY_MAP));
		Assert.assertTrue(!ObjectUtil.isEmpty(new Object[]{1,2,3}));
	}
	
	@Test
	public void testIsEmptyWithIeelgalArgument() {
		Assert.assertFalse(ObjectUtil.isEmpty(new Object()));
	}
	
	@Test
	public void isNumber() {
		Object num = 1;
		System.out.println(num.getClass());
		System.out.println(num instanceof Number);
	}
	
}
