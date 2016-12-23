package com.github.rapid.common.log.helper;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StringUtils;

import com.github.rapid.common.log.helper.IPUtil;


public class IPUtilTest extends Assert{
	@Test
	public void test() {
		assertTrue(StringUtils.hasText(IPUtil.getIp()));
		assertFalse(IPUtil.getIp().equals("127.0.0.1"));
		System.out.println(IPUtil.getIp());
	}
	
}
