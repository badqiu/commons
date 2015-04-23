package com.duowan.common.log.helper;

import java.util.Arrays;
import java.util.List;



public class EmailUtilTest {
	public static void main(String[] args) {
		List<String> tos = Arrays.asList("chenming@yy.com");
		EmailUtil.send("测试邮件", "spring mail2", tos);
		System.out.println("==============");
		EmailUtil.asynSend("测试邮件", "spring mail2", tos);
		System.out.println("==============");
	}
}
