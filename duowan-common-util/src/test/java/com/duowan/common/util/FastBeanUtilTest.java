package com.duowan.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.converters.DateConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;


public class FastBeanUtilTest {
	public @Rule TestName testName = new TestName();
	
	@Before
	public void setUp() {
		System.out.println("\n\n---------------------"+testName.getMethodName()+"---------------------------");
		ConvertUtils.register(new DateConverter(null),java.util.Date.class);
		Profiler.start();
	}
	
	@After
	public void tearDown() {
		Profiler.release();
		System.out.println(Profiler.dump());
	}
	
	@Test
	public void test_copyProperties_pref() throws Exception{
		FastBean source = new FastBean();
		FastBean target = new FastBean();
		int loopCount = 100000;
		
		Profiler.enter("FastBeanUtil.copyProperties",loopCount);
		for(int i = 0; i < loopCount; i++) {
			FastBeanUtil.copyProperties(source, target);
		}
		Profiler.release();
		
		Profiler.enter("BeanUtils.copyProperties",loopCount);
		for(int i = 0; i < loopCount; i++) {
			BeanUtils.copyProperties(source, target);
		}
		Profiler.release();
		
		Profiler.enter("PropertyUtils.copyProperties",loopCount);
		for(int i = 0; i < loopCount; i++) {
			PropertyUtils.copyProperties(source, target);
		}
		Profiler.release();
	}
	
	@Test
	public void test_describe_pref() throws Exception{
		FastBean source = new FastBean();
		int loopCount = 100000;
		
		Profiler.enter("FastBeanUtil.describe",loopCount);
		for(int i = 0; i < loopCount; i++) {
			FastBeanUtil.describe(source);
		}
		Profiler.release();
		
		Profiler.enter("BeanUtils.describe",loopCount);
		for(int i = 0; i < loopCount; i++) {
			BeanUtils.describe(source);
		}
		Profiler.release();
		
		Profiler.enter("PropertyUtils.describe",loopCount);
		for(int i = 0; i < loopCount; i++) {
			PropertyUtils.describe(source);
		}
		Profiler.release();
	}
	
	
	@Test
	public void test_populate_pref() throws Exception{
		FastBean source = new FastBean();
		Map map = FastBeanUtil.describe(source);
		int loopCount = 100000;
		
		Profiler.enter("FastBeanUtil.populate",loopCount);
		for(int i = 0; i < loopCount; i++) {
			FastBeanUtil.populate(source,map);
		}
		Profiler.release();
		
		Profiler.enter("BeanUtils.populate",loopCount);
		for(int i = 0; i < loopCount; i++) {
			BeanUtils.populate(source,map);
		}
		Profiler.release();
		
	}
	
	@Test
	public void test_date_parse_perf() throws Exception{
		int loopCount = 100000;
		Date date = new Date();
		String strDate = "2012-10-10 10:10:10";
		
		Profiler.enter("SimpleDateFormat.parse",loopCount);
		for(int i = 0; i < loopCount; i++) {
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate);
		}
		Profiler.release();
		
		Profiler.enter("SimpleDateFormat.format",loopCount);
		for(int i = 0; i < loopCount; i++) {
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
		}
		Profiler.release();
	}
	
}
