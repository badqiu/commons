package com.github.rapid.common.rpc.client;

import org.apache.commons.httpclient.HttpException;
import org.junit.Test;

import com.github.rapid.common.rpc.RPCRequest;
import com.github.rapid.common.rpc.client.CommonsHttpInvokerRequestExecutor;


public class CommonsHttpInvokerRequestExecutorTest {
	
	@Test(expected=HttpException.class)
	public void test() throws Exception {
		CommonsHttpInvokerRequestExecutor executor = new CommonsHttpInvokerRequestExecutor();
		RPCRequest invocation = new RPCRequest();
		invocation.setMethod("say");
		executor.executeRequest("http://www.baidu.com", invocation);
	}
	
}
