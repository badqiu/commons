package com.github.rapid.common.util.page;

import java.util.Arrays;

import org.junit.Test;

public class PageQueryUtilTest {

	@Test
	public void test() {
		PageQueryUtil.forEachPageQuery(1000, (pageQuery) -> {
			return Arrays.asList(1,2,3);
		}, (queryResultList) -> {
			System.out.println("queryResultList:"+queryResultList);
		});
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
		
		loopCount = 0;
		PageQueryUtil.forEachPageQuery(3, (pageQuery) -> {
			loopCount++;
			if(loopCount > 1) return null;
			return Arrays.asList(1,2,3);
		}, (queryResultList) -> {
			System.out.println("queryResultList:"+queryResultList);
		});
	}
	
}
