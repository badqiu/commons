package com.github.rapid.common.util;

import com.github.rapid.common.util.FileSizeUtil;

import junit.framework.TestCase;

public class FileSizeUtilsTest extends TestCase {
	
	public void testGetHumanReadableFileSize() {
		assertEquals("0",FileSizeUtil.getHumanReadableFileSize(0));
		assertEquals("1.12KB",FileSizeUtil.getHumanReadableFileSize(1024+124+1));
		assertEquals("2MB",FileSizeUtil.getHumanReadableFileSize(1024*1024*2+8));
		assertEquals("1GB",FileSizeUtil.getHumanReadableFileSize(1024*1024*1024));
		long tb = (long)1024*1024*1024*1024*5;
		assertEquals("5TB",FileSizeUtil.getHumanReadableFileSize(tb+8));
		assertEquals("8192PB",FileSizeUtil.getHumanReadableFileSize(Long.MAX_VALUE));
		
		assertEquals("-1",FileSizeUtil.getHumanReadableFileSize(-1));
		assertEquals("-1000000",FileSizeUtil.getHumanReadableFileSize(-1000000));
	}
	
	public void test_parseHumanReadableFileSize() {
		assertEquals(0,FileSizeUtil.parseHumanReadableFileSize(""));
		assertEquals(1024,FileSizeUtil.parseHumanReadableFileSize("1024"));
		assertEquals(1024 * 100,FileSizeUtil.parseHumanReadableFileSize("100KB"));
		assertEquals(1024L * 1024 * 100,FileSizeUtil.parseHumanReadableFileSize("100Mb"));
		assertEquals(1024L * 1024L * 1024 * 100,FileSizeUtil.parseHumanReadableFileSize("100gB"));
		assertEquals(1024L * 1024L * 1024L * 1024 * 100,FileSizeUtil.parseHumanReadableFileSize("100tb"));
		assertEquals(1024L * 1024L * 1024L * 1024L * 1024 * 3,FileSizeUtil.parseHumanReadableFileSize("3pb"));
		
		
		assertEquals(1024L * 1024L * 1024 * 100 + 1024L*1024L*512,FileSizeUtil.parseHumanReadableFileSize("100.5gB"));
		
		
	}
}
