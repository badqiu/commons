package com.duowan.common.rpc.client;

import org.junit.Test;

import com.duowan.common.rpc.RPCRequest;


public class CommonsHttpInvokerRequestExecutorTest {
	
	@Test
	public void test() throws Exception {
		CommonsHttpInvokerRequestExecutor executor = new CommonsHttpInvokerRequestExecutor();
		RPCRequest invocation = new RPCRequest();
		invocation.setMethod("say");
		executor.executeRequest("http://www.163.com", invocation);
	}
	
}
