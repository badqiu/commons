package com.duowan.common.log.aop;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.duowan.common.util.Profiler;


public class ProfiledAnnotationPointcutAdvisorTest extends Assert {
	public @Rule TestName testName = new TestName();
	List<String> list = new ArrayList();
	ProfiledAnnotationPointcutAdvisor advisor = new ProfiledAnnotationPointcutAdvisor();
	
	ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
	@Before
	public void setUp() {
		Profiler.start(testName.getMethodName());
		for(int i = 0; i < 29382; i++) {
			list.add(""+RandomUtils.nextInt());
		}
	}
	
	@Test
	public void test_ProfiledAnnotationPointcutAdvisor() throws Exception {
		BlogServer server = ProfilerMethodInteceptorTest.createProxy(new BlogServer(), null,advisor);
		testBlogServer(server);
		testBlogServer(context.getBean(BlogServer.class));
	}

	private void testBlogServer(BlogServer server) throws InterruptedException {
		server.array_loop(1, new String[]{"1","2"});
		server.list_loop(1, list, null);
		assertTrue(Profiler.dump().contains("BlogServer.array_loop"));
		assertFalse(Profiler.dump().contains("BlogServer.list_loop"));
	}
	
	@Test
	public void test_ProfiledAnnotationPointcutAdvisor_with_annotation_class() throws Exception {
		AnnotaionClassBlogServer server = ProfilerMethodInteceptorTest.createProxy(new AnnotaionClassBlogServer(), null,advisor);
		testAnnotationClassBlogServer(server);
		testAnnotationClassBlogServer(context.getBean(AnnotaionClassBlogServer.class));
	}

	private void testAnnotationClassBlogServer(AnnotaionClassBlogServer server)
			throws InterruptedException {
		server.array_loop(1, new String[]{"1","2"});
		server.list_loop(1, list, null);
		assertTrue(Profiler.dump().contains("AnnotaionClassBlogServer.array_loop"));
		assertTrue(Profiler.dump().contains("AnnotaionClassBlogServer.list_loop"));
	}
	
	@Test
	public void test_ProfilerMethodInteceptor() throws Exception {
		ProfilerMethodInteceptor inteceptor = new ProfilerMethodInteceptor();
		BlogServer server = ProfilerMethodInteceptorTest.createProxy(new BlogServer(), inteceptor,null);
		server.array_loop(1, new String[]{"1","2"});
		server.list_loop(1, list, null);
		assertTrue(Profiler.dump().contains("BlogServer.array_loop"));
		assertTrue(Profiler.dump().contains("BlogServer.list_loop"));
	}
	
	@After
	public void tearDown() {
		Profiler.release();
		Profiler.printDump();
	}
}
