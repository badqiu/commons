package com.github.rapid.common.rpc.client;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.rapid.common.rpc.WebServiceException;
import com.github.rapid.common.rpc.fortest.api.BlogInfoService;
import com.github.rapid.common.rpc.fortest.api.BlogInfoServiceImpl;
import com.github.rapid.common.rpc.fortest.api.model.Blog;
import com.github.rapid.common.rpc.fortest.api.request.BlogQuery;
import com.github.rapid.common.rpc.fortestinvoker.UserWebService;
import com.github.rapid.common.rpc.tools.JettyServer;


public class RPCProxyFactoryBeanTest extends BaseClientTestCase{
	
	UserWebService userWebService = null;
	BlogInfoService httpSimpleBlogInfoService = null;
	BlogInfoService blogInfoService = null;
	BlogInfoService localBlogInfoService = new BlogInfoServiceImpl();
	
	Map httpHeaders = new HashMap();
	@BeforeClass
	public static void beforeClass() throws Exception {
		JettyServer.main(null);
	}
	
	@Before
	public void setUp() {
		RPCProxyFactoryBean commonHttpFactory = new RPCProxyFactoryBean();
		commonHttpFactory.setServiceInterface(UserWebService.class);
		commonHttpFactory.setHttpInvokerRequestExecutor(new CommonsHttpInvokerRequestExecutor());
		commonHttpFactory.setServiceUrl("http://localhost:26060/services/userWebService");
		commonHttpFactory.setRetryIntervalMills(new int[]{100,200});
		commonHttpFactory.setHttpHeaders(httpHeaders);
		commonHttpFactory.afterPropertiesSet();
		userWebService = (UserWebService)commonHttpFactory.getObject();
		
		ApplicationContext context = new ClassPathXmlApplicationContext("client/rpc-client.xml");
		blogInfoService = (BlogInfoService)context.getBean("blogInfoService");
		userWebService = (UserWebService)context.getBean("userWebService");
		
		RPCProxyFactoryBean simpleHttpFactory = new RPCProxyFactoryBean();
		simpleHttpFactory.setServiceInterface(com.github.rapid.common.rpc.fortest.api.BlogInfoService.class);
		simpleHttpFactory.setHttpInvokerRequestExecutor(new SimpleHttpInvokerRequestExecutor());
		simpleHttpFactory.setServiceUrl("http://localhost:26060/services/BlogInfoService");
		simpleHttpFactory.setHttpHeaders(httpHeaders);
		simpleHttpFactory.afterPropertiesSet();
		httpSimpleBlogInfoService = (BlogInfoService)simpleHttpFactory.getObject();
	}
	
	@Test
	public void test_say() {
		assertEquals("say()badqiu",userWebService.say("badqiu", 12, 928282L));
	}
	
	@Test
	public void test_findByBlogQuery() {
		BlogQuery query = new BlogQuery();
		query.setUsername(";;abc:123&");
		assertEquals(localBlogInfoService.findByBlogQuery(query),blogInfoService.findByBlogQuery(query));
	}
	
	@Test
	public void test_say2() {
		userWebService.say("中国,人民银行", 12, 928282L);
		userWebService.say("中国,人民;银:行", 12, 928282L);
	}
	
	@Test
	public void test_findSingleBlog() throws Exception, InvocationTargetException, NoSuchMethodException {
		Blog blog = blogInfoService.findSingleBlog("group", "100");
		System.out.println(BeanUtils.describe(blog));
		System.out.println(BeanUtils.describe(BlogInfoServiceImpl.createBlog()));
		assertEquals(BlogInfoServiceImpl.createBlog(),blog);
	}
	
	@Test
	public void test_http_header() throws Exception, InvocationTargetException, NoSuchMethodException {
		httpHeaders.put("access-token", "secure-abc123");
		String remoteResult = httpSimpleBlogInfoService.httpHeader("hello");
		assertEquals(remoteResult,"accessToken:secure-abc123 info:hello");
		
		
		RPCProxyFactoryBean commonHttpFactory = new RPCProxyFactoryBean();
		commonHttpFactory.setServiceInterface(com.github.rapid.common.rpc.fortest.api.BlogInfoService.class);
		CommonsHttpInvokerRequestExecutor httpInvokerRequestExecutor = new CommonsHttpInvokerRequestExecutor();
		httpInvokerRequestExecutor.afterPropertiesSet();
		commonHttpFactory.setHttpInvokerRequestExecutor(httpInvokerRequestExecutor);
		commonHttpFactory.setServiceUrl("http://localhost:26060/services/BlogInfoService");
		commonHttpFactory.setHttpHeaders(httpHeaders);
		commonHttpFactory.afterPropertiesSet();
		BlogInfoService httpCommonBlogInfoService = (BlogInfoService)commonHttpFactory.getObject();
		
		httpHeaders.put("access-token", "secure-diy2");
		String remoteResult2 = httpCommonBlogInfoService.httpHeader("hello");
		assertEquals(remoteResult2,"accessToken:secure-diy2 info:hello");
	}
	
