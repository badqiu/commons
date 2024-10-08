package com.github.rapid.common.util.page;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

public class PageQueryUtilTest {

	@Before
	public void before() {
	}
	
	@Test
	public void test() {
		PageQueryUtil.forEachPageQuery(1000, (pageQuery) -> {
			return Arrays.asList(1,2,3);
		}, (queryResultList) -> {
			System.out.println("queryResultList:"+queryResultList);
			loopCount++;
		});
		assertEquals(1,loopCount);
	}
	
	@Test
	public void testForEachPage() {
		PageQueryUtil.forEachPage(1000, (page) -> {
			return Arrays.asList(1,2,3);
		}, (queryResultList) -> {
			System.out.println("queryResultList:"+queryResultList);
			loopCount++;
		});
		assertEquals(1,loopCount);
		
		
		
		loopCount = 0;
		PageQueryUtil.forEachPage(3, (pageQuery) -> {
			loopCount++;
			if(loopCount > 10) return null;
			return Arrays.asList(1,2,3);
		}, (queryResultList) -> {
			System.out.println("queryResultList:"+queryResultList);
		});
		assertEquals(11,loopCount);
	}
	
	@Test
	public void test_one_function() {
		PageQueryUtil.forEachPageQuery(1000, (pageQuery) -> {
			loopCount++;
			return Arrays.asList(1,2,3);
		});
		assertEquals(1,loopCount);
	}
	

	private int loopCount = 0;
	@Test
	public void test_ever_loop() {
		PageQueryUtil.forEachPageQuery(3, (pageQuery) -> {
			loopCount++;
			if(loopCount > 10) return null;
			return Arrays.asList(1,2,3);
		}, (queryResultList) -> {
			System.out.println("queryResultList:"+queryResultList);
		});
		assertEquals(11,loopCount);
		
		loopCount = 0;
		PageQueryUtil.forEachPageQuery(3, (pageQuery) -> {
			loopCount++;
			if(loopCount > 1) return null;
			return Arrays.asList(1,2,3);
		}, (queryResultList) -> {
			System.out.println("queryResultList:"+queryResultList);
		});
		assertEquals(2,loopCount);
		
	}
	
}
