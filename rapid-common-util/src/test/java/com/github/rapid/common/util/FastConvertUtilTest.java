package com.github.rapid.common.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.github.rapid.common.util.FastConvertUtil;

public class FastConvertUtilTest {

	@Test
	public void test() {
		Map map = new HashMap();
		Object result = FastConvertUtil.convert(Map.class, map);
		assertEquals(map,result);
	}
	

}
