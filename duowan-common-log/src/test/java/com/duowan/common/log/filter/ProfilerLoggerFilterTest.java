package com.duowan.common.log.filter;

import java.io.IOException;

import javax.servlet.ServletException;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.duowan.common.log.ProfilerDigestLog;


public class ProfilerLoggerFilterTest extends Assert{

	@Test
	public void test() throws ServletException, IOException {
		ProfilerLoggerFilter filter = new ProfilerLoggerFilter();
		filter.afterPropertiesSet();
		
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setRequestURI("/hibernate.do");
		filter.doFilter(request, new MockHttpServletResponse(), new MockFilterChain());
		
		String digestLog = ProfilerDigestLog.getDigestLog();
		System.out.println(digestLog);
		System.out.println(ProfilerDigestLog.getDigestLog());
		assertTrue(digestLog,digestLog.contains("/hibernate.do,Y,0,0,127.0.0.1"));
	}
	
}
