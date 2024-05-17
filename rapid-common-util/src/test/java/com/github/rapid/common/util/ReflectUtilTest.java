package com.github.rapid.common.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

public class ReflectUtilTest {

	@Test
	public void test() {
		ReflectUtil.modifyAllStaticVariables(DemoConstant.class, DemoConstantProd.class);
		
		assertEquals(DemoConstant.name,"prod_name");
		assertEquals(DemoConstant.privateName,"prod_private_name");
//		assertEquals(DemoConstant.privateFinalName,"prod_private_final_name");
	}

	public static class DemoConstant {
		public static String  name = "test_name";
		private static String  privateName = "test_name";
		
//		private static final String  privateFinalName = "test_private_final_name";
	}
	
	public static class DemoConstantProd {
		public static String  name = "prod_name";
		private static String  privateName = "prod_private_name";
		
//		private static final String  privateFinalName = "prod_private_final_name";
	}
}
