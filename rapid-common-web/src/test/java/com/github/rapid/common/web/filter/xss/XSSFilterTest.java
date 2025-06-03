package com.github.rapid.common.web.filter.xss;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.github.rapid.common.util.XssException;

import jakarta.servlet.ServletException;


public class XSSFilterTest {
	XSSFilter filter = new XSSFilter();
	
	MockHttpServletRequest request = new MockHttpServletRequest();
	MockFilterConfig filterConfig = new MockFilterConfig();
	MockFilterChain filterChain = new MockFilterChain();
	MockHttpServletResponse response = new MockHttpServletResponse();
	
	@Before
	public void setUp() throws Exception {
		filterConfig.addInitParameter("excludes", "/exclude.do");
		filter.init(filterConfig);
	}
	
	@Test(expected=XssException.class)
	public void testFail() throws ServletException, IOException {
		request.addParameter("badqiu", "<iframe src=www.163.com/>");
		filter.doFilter(request, response, filterChain);
	}
	
	@Test(expected=XssException.class)
	public void testFail2() throws ServletException, IOException {
		request.addParameter("badqiu", new String[]{"<iframe src=www.163.com/>","bbbb"});
		filter.doFilter(request, response, filterChain);
	}
	
	@Test
	public void testExcelude() throws ServletException, IOException {
		request.addParameter("badqiu", new String[]{"<iframe src=www.163.com/>","bbbb"});
		request.setRequestURI("/exclude.do");
		filter.doFilter(request, response, filterChain);
	}
}
