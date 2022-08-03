package com.github.rapid.common.rpc.server;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.io.output.TeeOutputStream;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.util.StringUtils;

import com.github.rapid.common.rpc.fortestinvoker.UserInfo;
import com.github.rapid.common.rpc.fortestinvoker.UserTypeEnum;
import com.github.rapid.common.rpc.fortestinvoker.UserWebService;
import com.github.rapid.common.rpc.fortestinvoker.UserWebServiceImpl;
import com.github.rapid.common.rpc.serde.SimpleSerDeImpl;
import com.github.rapid.common.rpc.server.MethodInvoker;

public class MethodInvokerSerTest extends Assert{
	
	MethodInvoker invoker = new MethodInvoker();
	UserWebService userWebService = null;
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	Map<String,Object> params = new HashMap<String,Object>();
	
	@Before
	public void setUp() {
		output.reset();
		
		Map<String,Object> serviceMapping = new HashMap<String,Object>();
		System.setOut(new PrintStream(new TeeOutputStream(output,System.out)));
		serviceMapping.put("userWebService", new UserWebServiceImpl());
		invoker.setServiceMapping(serviceMapping);
		
		ProxyFactoryBean proxy = new ProxyFactoryBean();
		proxy.setInterfaces(new Class[]{UserWebService.class});
		proxy.addAdvice(new MethodInterceptor() {
			public Object invoke(MethodInvocation invocation) throws Throwable {
				Map<String,Object> parameters = new SimpleSerDeImpl().serializeForParametersMap(invocation.getArguments());
				String serviceId = StringUtils.uncapitalize(invocation.getMethod().getDeclaringClass().getSimpleName());
				return invoker.invoke(serviceId, invocation.getMethod().getName(), parameters);
			};
		});
		userWebService = (UserWebService)proxy.getObject();
		
	}
	
	@Test
	public void test_bye() {
		userWebService.bye(1L, 2L, 3, 4, (byte)5, (byte)6, true, false, 9d, 21d, 'c', null, null);
		verifyOutput("UserWebServiceImpl.bye() p11:1 p21:3 p31:5");
	}
	
	@Test
	public void test_hello() {
		userWebService.hello(new String[]{"qq","yy"}, new int[]{1,2}, Arrays.asList(999L,888L));
		verifyOutput("UserWebServiceImpl.hello() name:[qq, yy] age:[1, 2] timestamp:[999, 888]");
	}
	
	@Test
	public void test_say() throws ParseException {
		userWebService.say("badqiu", Integer.MIN_VALUE, Long.MAX_VALUE);
		verifyOutput("UserWebServiceImpl.say() name:badqiu age:-2147483648 timestamp:9223372036854775807");
	}
	
	@Test
	public void test_dateArgument() throws ParseException {
		Date date = DateUtils.parseDate("20111111 23:59:59.999", new String[]{"yyyyMMdd HH:mm:ss.SSS"});
		userWebService.dateArgument(date,new Timestamp(date.getTime()),new Time(date.getTime()),new java.sql.Date(date.getTime()));
		verifyOutput("dateArgument() date:2011-11-11 23:59:59 timestamp:2011-11-11 23:59:59.999 time:23:59:59 sqlDate:2011-11-11");
	}
	
	@Test
	public void test_notArgument() throws ParseException {
		userWebService.notArgument();
		verifyOutput("notArgument()");
	}
	
	@Test
	public void test_enumArgument() throws ParseException {
		userWebService.enumArgument(UserTypeEnum.CUSTOM,new UserTypeEnum[]{UserTypeEnum.SYSTEM,UserTypeEnum.CUSTOM});
		verifyOutput("enumArgument() userType:CUSTOM userTypes:[SYSTEM, CUSTOM]");
	}
	
	@Test
	public void test_objectArgument() throws ParseException {
		userWebService.objectArgument("badqiu",23,null);
		verifyOutput("UserWebServiceImpl.objectArgument() name:badqiu age:23 userInfo:{}");
		
		UserInfo userInfo = new UserInfo();
		userInfo.setWeight(999);
		userInfo.setName("jjyy");
		userInfo.setAge(888);
		userWebService.objectArgument("badqiu",23,userInfo);
		verifyOutput("UserWebServiceImpl.objectArgument() name:badqiu age:23 userInfo:{weight=999, name=jjyy, age=888, class=class com.duowan.common.rpc.fortestinvoker.UserInfo}");
	}

	@Test
	public void test_MapArgument() throws ParseException {
		
		userWebService.mapArgument(new String[]{"badqiu","jjyy"}, new int[]{111,222}, null);
		verifyOutput("UserWebServiceImpl.mapArgument() name:[badqiu, jjyy] age:[111, 222] map:{}");
		
		Map map = new HashMap();
		map.put("int1", 1);
		map.put("key2", "value2");
		map.put("key1", "value1");
		userWebService.mapArgument(new String[]{"badqiu","jjyy"}, new int[]{111,222}, map);
		verifyOutput("UserWebServiceImpl.mapArgument() name:[badqiu, jjyy] age:[111, 222] map:{key1=value1, key2=value2, int1=1}");
		
		map = new HashMap();
		userWebService.mapArgument(new String[]{"badqiu","jjyy"}, new int[]{111,222}, map);
		verifyOutput("UserWebServiceImpl.mapArgument() name:[badqiu, jjyy] age:[111, 222] map:null");

	}
	
	public void verifyOutput(String str) {
		String actual = output.toString();
		output.reset();
		params.clear();
		String message = "\n"+str+"\n"+actual;
		String expected = actual.replaceAll("\\s*", "");
		String right = str.replaceAll("\\s*", "");
		assertEquals(message, expected, right);
	}
}
