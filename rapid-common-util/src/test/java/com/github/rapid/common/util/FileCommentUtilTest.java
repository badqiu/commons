package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class FileCommentUtilTest {

	@Test
	public void test() {
		String content = FileCommentUtil.getComments("<!-- helloworld -->");
		System.out.println(content);
	}

}
