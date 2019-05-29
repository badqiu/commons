package com.github.rapid.common.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

public class ScanClassUtilTest {

	@Test
	public void test_isIgnoreClassName() {
		assertTrue(ScanClassUtil.isIgnoreClassName("abc.TestHello"));
		assertTrue(ScanClassUtil.isIgnoreClassName("hi.abc.HelloTest"));
		assertTrue(ScanClassUtil.isIgnoreClassName("hi.abc.package-info"));
		
		assertFalse(ScanClassUtil.isIgnoreClassName("hi.abc.HelloClass"));
	}

	@Test
	public void scanPackages() throws Exception{
		List<String> classList = ScanClassUtil.scanPackages("com.github.rapid.common.util");
		assertFalse(classList.isEmpty());
		
		for(Object row : classList) {
			System.out.println(row);
			Class.forName((String)row);
		}
		
	}
}
