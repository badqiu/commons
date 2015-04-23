package com.github.rapid.common.util;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;

import org.apache.cxf.helpers.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FreemarkerFilterClassLoaderTest extends Assert {
	
	FreemarkerFilterClassLoader classLoader = new FreemarkerFilterClassLoader();
	HashMap variables = new HashMap();
	
	@Before
	public void setUp() {
		variables.put("username", "badqiu");
		classLoader.setVariables(variables);
		classLoader.getNeedFilterResources().add("fortest_classloader.txt");
	}
	
	@Test
	public void test_needFilterResources() throws Exception {
		classLoader.getNeedFilterResources().clear();
		URL url = classLoader.getResource("fortest_classloader.txt");
		assertEquals("username:${username}",IOUtils.readStringFromStream(url.openStream()));
		
		classLoader.getNeedFilterResources().add("*_classloader.txt");
		url = classLoader.getResource("fortest_classloader.txt");
		assertEquals("username:badqiu",IOUtils.readStringFromStream(url.openStream()));
	}
	
	@Test
	public void test_getResource() throws Exception {
		URL url = classLoader.getResource("fortest_classloader.txt");
		assertEquals("username:badqiu",IOUtils.readStringFromStream(url.openStream()));
	}
	
	@Test
	public void test_getResourceAsStream() throws Exception {
		InputStream input = classLoader.getResourceAsStream("fortest_classloader.txt");
		assertEquals("username:badqiu",IOUtils.readStringFromStream(input));
	}
	
	@Test
	public void test_getResources() throws Exception {
		Enumeration<URL> urls = classLoader.getResources("fortest_classloader.txt");
		boolean loopExec = false;
		while(urls.hasMoreElements()) {
			URL url = urls.nextElement();
			System.out.println(url);
			assertEquals("username:badqiu",IOUtils.readStringFromStream(url.openStream()));
			loopExec = true;
		}
		
		assertTrue(loopExec);
	}
}
