package com.github.rapid.common.util;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class TagUtilTest {

	@Test
	public void test() {
		Set tags = TagUtil.parseTags(" , ,,A,B");
		System.out.println(tags);
		assertEquals(tags.size(),2);
	}

	@Test
	public void test_blank() {
		Set tags = TagUtil.parseTags("    ");
		assertEquals(tags.size(),0);
		
		tags = TagUtil.parseTags(null);
		assertEquals(tags.size(),0);
	}
	
	@Test
	public void test_format() {
		String tags = TagUtil.format(",,A,,B, , ,C");
		assertEquals(tags,"A,B,C");
	}
}
