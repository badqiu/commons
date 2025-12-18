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
	
	@Test
	public void hasAnyTags() {
		assertTrue(TagUtil.hasAnyTags("a,b", "a"));
		assertTrue(TagUtil.hasAnyTags("a,b", "b"));
		assertTrue(TagUtil.hasAnyTags("a,b,c", "c"));
		assertTrue(TagUtil.hasAnyTags("c", "a,b,c"));
		
		assertFalse(TagUtil.hasAnyTags("a,b", "c"));
		assertFalse(TagUtil.hasAnyTags("a,b", null));
		assertFalse(TagUtil.hasAnyTags(null, "c"));
	}
	
	@Test
	public void hasAllTags() {
		assertTrue(TagUtil.hasAllTags("a,b", "a"));
		assertTrue(TagUtil.hasAllTags("a,b", "b"));
		assertTrue(TagUtil.hasAllTags("a,b,c", "c"));
		assertTrue(TagUtil.hasAllTags("a,b,c", "a,b"));
		assertTrue(TagUtil.hasAllTags("a,b,c", "c,a,b"));
		
		assertFalse(TagUtil.hasAllTags("a,b", "c"));
		assertFalse(TagUtil.hasAllTags("a,b", null));
		assertFalse(TagUtil.hasAllTags(null, "c"));
	}
}
