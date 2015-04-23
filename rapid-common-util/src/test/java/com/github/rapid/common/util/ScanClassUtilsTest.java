package com.github.rapid.common.util;

import java.util.List;

import com.github.rapid.common.util.ScanClassUtils;

import junit.framework.TestCase;

public class ScanClassUtilsTest extends TestCase {
	
	public void testScanPackages() {
		List clazzes = null;
		try {
			clazzes = ScanClassUtils.scanPackages(null);
			fail();
		}catch(Exception e) {
			assertTrue(true);
		}
		
		clazzes = ScanClassUtils.scanPackages("com.duowan.common.util");
		assertFalse(clazzes.isEmpty());
		assertTrue(contains(clazzes, "com.duowan.common.util"));
		System.out.println(clazzes);
		
		clazzes = ScanClassUtils.scanPackages("com.duowan.common.util.**.*");
		assertFalse(clazzes.isEmpty());
		assertTrue(contains(clazzes, "com.duowan.common.util"));
		assertTrue(contains(clazzes, "com.duowan.common.util.fortest"));
		System.out.println(clazzes);
		
		clazzes = ScanClassUtils.scanPackages("javacommon.**.*,com.duowan.*");
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
	
}
