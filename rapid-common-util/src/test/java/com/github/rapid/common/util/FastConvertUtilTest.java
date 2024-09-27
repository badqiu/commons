package com.github.rapid.common.util;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class FastConvertUtilTest {

	@Test
	public void test() {
		Map map = new HashMap();
		Object result = FastConvertUtil.convert(Map.class, map);
		assertEquals(map,result);
	}
	
	
	@Test
	public void string2Timestamp() {
		long time = FastConvertUtil.string2Timestamp("1");
		assertEquals(time,1L);
		
		time = FastConvertUtil.string2Timestamp("2000-01-01");
		assertEquals(time,946656000000L);
		
		time = FastConvertUtil.string2Timestamp("2000-01-01 11:12:13");
		assertEquals(time,946696333000L);
	}

}
