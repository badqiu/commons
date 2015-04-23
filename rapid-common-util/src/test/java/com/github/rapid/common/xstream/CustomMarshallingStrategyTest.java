package com.github.rapid.common.xstream;

import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.util.ResourceUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class CustomMarshallingStrategyTest {

	Map vars;
	ApplicationContext applicationContext;
	
	@Test
	public void test() throws Exception {
		
		XStream xstream = buildXStream();
		
		InputStream input = new FileInputStream(ResourceUtils.getFile("classpath:fortest_xstream/xstream_test.xml"));
		
		fromXml(xstream, input);
		
		input.close();
	}

	private void fromXml(XStream xstream, InputStream input) {
		CustomMarshallingStrategy marshallingStrategy = new CustomMarshallingStrategy();
		marshallingStrategy.setBeans(vars);
		marshallingStrategy.setApplicationContext(applicationContext);
		xstream.setMarshallingStrategy(marshallingStrategy);
		xstream.fromXML(input);
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
