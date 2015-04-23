package com.github.rapid.common.log.aop;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.aopalliance.aop.Advice;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.springframework.aop.Advisor;
import org.springframework.aop.framework.ProxyFactory;

import com.github.rapid.common.log.ProfilerDigestLog;
import com.github.rapid.common.log.ProfilerLogger;
import com.github.rapid.common.util.Profiler;


public class ProfilerMethodInteceptorTest extends Assert{
	
	public @Rule TestName testName = new TestName();
	

	
	@Before
	public void setUp() {
		System.out.println("\n\n---------------------"+testName.getMethodName()+"---------------------------");
		Profiler.start();
	}
	
	@After
	public void tearDown() {
		Profiler.release();
		System.out.println(Profiler.dump());
		System.out.println(ProfilerDigestLog.getDigestLog());
		ProfilerLogger.infoDigestLogAndDump();
	}
	
	@Test
	public void test() throws Throwable {
		
		
		ProfilerMethodInteceptor p = new ProfilerMethodInteceptor();
		Date proxy = createProxy(new Date(),p,null);
		assertTrue(proxy.before(new Date()));
		proxy.getTime();
		
		int loopCount = 1000000;
		Profiler.enter("System.currentTimeMillis",loopCount);
		for(int i = 0; i < loopCount; i++) {
			System.currentTimeMillis();
		}
		Profiler.release();
		
		
		
	}
	
	@Test
	public void test_blogServer() throws Throwable {
		List<String> list = new ArrayList();
		for(int i = 0; i < 29382; i++) {
			list.add(""+RandomUtils.nextInt());
		}
		
		Profiler.start();
		
		ProfilerMethodInteceptor p = new ProfilerMethodInteceptor();
		BlogServer proxy = createProxy(new BlogServer(),p,null);
		proxy.array_loop(100, list.toArray(new String[list.size()]));
		proxy.map_loop(100, new HashMap(),null);
		proxy.list_loop(100,list,null);
		
		Profiler.release();
		
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T createProxy(T target,Advice advice,Advisor advisor) {
		Profiler.enter("createProxy");
		ProxyFactory factory = new ProxyFactory();
		factory.setTarget(target);
		factory.setOptimize(true);
		if(advice != null ) {
			factory.addAdvice(advice);
		}
		if(advisor != null) {
			factory.addAdvisor(advisor);
		}
		T result = (T)factory.getProxy();
		Profiler.release();
		return result;
	}


}
