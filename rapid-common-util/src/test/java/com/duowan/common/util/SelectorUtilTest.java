package com.duowan.common.util;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class SelectorUtilTest {

	@Test
	public void test_map() {
		Map map = new HashMap();
		map.put("id", "123");
		Object r = SelectorUtil.getElementById(map, "123");
		assertEquals(map,r);
	}

}
