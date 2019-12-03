package com.github.rapid.common.rpc.server;

import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.output.TeeOutputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.convert.ConversionFailedException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.rapid.common.rpc.fortestinvoker.UserWebService;
import com.github.rapid.common.rpc.fortestinvoker.UserWebServiceImpl;

public class MethodInvokerTest extends Assert{
	
	MethodInvoker invoker = new MethodInvoker();
	UserWebService userWebService = mock(UserWebService.class);
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	Map<String,Object> params = new HashMap<String,Object>();
	
	@Before
	public void setUp() {
		output.reset();
		Map<String,Object> serviceMapping = new HashMap<String,Object>();
		System.setOut(new PrintStream(new TeeOutputStream(output,System.out)));
		serviceMapping.put("userWebService", new UserWebServiceImpl());
		invoker.setServiceMapping(serviceMapping);
	}
	
	@Test
	public void testInvokeNotArgument() throws Exception, IllegalAccessException, InvocationTargetException {
		Map<String,Object> params = new HashMap<String,Object>();
		invoker.invoke("userWebService", "notArgument", params);
		
		verifyOutput("notArgument()");
	}
	
	@Test
	public void testInvokeSay() throws Exception, IllegalAccessException, InvocationTargetException {
		invoker.invoke("userWebService", "say", params);
		verifyOutput("UserWebServiceImpl.say() name:null age:0 timestamp:null");
		
		params.put("name", "jjyy");
		params.put("age", "50");
		params.put("timestamp", "100");
		invoker.invoke("userWebService", "say", params);
		verifyOutput("UserWebServiceImpl.say() name:jjyy age:50 timestamp:100");
		
		
		ObjectMapper mapper = new ObjectMapper();
	}

	@Test
	public void testInvokeSayWithArguments() throws Exception, IllegalAccessException, InvocationTargetException {
		params.put(MethodInvoker.KEY_PARAMETERS, "jjyy;50;100");
		invoker.invoke("userWebService", "say", params);
		verifyOutput("UserWebServiceImpl.say() name:jjyy age:50 timestamp:100");
	}

	@Test
	public void testInvokeSayWithJsonArguments() throws Exception, IllegalAccessException, InvocationTargetException {
		params.put(MethodInvoker.KEY_PROTOCOL, "json");
		params.put(MethodInvoker.KEY_PARAMETERS, "[\"jjyy\",50,100]");
		invoker.invoke("userWebService", "say", params);
		verifyOutput("UserWebServiceImpl.say() name:jjyy age:50 timestamp:100");
	}
	
	@Test
	public void testInvokeBye() throws Exception, IllegalAccessException, InvocationTargetException {
		invoker.invoke("userWebService", "bye", params);
		verifyOutput("UserWebServiceImpl.bye() p11:0 p21:0 p31:0");
	}
	
	@Test
	public void testArrayAndListArgument() throws Exception, IllegalAccessException, InvocationTargetException {
		params.put("name", "badqiu");
		params.put("age", "1,2");
		params.put("timestamp", "4,5");
		invoker.invoke("userWebService", "hello", params);
		verifyOutput("UserWebServiceImpl.hello() name:[badqiu] age:[1, 2] timestamp:[4, 5]");
		
		params.put("name", "badqiu");
		params.put("age", "1");
		params.put("timestamp","4");
		invoker.invoke("userWebService", "hello", params);
		verifyOutput("UserWebServiceImpl.hello() name:[badqiu] age:[1] timestamp:[4]");
	}
	
	@Test
	public void testMapArgument() throws Exception, IllegalAccessException, InvocationTargetException {
		params.put("map", "key1:value,key2:value2");
		params.put("age", "12");
		params.put("name", "badqiu");
		invoker.invoke("userWebService", "mapArgument", params);
		verifyOutput("UserWebServiceImpl.mapArgument() name:[badqiu] age:[12] map:{key2=value2, key1=value}");
	}
	
	@Test
	public void testObjectArgument() throws Exception, IllegalAccessException, InvocationTargetException {
		params.put("userInfo", "weight:100,name:jjyy,age:50");
		params.put("age", "12");
		params.put("name", "badqiu");
		invoker.invoke("userWebService", "objectArgument", params);
		verifyOutput("UserWebServiceImpl.objectArgument() name:badqiu age:12 userInfo:{weight=100, name=jjyy, age=50, class=class com.duowan.common.rpc.fortestinvoker.UserInfo}");
	}
	
