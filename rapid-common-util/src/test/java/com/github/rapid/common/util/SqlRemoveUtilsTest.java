package com.github.rapid.common.util;

import com.github.rapid.common.util.SqlRemoveUtil;

import junit.framework.TestCase;

public class SqlRemoveUtilsTest extends TestCase {
	
	public void testRemoveXsqlBuilderOrders() {
		String result = SqlRemoveUtil.removeXsqlBuilderOrders("where /~ order by [sortColumn] [sortDirection] ~/");
		assertEquals("where ",result);
		
		result = SqlRemoveUtil.removeXsqlBuilderOrders("where /~    oRder BY [sortColumn] [sortDirection] ~/");
		assertEquals("where ",result);
		
		result = SqlRemoveUtil.removeXsqlBuilderOrders("where order by [sortColumn] [sortDirection]");
		assertEquals("where ",result);
	}
}
