package com.github.rapid.common.util;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

public class CollectionHelperTest extends TestCase {
	List values = new ArrayList();
	public void setUp() {
		for(int i = 0; i < 10; i++) {
			values.add(i);
		}
	}
	
	@Test
	public void testSafeGet() {
		List list = new ArrayList();
		list.add("00");
		list.add("11");
		
		assertNull(CollectionHelper.safeGet(null, 10, null));
		assertEquals(1,CollectionHelper.safeGet(null, 10, 1));
		
		assertEquals("00",CollectionHelper.safeGet(list, 0, 1));
		assertEquals("11",CollectionHelper.safeGet(list, 1, 1));
		assertEquals(1,CollectionHelper.safeGet(list, 2, 1));
		assertEquals(1,CollectionHelper.safeGet(list, 3, 1));
		
	}
//	public void testMin() {
//		assertEquals(new Integer(0),(Integer)CollectionUtils.min(values,"class"));
//		
//		assertEquals(null,CollectionUtils.min(null,null));
//	}
//	
//	public void testMax() {
//		assertEquals(new Integer(9),(Integer)CollectionUtils.max(values,"class"));
//		assertEquals(null,CollectionUtils.max(null,null));
//	}
	
	public void testSum() {
		assertEquals(45,(long)CollectionHelper.sum(values));
		assertEquals(0,(long)CollectionHelper.sum(new ArrayList()));
		
		assertEquals(0,(long)CollectionHelper.sum(null));

	}
	
	public void testAvg() {
		assertEquals(4.5,(double)CollectionHelper.avg(values));
		assertEquals(0,(long)CollectionHelper.avg(new ArrayList()));
		
		assertEquals(0,(long)CollectionHelper.avg(null));
	}
}
