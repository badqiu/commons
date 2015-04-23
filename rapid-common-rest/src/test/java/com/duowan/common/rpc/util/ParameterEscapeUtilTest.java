package com.duowan.common.rpc.util;

import java.net.URLDecoder;
import java.net.URLEncoder;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.junit.Assert;
import org.junit.Test;


public class ParameterEscapeUtilTest extends Assert {

	@Test
	public void test_url_encode() throws Exception {
		assertEquals("%2C_%3B_%3A",URLEncoder.encode(",_;_:", "UTF-8"));
		assertEquals(",_;_:",URLDecoder.decode(",_;_:", "UTF-8"));
		assertEquals(",_;_:",URLDecoder.decode("%2C_%3B_%3A", "UTF-8"));
		System.out.println(Long.MAX_VALUE);
	}

	@Test
	public void test_unescapeParameter() throws Exception {
		assertEquals(",;:\\",ParameterEscapeUtil.unescapeParameter("\\,\\;\\:\\\\"));
		assertEquals(",;:\\",ParameterEscapeUtil.unescapeParameter("\\\001\\\002\\\003\\\\"));
	}
	
	@Test
	public void test_escapeParameter() throws Exception {
		assertEquals("\\\001\\\002\\\003\\\\",ParameterEscapeUtil.escapeParameter(",;:\\"));
		assertEquals("\001\002\\\001\\\002\\\003\\\\",ParameterEscapeUtil.escapeParameter("\001\002,;:\\"));
	}
	
}
