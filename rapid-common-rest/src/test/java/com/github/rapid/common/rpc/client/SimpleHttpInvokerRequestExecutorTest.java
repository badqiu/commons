package com.github.rapid.common.rpc.client;

import org.junit.Test;

import com.github.rapid.common.rpc.RPCRequest;

public class SimpleHttpInvokerRequestExecutorTest {

	@Test
	public void test() throws Exception {
		 SimpleHttpInvokerRequestExecutor sh = new  SimpleHttpInvokerRequestExecutor();
		 RPCRequest r = new RPCRequest();
		 r.setArguments(new Object[]{123,456});
		 sh.executeRequest("http://localhost:8080/userdata/importData.do?username=test&password=iXZfu38dfdo20M8Odw9eJR5dfeQ2ogy4o0Oet4G&importMode=append&appId=lj&module=master", r);
	}

}
