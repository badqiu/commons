package com.github.rapid.common.xstream;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.util.ResourceUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.javabean.JavaBeanConverter;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class CustomMarshallingStrategyTest {

	Map vars = new HashMap();
	ApplicationContext applicationContext;
	
	@Before
	public void setUp() {
		applicationContext = new ClassPathXmlApplicationContext("classpath:fortest_xstream/applicationContext-resource.xml");
	}
	@Test
	public void test() throws Exception {
		vars.put("ds1", new DriverManagerDataSource());
		vars.put("ds2", new DriverManagerDataSource());
		
		XStream xstream = buildXStream();
		
		InputStream input = new FileInputStream(ResourceUtils.getFile("classpath:fortest_xstream/xstream_test.xml"));
		XstreamTestUser testUser = (XstreamTestUser)fromXml(xstream, input);
		assertNotNull(testUser.getDs1());
		assertNotNull(testUser.getDs2());
		assertNotNull(testUser.getDs3());
		
		assertNull(testUser.getDs4());
		
		input.close();
	}
	
	@Test
	public void test_by_custom_converter() throws Exception {
		vars.put("ds1", new DriverManagerDataSource());
		vars.put("ds2", new DriverManagerDataSource());
		
		XStream xstream = buildXStream();
		xstream.registerConverter(new JavaBeanConverter(xstream.getMapper(),XstreamTestUser.class));
		
		InputStream input = new FileInputStream(ResourceUtils.getFile("classpath:fortest_xstream/xstream_test.xml"));
		XstreamTestUser testUser = (XstreamTestUser)fromXml(xstream, input);
		assertNotNull(testUser.getDs1());
		assertNotNull(testUser.getDs2());
		assertNotNull(testUser.getDs3());
		
		assertNull(testUser.getDs4());
		
		input.close();
	}
	
	

	private Object fromXml(XStream xstream, InputStream input) {
		CustomMarshallingStrategy marshallingStrategy = new CustomMarshallingStrategy();
		marshallingStrategy.setBeans(vars);
		marshallingStrategy.setApplicationContext(applicationContext);
		xstream.setMarshallingStrategy(marshallingStrategy);
		return xstream.fromXML(input);
	}
	
	private static XStream buildXStream() {
		XStream xstream = new XStream(new DomDriver());
		xstream.useAttributeFor(int.class);
		xstream.useAttributeFor(long.class);
		xstream.useAttributeFor(char.class);
		xstream.useAttributeFor(float.class);
		xstream.useAttributeFor(boolean.class);
		xstream.useAttributeFor(double.class);
		xstream.useAttributeFor(Integer.class);
		xstream.useAttributeFor(Long.class);
		xstream.useAttributeFor(Character.class);
		xstream.useAttributeFor(Float.class);
		xstream.useAttributeFor(Double.class);
		xstream.useAttributeFor(Boolean.class);
		xstream.useAttributeFor(String.class);
		
		return xstream;
	}

}
