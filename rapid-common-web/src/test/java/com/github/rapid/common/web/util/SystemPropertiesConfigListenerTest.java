/**
 * 
 */
package com.github.rapid.common.web.util;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockServletContext;

import jakarta.servlet.ServletContextEvent;

/**
 * @author Administrator
 *
 */
public class SystemPropertiesConfigListenerTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test_contextInitialized() {
		SystemPropertiesConfigListener listener = new SystemPropertiesConfigListener();
		MockServletContext servletContext = new MockServletContext();
		servletContext.addInitParameter(SystemPropertiesConfigListener.CONFIG_PARAM_NAME,"classpath*:test_contextInitialized.properties,classpath*:application-local.properties,file:/data/apps/demoproject/application.properties");
		listener.contextInitialized(new ServletContextEvent(servletContext));
		
		Assert.assertEquals(System.getProperty("appName"),"test_contextInitialized");
		Assert.assertEquals(System.getProperty("appNickname"),"test_contextInitialized/qq");
		Assert.assertEquals(System.getProperty("appRef"),"test_contextInitialized/qq/Asia/Shanghai");
		
		System.getProperties().list(System.out);
	}
}
