package com.duowan.common.lang.enums;

import com.duowan.common.lang.enums.EnumBaseUtils;
import com.duowan.common.util.fortest_enum.SomeTypeEnum;

import junit.framework.TestCase;

public class EnumBaseUtilsTest extends TestCase {
	
	public void test() {
		SomeTypeEnum[] values = SomeTypeEnum.class.getEnumConstants();
		assertEquals(values[0],SomeTypeEnum.K1);
		assertEquals(values[1],SomeTypeEnum.K2);
	}
	
	public void test2() {
		SomeTypeEnum[] values = SomeTypeEnum.class.getEnumConstants();
		String str = EnumBaseUtils.getCode(SomeTypeEnum.K1);
		
		EnumBaseUtils.getRequiredByCode(SomeTypeEnum.K1.getKey(), SomeTypeEnum.class);
		
		assertEquals("K1",EnumBaseUtils.getName(SomeTypeEnum.K1));
		assertEquals("V1",EnumBaseUtils.getDesc(SomeTypeEnum.K1));
		assertEquals("K1",EnumBaseUtils.getCode(SomeTypeEnum.K1));
	}
}
