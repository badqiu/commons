package com.github.rapid.common.util;

import java.sql.Timestamp;

import org.junit.Test;

public class AnyTest {

	
	@Test
	public void test() {
		System.out.println(1 == new Long(1));
		System.out.println("9e0d79aa0901e3694cefb2eef871a49cd9f3de5c3e95e99d060970e04c827097affda6dda5433ca1452da41a932e6fb92d1c1b7c424b69398878e3b170ada7c605401574a4bfd9f24d8db5e61031d584f491250a1bacc62d7e0301cf7affc2754ea1ec66ab9d453bcb45edfee2ce98ec2edfb480c8c3121ee527d32069453d7a".length());
//		String key = "aliyun-qiu-dockerkey-Zdf23XgfgFd";
//		System.out.println(key.length());
		// 1700702889244
		// 1700703088365
//		System.out.println(System.currentTimeMillis());
		Timestamp start = new Timestamp(1703836606004L);
		System.out.println(start);
		Timestamp end = new Timestamp(1703836615531L);
		System.out.println(end);
		System.out.println(end.getTime() - start.getTime());
	}
}
