package com.duowan.common.spring;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ScriptFactoryBeanTest {

	@Test
	public void test() {
		ClassPathXmlApplicationContext ac = new ClassPathXmlApplicationContext("fortest_spring/applicationContext-testScriptFactoryBean.xml");
		Object stringBean = ac.getBean("stringBean");
		assertEquals("1_b",stringBean);
		
		Object stringBeanByJavascript = ac.getBean("stringBeanByJavascript");
		assertEquals("1_javascript",stringBeanByJavascript);
		
		Object langGroovyString = ac.getBean("langGroovyString");
		assertEquals("hello badqiu badqiu",langGroovyString.toString());
	}

}
