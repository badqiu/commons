package com.github.rapid.common.util;

import java.sql.Timestamp;

import org.junit.Test;

public class AnyTest {

	
	@Test
	public void test() {
//		String key = "aliyun-qiu-dockerkey-Zdf23XgfgFd";
//		System.out.println(key.length());
		// 1700702889244
		// 1700703088365
//		System.out.println(System.currentTimeMillis());
		Timestamp start = new Timestamp(1702965991174L);
		System.out.println(start);
		Timestamp end = new Timestamp(1702965992000L);
		System.out.println(end);
		System.out.println(end.getTime() - start.getTime());
	}
}
