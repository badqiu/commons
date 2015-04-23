package com.duowan.common.rpc.util;

import org.junit.Assert;
import org.junit.Test;


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
