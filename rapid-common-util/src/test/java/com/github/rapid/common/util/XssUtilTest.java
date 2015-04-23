package com.github.rapid.common.util;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.github.rapid.common.util.Profiler;
import com.github.rapid.common.util.XssException;
import com.github.rapid.common.util.XssUtil;


public class XssUtilTest extends Assert{
	Map invlidMap = new HashMap(); 
	{
		invlidMap.put("name", "<iframe src=http://www.163.com />");
		invlidMap.put("sex", "<div onclick=\"alert('div onclick xss')\">div click xss</div>");
		invlidMap.put("array", new String[]{"<div onclick=\"alert('div onclick xss')\">div click xss</div>","bbb"});
	}
	
	Map invlidArrayMap = new HashMap(); 
	{
		invlidArrayMap.put("array", new String[]{"<div onclick=\"alert('div onclick xss')\">div click xss</div>","bbb"});
	}
	
	@Test
	public void test_checkXss() {
		for(Object key : invlidMap.keySet()) {
			Object v = (Object)invlidMap.get(key);
			if(v instanceof String) {
				String string = (String)v;
				assertTrue(XssUtil.hasXSS(string));
				try {
					XssUtil.checkXSS(string);
					fail();
				}catch(XssException e) {
					assertTrue(true);
				}
			}
			
		}
		
		try {
			XssUtil.checkXSS(invlidMap);
			fail();
		}catch(Exception e) {
			assertTrue(true);
		}
		try {
			XssUtil.checkXSS(invlidArrayMap);
			fail();
		}catch(Exception e) {
			assertTrue(true);
		}
	}
	
	@Test
	public void test_hasXSS() {
		assertTrue(XssUtil.hasXSS(" <script>"));
		assertTrue(XssUtil.hasXSS(" <script    aaa=123>"));
		assertTrue(XssUtil.hasXSS(" </script>"));
		assertTrue(XssUtil.hasXSS(" src=' aaaa.txt'"));
		assertTrue(XssUtil.hasXSS("<iframe src=http://www.163.com />"));
		assertFalse(XssUtil.hasXSS("<iframe http://www.163.com />"));
		assertTrue(XssUtil.hasXSS("  src=\" bbb.txt \"error"));
		assertTrue(XssUtil.hasXSS("  eval  ('a.txt')"));
		assertTrue(XssUtil.hasXSS("  eval \n\t  ('a.txt')"));
		assertTrue(XssUtil.hasXSS("   expression  ('a.txt')"));
		assertTrue(XssUtil.hasXSS("   javascript:alert(a.txt)"));
		assertTrue(XssUtil.hasXSS("   vbscript:alert(a.txt)"));
		assertTrue(XssUtil.hasXSS("   onload = 'alert'"));
		assertTrue(XssUtil.hasXSS("   &#0000106"));
		assertTrue(XssUtil.hasXSS("   <iframe src='xxx.html'"));
		assertTrue(XssUtil.hasXSS("   <div onclick=\"alert('div onclick xss')\">div click xss</div>"));
		assertFalse(XssUtil.hasXSS("   <iframe"));
		
		Profiler.start("test_hasXSS");
		int i = 0;
		for(;i < 1000000; i++) {
			XssUtil.hasXSS(" <script    aaa=123>");
		}
		Profiler.release(i);
		Profiler.printDump();
	}
	
//	@Test
//	public void test_trimXSS_perf() {
//		Profiler.start("test_trimXSS_perf");
//		int i = 0;
//		for(;i < 500000; i++) {
//			XssUtil.trimXSS(" <script");
//		}
//		Profiler.release(i);
//		Profiler.printDump();
//	}
//	@Test
//	public void test_trimXSS() {
//		Profiler.start("test_trimXSS");
//		int i = 0;
//		for(;i < 50000; i++) {
//			assertEquals(" ",XssUtil.trimXSS(" <script>"));
//			assertEquals(" ",XssUtil.trimXSS(" <script    aaa=123>"));
//			assertEquals("a  ",XssUtil.trimXSS("a  </script>"));
//			assertEquals("bb ",XssUtil.trimXSS("bb src=' aaaa.txt'"));
//			assertEquals("  error",XssUtil.trimXSS("  src=\" bbb.txt \"error"));
//			assertEquals("jj  ",XssUtil.trimXSS("jj  eval  ('a.txt')"));
//			assertEquals("dd   ",XssUtil.trimXSS("dd   expression  ('a.txt')"));
//			assertEquals("kk   alert(a.txt)",XssUtil.trimXSS("kk   javascript:alert(a.txt)"));
//			assertEquals("   alert(a.txt)",XssUtil.trimXSS("   vbscript:alert(a.txt)"));
//			assertEquals("    ",XssUtil.trimXSS("   onload  ()= "));
//			assertEquals("   0000106",XssUtil.trimXSS("   &#0000106"));
//		}
//		Profiler.release(i);
//		Profiler.printDump();
//	}
	
}
