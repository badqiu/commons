package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class ResourceUtilTest {

	@Test
	public void getResourceAsText_success() {
		
		assertNotNull(ResourceUtil.getResourceAsText("/log4j.properties"));
		assertNull(ResourceUtil.getResourceAsText("not_exists_log4j.properties"));
		assertNull(ResourceUtil.getResourceAsText("/not_exists_log4j.properties"));
		
	}
	
	@Test
	public void getResourceAsByteArray() {
		
		assertNotNull(ResourceUtil.getResourceAsByteArray("/log4j.properties"));
		assertNull(ResourceUtil.getResourceAsByteArray("not_exists_log4j.properties"));
		assertNull(ResourceUtil.getResourceAsByteArray("/not_exists_log4j.properties"));
		
	}

}
