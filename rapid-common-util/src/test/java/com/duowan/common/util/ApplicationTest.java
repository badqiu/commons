package com.duowan.common.util;

import org.junit.Assert;
import org.junit.Test;


public class ApplicationTest extends Assert{

	@Test
	public void testGetAppMode() {
		assertFalse(Application.isDevMode());
		assertEquals("prod",Application.getAppMode());
	}
	
//	@Test
//	public void testReload() throws InterruptedException {
//		while(true) {
//			Application.reload();
//			Thread.sleep(1000 * 5);
//		}
//	}
	
}
