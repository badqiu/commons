package com.github.rapid.common.util;

import com.github.rapid.common.util.FileSizeUtils;

import junit.framework.TestCase;

public class FileSizeUtilsTest extends TestCase {
	
	public void testGetHumanReadableFileSize() {
		assertEquals("0",FileSizeUtils.getHumanReadableFileSize(0));
		assertEquals("1.12KB",FileSizeUtils.getHumanReadableFileSize(1024+124+1));
		assertEquals("2MB",FileSizeUtils.getHumanReadableFileSize(1024*1024*2+8));
		assertEquals("1GB",FileSizeUtils.getHumanReadableFileSize(1024*1024*1024));
		long tb = (long)1024*1024*1024*1024*5;
		assertEquals("5TB",FileSizeUtils.getHumanReadableFileSize(tb+8));
		assertEquals("8192PB",FileSizeUtils.getHumanReadableFileSize(Long.MAX_VALUE));
		
		assertEquals("-1",FileSizeUtils.getHumanReadableFileSize(-1));
		assertEquals("-1000000",FileSizeUtils.getHumanReadableFileSize(-1000000));
	}
	
	public void test_parseHumanReadableFileSize() {
		assertEquals(0,FileSizeUtils.parseHumanReadableFileSize(""));
		assertEquals(1024,FileSizeUtils.parseHumanReadableFileSize("1024"));
		assertEquals(1024 * 100,FileSizeUtils.parseHumanReadableFileSize("100KB"));
		assertEquals(1024L * 1024 * 100,FileSizeUtils.parseHumanReadableFileSize("100Mb"));
		assertEquals(1024L * 1024L * 1024 * 100,FileSizeUtils.parseHumanReadableFileSize("100gB"));
		assertEquals(1024L * 1024L * 1024L * 1024 * 100,FileSizeUtils.parseHumanReadableFileSize("100tb"));
		assertEquals(1024L * 1024L * 1024L * 1024L * 1024 * 3,FileSizeUtils.parseHumanReadableFileSize("3pb"));
		
		
		assertEquals(1024L * 1024L * 1024 * 100 + 1024L*1024L*512,FileSizeUtils.parseHumanReadableFileSize("100.5gB"));
		
		
	}
}