	@Test
	public void test_js() throws Exception, InvocationTargetException, NoSuchMethodException {
		String str = readURL("http://localhost:26060/services/BlogInfoService/findDate?__format=jsonp&__jsoncallback=findDateCallback");
		assertEquals("findDateCallback({\"result\":199999999})",str);
	}
	
	public static String readURL(String urladdress) {
		GetMethod method = new GetMethod(urladdress);
		method.setFollowRedirects(true);
		method.setRequestHeader("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; .NET CLR 1.1.4322)");
		method.setRequestHeader("Accept-Language", "zh-cn");

		try {
			new HttpClient().executeMethod(method);
			return method.getResponseBodyAsString();
		} catch (Exception e) {
			throw new RuntimeException("error on url:" + urladdress, e);
		} finally {
			method.releaseConnection();
		}
	}

	@Test
	public void test_complexArguments() {
		Blog blog = blogInfoService.findByBlogQuery(null);
		Map parameters = new HashMap();
		parameters.put("abc", "123");
		String[] names = new String[]{"badqiu","jj"};
		int[] args = new int[]{1,2};
		
		String result = blogInfoService.complexArguments(blog, names, args, parameters, null);
		assertNotNull(result);
		assertEquals(localBlogInfoService.complexArguments(blog, names, args, parameters, null),result);
	}
	
	String specialChars = "!@####$%%^&*(\", age)(*&))(*&^$#@!%~`[{]}'\\\"";
	String genComprexChars = genComprexChars();
	@Test
	public void test_local_equals_remote() throws Exception, InvocationTargetException, NoSuchMethodException {
		assertEquals(localBlogInfoService.emptyParam(),blogInfoService.emptyParam());
		assertEquals(localBlogInfoService.returnBlogQuery(new BlogQuery("badqiu","pwd",18)),blogInfoService.returnBlogQuery(new BlogQuery("badqiu","pwd",18)));
		
		assertEquals(localBlogInfoService.findByBlogQuery(null),blogInfoService.findByBlogQuery(null));
		assertEquals(localBlogInfoService.findBlogCollection("key"),blogInfoService.findBlogCollection("key"));
		assertEquals(localBlogInfoService.findBlogList("key"),blogInfoService.findBlogList("key"));
		assertEquals(localBlogInfoService.findBlogLinkedList(null),blogInfoService.findBlogLinkedList(null));
		assertEquals(localBlogInfoService.genBlogList(100),blogInfoService.genBlogList(100));
		assertEquals(localBlogInfoService.findBlogListMap(null),blogInfoService.findBlogListMap(null));
		
		
		
		assertEquals(localBlogInfoService.findBlogMap("key"),blogInfoService.findBlogMap("key"));
		assertArrayEquals(localBlogInfoService.findBlogArray("key"),blogInfoService.findBlogArray("key"));
		assertEquals(localBlogInfoService.findBlogMapList("key"),blogInfoService.findBlogMapList("key"));
		assertEquals(localBlogInfoService.findBlogLinkedHashMap("key"),blogInfoService.findBlogLinkedHashMap("key"));
		assertEquals(localBlogInfoService.findUserTypeEnum("key"),blogInfoService.findUserTypeEnum("key"));
		assertArrayEquals(localBlogInfoService.findUserTypeEnumArray("key"),blogInfoService.findUserTypeEnumArray("key"));
		assertEquals(localBlogInfoService.findDate("key"),blogInfoService.findDate("key"));
		assertEquals(localBlogInfoService.findDouble("key"),blogInfoService.findDouble("key"),0.0001);
		assertEquals(localBlogInfoService.findDoubleObject("key"),blogInfoService.findDoubleObject("key"));
		assertEquals(localBlogInfoService.AComplex__methodDDDD123Name(),blogInfoService.AComplex__methodDDDD123Name());
		assertEquals(localBlogInfoService.return_input("中国人民银行;123"),blogInfoService.return_input("中国人民银行;123"));
		

		
		assertEquals(localBlogInfoService.tree_set("123").toString(),blogInfoService.tree_set("123").toString());
		assertTrue(localBlogInfoService.tree_set_but_return_set("123").toString().equals(new TreeSet(blogInfoService.tree_set_but_return_set("123")).toString()));
		assertEquals(localBlogInfoService.linked_hash_set("123").toString(),blogInfoService.linked_hash_set("123").toString());
		
		assertEquals(localBlogInfoService.AComplex__methodDDDD123Name(),httpSimpleBlogInfoService.AComplex__methodDDDD123Name());
		
		assertEquals(localBlogInfoService.return_exception().toString(),blogInfoService.return_exception().toString());

		assertEquals(new TreeMap(localBlogInfoService.map_arg(new HashMap<String,String>())).toString(),new TreeMap(blogInfoService.map_arg(new HashMap<String,String>())).toString());
		assertEquals(localBlogInfoService.list_arg(new ArrayList()).toString(),blogInfoService.list_arg(new ArrayList()).toString());
		assertEquals(Arrays.toString(localBlogInfoService.array_arg(new String[]{"1","2"})),Arrays.toString(blogInfoService.array_arg(new String[]{"1","2"})));
		
		blogInfoService.void_method();
		assertNull(blogInfoService.null_return());
//		assertEquals(localBlogInfoService.findComplexObject("key"),blogInfoService.findComplexObject("key"));
		assertFalse(localBlogInfoService.linked_hash_set_but_return_set("123").toString().equals(blogInfoService.linked_hash_set_but_return_set("123").toString()));
		
		assertEquals(localBlogInfoService.pageQuery(1, 5).getPaginator().toString(),blogInfoService.pageQuery(1, 5).getPaginator().toString());
		assertEquals(localBlogInfoService.pageQuery(4, 5).getPaginator().toString(),blogInfoService.pageQuery(4, 5).getPaginator().toString());
		
		assertFalse(localBlogInfoService.nullValueMapList("key").equals(blogInfoService.nullValueMapList("key")));
		assertTrue("remote:"+blogInfoService.emptyValueMapList("key")+" local:"+localBlogInfoService.emptyValueMapList("key"),localBlogInfoService.emptyValueMapList("key").equals(blogInfoService.emptyValueMapList("key")));
		
		assertEquals(localBlogInfoService.tree_map("123").toString(),blogInfoService.tree_map("123").toString());
		assertEquals(localBlogInfoService.tree_map_but_return_map("123").toString(),new TreeMap(blogInfoService.tree_map_but_return_map("123")).toString());
		assertEquals(localBlogInfoService.linked_hash_map("123").toString(),blogInfoService.linked_hash_map("123").toString());
		assertEquals(localBlogInfoService.linked_hash_map_but_return_map("123").toString(),blogInfoService.linked_hash_map_but_return_map("123").toString());
		
		assertEquals(localBlogInfoService.return_input_2_arg(specialChars,specialChars),blogInfoService.return_input_2_arg(specialChars,specialChars));
		assertEquals(localBlogInfoService.return_input_2_arg(genComprexChars,genComprexChars),blogInfoService.return_input_2_arg(genComprexChars,genComprexChars));
	}
	
