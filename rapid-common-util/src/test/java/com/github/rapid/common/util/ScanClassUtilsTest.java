package com.github.rapid.common.util;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

public class ScanClassUtilsTest extends TestCase {
	
	public void testScanPackages() {
		List clazzes = null;
		try {
			clazzes = ScanClassUtil.scanPackages(null);
			fail();
		}catch(Exception e) {
			assertTrue(true);
		}
		
		clazzes = ScanClassUtil.scanPackages("com.duowan.common.util");
		assertFalse(clazzes.isEmpty());
		assertTrue(contains(clazzes, "com.duowan.common.util"));
		System.out.println(clazzes);
		
		clazzes = ScanClassUtil.scanPackages("com.duowan.common.util.**.*");
		assertFalse(clazzes.isEmpty());
		assertTrue(contains(clazzes, "com.duowan.common.util"));
		assertTrue(contains(clazzes, "com.duowan.common.util.fortest"));
		System.out.println(clazzes);
		
		clazzes = ScanClassUtil.scanPackages("javacommon.**.*,com.duowan.*");
		assertFalse(clazzes.isEmpty());
		assertTrue(contains(clazzes, "com.duowan.common"));
		System.out.println(clazzes);
	}
	
	public boolean contains(List<String> clazzes,String containString) {
		for(String s : clazzes) {
			if(s.contains(containString)) {
				return true;
			}
		}
		return false;
	}
	
	@Test
	public void test() {
	}
	
}
