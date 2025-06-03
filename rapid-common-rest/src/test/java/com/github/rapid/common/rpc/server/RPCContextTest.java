package com.github.rapid.common.rpc.server;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.github.rapid.common.util.Profiler;

public class RPCContextTest {

	@Test
	public void test() {
		MockHttpServletRequest request = new MockHttpServletRequest();
		RPCContext.setRequest(request);
		
		long count = 10000000000L;
		Profiler.start("RPCContext.getRequest()",count);
		for(long i = 0; i < count; i++) {
			RPCContext.getRequest();
		}
		Profiler.release();
		
		
		System.out.println(Profiler.dump());
		RPCContext.clear();
	}

}