	private String genComprexChars() {
		StringBuilder sb = new StringBuilder(65538);
		System.out.println("Character.MIN_VALUE:"+(int)Character.MIN_VALUE+" Character.MAX_VALUE:"+(int)Character.MAX_VALUE);
		for(int i = Character.MIN_VALUE; i <= Character.MAX_VALUE;i++) {
			sb.append((char)i);
		}
		return sb.toString();
	}
	
	@Test
	public void test_big_data() {
		//FIXME 修复大数据错误
//		assertEquals(localBlogInfoService.return_input_2_arg(specialChars,StringUtils.repeat(specialChars,5000)),blogInfoService.return_input_2_arg(specialChars,StringUtils.repeat(specialChars,5000)));
	}
	
	@Test
	public void test_performance() throws InvocationTargetException, NoSuchMethodException, Exception {
		blogInfoService.findBlogList("key");
		long start = System.currentTimeMillis();
		float count = 3000;
		for(int i = 0; i < count; i++) {
//			test_ListMapResult();
			blogInfoService.findBlogList("key");
		}
		long cost = System.currentTimeMillis() - start;
		System.out.println("cost:"+cost+" tps:"+ (count / cost * 1000));
	}
	
	@Test
	public void test_throwException() throws Exception, InvocationTargetException, NoSuchMethodException {
		try {
			blogInfoService.throwException("key");
			fail();
		}catch(WebServiceException e) {
			assertEquals(e.getErrorNo(),"RuntimeException");
			assertEquals(e.getMessage(),"custom_unknow_error");
		}
		
		try {
			blogInfoService.throwWebServiceException("key");
			fail();
		}catch(WebServiceException e) {
			e.printStackTrace();
			assertEquals(e.getErrorNo(),"test_error_no");
			assertEquals(e.getMessage(),"test_error_message");
		}
		
		try {
			blogInfoService.voidThrowException("key");
			fail();
		}catch(WebServiceException e) {
			e.printStackTrace();
			assertEquals(e.getErrorNo(),"RuntimeException");
			assertEquals(e.getMessage(),"voidThrowException");
		}
	}
	
	@Test(expected=WebServiceException.class)
	public void test_retry() throws Exception, InvocationTargetException, NoSuchMethodException {
		RPCProxyFactoryBean factory = new RPCProxyFactoryBean();
		factory.setServiceInterface(UserWebService.class);
		factory.setHttpInvokerRequestExecutor(new CommonsHttpInvokerRequestExecutor());
		factory.setServiceUrl("http://localhost:26111/services/userWebService");
		factory.setRetryIntervalMills(new int[]{100,200});
		factory.afterPropertiesSet();
		userWebService = (UserWebService)factory.getObject();
		userWebService.hello(new String[]{"name"}, null, null);
	}
}
