package com.github.rapid.common.util;

import org.junit.Test;

import com.github.rapid.common.util.yymsg.YYProtocol;


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
