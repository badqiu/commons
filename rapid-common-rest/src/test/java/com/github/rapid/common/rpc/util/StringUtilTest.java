package com.github.rapid.common.rpc.util;

import org.junit.Assert;
import org.junit.Test;

import com.github.rapid.common.rpc.util.StringUtil;


public class StringUtilTest extends Assert{
	
	@Test
	public void test_removeExtension() {
		assertEquals(null,StringUtil.removeExtension(null));
		assertEquals("say",StringUtil.removeExtension("say.do"));
		assertEquals(" ",StringUtil.removeExtension(" "));
		assertEquals("say",StringUtil.removeExtension("say"));
		
		assertEquals("say",StringUtil.removeExtension("say.do.abc.diy"));
	}
}
