package com.github.rapid.common.test.util;

import org.apache.commons.lang.StringUtils;

public class NumberTest {

	public static void main(String... args) {
		for (int i = 0; i < 1000; i++) {
			String finalNum = StringUtils.leftPad(number2Binary(i),10,'0'); //通过 leftPad,补0
			System.out.println(finalNum);
		}
	}

	/**
	 * 讲10 进制转化为二进制
	 * 
	 * @param de ：待转换的十进制
	 * @return ：转换后的二进制（string）
	 */
	public static String number2Binary(int de) {
		String numstr = "";
		while (de > 0) {
			int res = de % 2; // 除2 取余数作为二进制数
			numstr = res + numstr;
			de = de / 2;
		}
		return numstr;
	}

}
