package com.duowan.common.util;

import org.junit.Test;

import com.duowan.common.util.yymsg.YYProtocol;
//import com.duowan.common.util.yymsg.YYProtocol.GTopicTextChat;
//import com.duowan.common.util.yymsg.YYProtocol.Header;
//import com.duowan.common.util.yymsg.YYProtocol.MultiRouteGChatMsg;


public class YYProtocolTest {

	@Test
	public void test() {
		YYProtocol p = new YYProtocol();
		ClassTestUtil.invokeAllMethods(p);
		
//		YYProtocol.Header header = new Header();
//		ClassTestUtil.invokeAllMethods(header);
//		ClassTestUtil.invokeAllMethods(GTopicTextChat.class);
//		ClassTestUtil.invokeAllMethods(MultiRouteGChatMsg.class);
		
	}
}
