package com.duowan.common.log.helper;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * TestEmailUtil.java
 * 2013-1-29 上午10:50:29
 * 
 * @author chenming@yy.com
 * TODO:说点什么呗
 * 
 */
public class TestEmailUtil {
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testSend() {
		String subject = "这是一封测试邮件";
		String content = "这当然是测试信息啦";
		List<String> tos = Arrays.asList("chenming@yy.com");
		EmailUtil.send(subject, content, tos);
	}

	@Test
	public void testAsynSend() {
		String subject = "这是一封异步测试邮件";
		String content = "这当然是测试信息啦";
		List<String> tos = Arrays.asList("chenming@yy.com");
		EmailUtil.asynSend(subject, content, tos);
	}
}
