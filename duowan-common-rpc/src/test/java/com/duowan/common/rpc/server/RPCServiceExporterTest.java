package com.duowan.common.rpc.server;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.commons.io.output.TeeOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.duowan.common.rpc.SerDe;
import com.duowan.common.rpc.fortestinvoker.UserWebServiceImpl;
import com.duowan.common.rpc.json.JsonSerDeImpl;
import com.duowan.common.rpc.json.JsonpSerDeImpl;


public class RPCServiceExporterTest extends Assert{
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	
	RPCServiceExporter exporter = new RPCServiceExporter();
	MockHttpServletRequest request = new MockHttpServletRequest();
	MockHttpServletResponse response = new MockHttpServletResponse();
	
	@Before
	public void setUp() throws Exception {
		output.reset();
		
		System.setOut(new PrintStream(new TeeOutputStream(output,System.out)));
		
		Map<String,Object> serviceMapping = new HashMap<String,Object>();
		serviceMapping.put("UserWebService", new UserWebServiceImpl());
		exporter.invoker.setServiceMapping(serviceMapping);
		
		Map<String,SerDe> serDeMapping = new HashMap<String,SerDe>();
		serDeMapping.put("json", new JsonSerDeImpl());
		serDeMapping.put("jsonp", new JsonpSerDeImpl());
		
//		Map<String, SerDe> serDeMapping = new HashMap<String,SerDe>();
//		serDeMapping.put("json", new SerDe() {
//			public void serialize(Object object, OutputStream output) {
//			}
//
//			public Object deserialize(InputStream input, Type returnType) {
//				return null;
//			}
//		});
		exporter.setSerDeMapping(serDeMapping);
	}
	
	@Test
	public void test_say() throws ServletException, IOException {
		
		request.setMethod("POST");
		request.setRequestURI("/security_sys/services/UserWebService/say");
		exporter.handleRequest(request, response);
		verifyOutput("UserWebServiceImpl.say() name:null age:0 timestamp:null");
		
		request.setRequestURI("/security_sys/services/UserWebService/say.do");
		exporter.handleRequest(request, response);
		verifyOutput("UserWebServiceImpl.say() name:null age:0 timestamp:null");
		
		request.addParameter("name", "badqiu");
		request.addParameter("age", "99");
		request.addParameter("timestamp", "20111111");
		request.setRequestURI("/security_sys/services/UserWebService/say.do");
		exporter.handleRequest(request, response);
		verifyOutput("UserWebServiceImpl.say() name:badqiu age:99 timestamp:20111111");
		
		request.addParameter(MethodInvoker.KEY_PARAMETERS, "123;456;20000101");
		request.setRequestURI("/security_sys/services/UserWebService/say.do");
		exporter.handleRequest(request, response);
		verifyOutput("UserWebServiceImpl.say() name:123 age:456 timestamp:20000101");
	}
	
	public void verifyOutput(String str) {
		String actual = output.toString();
		output.reset();
		assertTrue("expected:"+str+"\nactual:"+actual,actual.replaceAll("\\s*", "").equals(str.replaceAll("\\s*", "")));
	}
}
