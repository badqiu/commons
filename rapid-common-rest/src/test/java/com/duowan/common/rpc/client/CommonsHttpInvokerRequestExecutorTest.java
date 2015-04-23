package com.duowan.common.rpc.client;

import org.apache.commons.httpclient.HttpException;
import org.junit.Test;

import com.duowan.common.rpc.RPCRequest;


public class CommonsHttpInvokerRequestExecutorTest {
	
	@Test(expected=HttpException.class)
	public void test() throws Exception {
		CommonsHttpInvokerRequestExecutor executor = new CommonsHttpInvokerRequestExecutor();
		RPCRequest invocation = new RPCRequest();
		invocation.setMethod("say");
		executor.executeRequest("http://www.baidu.com", invocation);
	}
	
}
