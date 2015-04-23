package com.duowan.common.log.logback;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.slf4j.MDC;
import org.slf4j.helpers.Util;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.classic.util.LogbackMDCAdapter;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

import com.duowan.common.util.yymsg.YYMsnSender;
import com.duowan.common.util.yymsg.YYProtocol.GTopicTextChat;
import com.duowan.common.util.yymsg.YYProtocol.MultiRouteGChatMsg;

/**
 * TestYYAppender.java
 * 2012-12-28 下午05:54:09
 * 
 * @author chenming@yy.com
 * TODO:说点什么呗
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({MDC.class})
public class TestYYGroupAppender {
	private Logger iLog;

	@Before
	public void setUp() throws Exception {
		LoggerContext defaultLoggerContext = new LoggerContext();
		try {
			new ContextInitializer(defaultLoggerContext).autoConfig();
		} catch (JoranException je) {
			Util.report("Failed to auto configure default logger context", je);
		}
		/*
		 * MDCAdapter应该是具体的日志实现,mock之
		 */
		PowerMockito.mockStatic(MDC.class);
		PowerMockito.when(MDC.getMDCAdapter()).thenReturn(new LogbackMDCAdapter());
		
		StatusPrinter.printInCaseOfErrorsOrWarnings(defaultLoggerContext);
		iLog = defaultLoggerContext.getLogger(TestYYGroupAppender.class.getName());
	}
	
//	/**
//	 * 弹窗消息
//	 * @throws ParseException
//	 */
//	@Test
//	public void test_uMessage() throws ParseException {
//		AppSendUmessageHandlerClient handlerClient = new AppSendUmessageHandlerClient();
//		UmessageParam umessageParam = new UmessageParam();
//		umessageParam.setAppId(1);
//		umessageParam.setMessageContent("这是一条测试信息");
//		umessageParam.setMessageLinkUrl("www.baidu.com");
//		umessageParam.setMessageTitle("系统异常");
//		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		umessageParam.setShowStartDate(df.parse("2012-12-28 17:50:00"));
//		umessageParam.setShowEndDate(df.parse("2012-12-28 18:20:00"));
//		String testKey = "D767024D1838EB4D98DD";
//		String signText = testKey + umessageParam.getShowStartDate().getTime() 
//			+ umessageParam.getShowEndDate().getTime() + umessageParam.getAppId();
//		String sign = EncryptUtil.getMD5(signText);
//		umessageParam.setSecurityKey(sign);
//		umessageParam.setUidList(Arrays.asList("50002305"));
//		handlerClient.sendUmessage(umessageParam);
//	}
	
	@Test
	public void test_appender() {
		Throwable throwable = new RuntimeException("this is a test error");
		iLog.error("********************");
		for (int i = 0; i < 10; i++) {
			iLog.error("{}", new Object[] { throwable });
		}
		
		try {
			Thread.sleep(1000l*5);
		} catch (InterruptedException e) {
			iLog.error("exception:{}", e);
		}
		
		System.out.println("wait out...");
		iLog.error("{}", new Object[] { throwable });
	}
	
	@Test
	public void test_appender_info() {
		iLog.info("this is a common log");
		try {
			Thread.sleep(1000l*5);
		} catch (InterruptedException e) {
			iLog.error("exception:{}", e);
		}
		
		System.out.println("wait out...");
		iLog.error("this is a error log");
		iLog.error("this is a error log2");
	}
	
	@Test
	public void test_sendYyMsg() {
		GTopicTextChat messageTemplate = new GTopicTextChat();
		messageTemplate.setNickname("udblogin测试告警");
		messageTemplate.setMsgtext("这是一条测试YY群信息");
		MultiRouteGChatMsg msg = new MultiRouteGChatMsg(18847324, messageTemplate);
		msg.setFolderId(24612498);
		YYMsnSender msgSender = new YYMsnSender("101.226.185.11", 54321);
		msgSender.send(msg);
	}
}
