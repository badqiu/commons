package com.duowan.common.web.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.duowan.common.web.wrapper.UnsafeRedirectException;


public class RequestResponseWrapperFilterTest {
	
	RequestResponseWrapperFilter filter = new RequestResponseWrapperFilter();
	MockHttpServletRequest request = new MockHttpServletRequest();
	MockHttpServletResponse response = new MockHttpServletResponse();
	MockFilterConfig filterConfig = new MockFilterConfig();
	
	@Before
	public void setUp() throws ServletException {
		filterConfig.addInitParameter("requestWrappers", "com.duowan.common.web.wrapper.RealRemoteAddrRequestWrapper ");
		filterConfig.addInitParameter("responseWrappers", "com.duowan.common.web.wrapper.RediectProtectReponseWrapper");
		
		filterConfig.addInitParameter("safe_host_white_list", "duowan.com,yy.com");
		filter.init(filterConfig);		
	}
	
	@Test
	public void test_request_getRemoteAddr() throws Exception{
		MockFilterChain filterChain = new MockFilterChain() {
			@Override
			public void doFilter(ServletRequest request,
					ServletResponse response) {
				super.doFilter(request, response);
				Assert.assertEquals(request.getRemoteAddr(),"255.255.255.255");
				Assert.assertEquals(request.getRemoteAddr(),"255.255.255.255");
			}
		};
		request.addHeader("X-Forwarded-For", "198.198.198.198,255.255.255.255");
		filter.doFilter(request, response, filterChain);
	}
	
	@Test
	public void test_response_sendRedirect() throws Exception{
		testSendRedirect("https://www.duowan.com/abc.html?q=123&diy=456");
		testSendRedirect("https://www.duowan.com:8080/abc.html?q=123&diy=456");
		testSendRedirect("http://www.yy.com/abc.html?q=123&diy=456");
		testSendRedirect("http://www.duowan.com/abc.html?q=123&diy=456");
		
		testSendRedirect("/abc.html?q=123&diy=456");
	}

	@Test(expected=UnsafeRedirectException.class)
	public void test_response_sendRedirect_unsafe() throws Exception{
		testSendRedirect("http://www.unsafe.com/abc.html?q=123&diy=456");
	}
	
	@Test(expected=UnsafeRedirectException.class)
	public void test_response_sendRedirect_unsafe2() throws Exception{
		testSendRedirect("https://www.unsafe.com:8080/abc.html?q=123&diy=456");
	}
	
	private void testSendRedirect(final String url) throws ServletException, IOException {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain filterChain = new MockFilterChain() {
			@Override
			public void doFilter(ServletRequest request,
					ServletResponse response) {
				super.doFilter(request, response);
				HttpServletResponse httpResponse = (HttpServletResponse)response;
				try {
					httpResponse.sendRedirect(url);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
		};
		filter.doFilter(request, response, filterChain);
	}
}