	@Test
	public void testDateArgument() throws Exception, IllegalAccessException, InvocationTargetException {
		params.put("date", "19980101235959");
		params.put("timestamp", "19990101235959888");
		params.put("sqlDate", "20000101235959");
		params.put("time", "20010101235959");
		
		invoker.invoke("userWebService", "dateArgument", params);
		verifyOutput("dateArgument() date:1998-01-01 23:59:59 timestamp:1999-01-01 23:59:59.888 time:23:59:59 sqlDate:2000-01-01");
	}

	@Test
	public void testDateArgument2() throws Exception, IllegalAccessException, InvocationTargetException {
		params.put("date", "1998-01-01 23:59:59");
		params.put("timestamp", "1999-01-01 23:59:59.888");
		params.put("sqlDate", "2000-01-01 23:59:59");
		params.put("time", "2001-01-01 23:59:59");
		
		invoker.invoke("userWebService", "dateArgument", params);
		verifyOutput("dateArgument() date:1998-01-01 23:59:59 timestamp:1999-01-01 23:59:59.888 time:23:59:59 sqlDate:2000-01-01");
	}

	@Test
	public void testDateArgument3() throws Exception, IllegalAccessException, InvocationTargetException {
		params.put("date", "1998-01-01 23:59:59");
		params.put("timestamp", "1999-01-01 23:59:59.888");
		params.put("sqlDate", "2000-01-01 23:59:59");
		params.put("sqlTime", "2001-01-01 23:59:59");
		params.put("charValue", "1");
		
		String string = (String)invoker.invoke("userWebService", "singleBlog", params);
		assertEquals(string,"Blog [id=null, charValue=1, character=null, byteValue=0, byteObject=null, shortValue=0, shortObject=null, intValue=0, integer=null, longValue=0, longObject=null, doubleValue=0.0, doubleObject=null, floatValue=0.0, floatObject=null, bigDecimal=null, bigInteger=null, date=Thu Jan 01 23:59:59 CST 1998, sqlDate=2000-01-01, sqlTime=23:59:59, timestamp=1999-01-01 23:59:59.888]");
	}
	
	@Test
	public void testPerf() throws Exception, IllegalAccessException, InvocationTargetException {
		params.put("date", "1998-01-01 23:59:59");
		params.put("timestamp", "1999-01-01 23:59:59.888");
		params.put("sqlDate", "2000-01-01 23:59:59");
		params.put("sqlTime", "2001-01-01 23:59:59");
		params.put("charValue", "1");
		
		long start = System.currentTimeMillis();
		int count = 100000;
		for(int i = 0; i < count; i++) {
			String string = (String)invoker.invoke("userWebService", "singleBlog", params);
		}
		long cost = System.currentTimeMillis() - start;
		
		;
		System.out.println("cost:"+cost+" count:"+count+ " tps:"+(count * 1000.0 / cost));
		
	}
	
	@Test
	public void testPerf2() throws Exception, IllegalAccessException, InvocationTargetException {
		params.put("tdate", "1998-01-01 23:59:59");
		params.put("age", "1");
		params.put("big", "100");
		params.put("rate", "1000");
		params.put("f", "10");
		
		long start = System.currentTimeMillis();
		int count = 100000;
		for(int i = 0; i < count; i++) {
			String string = (String)invoker.invoke("userWebService", "simple", params);
		}
		long cost = System.currentTimeMillis() - start;
		
		;
		System.out.println("cost:"+cost+" count:"+count+ " tps:"+(count * 1000.0 / cost));
		
	}
	
	@Test
	public void testEnumArgument() throws Exception, IllegalAccessException, InvocationTargetException {
		params.put("userTypes", "SYSTEM,CUSTOM");
		params.put("userType", "SYSTEM");
		
		invoker.invoke("userWebService", "enumArgument", params);
		verifyOutput("enumArgument() userType:SYSTEM userTypes:[SYSTEM, CUSTOM]");
		
		params.put("userTypes", "SYSTEM,NOT_EXIST_ENUM");
		try {
			invoker.invoke("userWebService", "enumArgument", params);
			verifyOutput("enumArgument() userType:SYSTEM userTypes:[SYSTEM, CUSTOM]");
			fail();
		}catch(ConversionFailedException e) {
			assertTrue(true);
		}
	}
	
	
	public void verifyOutput(String str) {
		String actual = output.toString();
		output.reset();
		params.clear();
		assertTrue(str+"\n"+actual,actual.replaceAll("\\s*", "").equals(str.replaceAll("\\s*", "")));
	}
}
