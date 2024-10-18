package com.github.rapid.common.util;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class SmartConfigParserTest {

	@Test
	public void testParseProperties() {
		Map map = SmartConfigParser.smartParseFile("config.properties", "name=jane\nage=20");
		assertEquals("{age=20, name=jane}",map.toString());
	}

	@Test
	public void testParseYaml() {
		Map map = SmartConfigParser.smartParseFile("config.yaml", "name=jane\nage=20");
		assertEquals("{age=20, name=jane}",map.toString());
	}
	
	@Test
	public void testParseXml() {
		Map map = SmartConfigParser.smartParseFile("config.xml", "<java.util.HashMap age=\"20\" name=\"jane\"></java.util.HashMap>");
		assertEquals("{age=20, name=jane}",map.toString());
	}
	
	@Test
	public void testParseJson() {
		Map map = SmartConfigParser.smartParseFile("config.json", "name=jane\nage=20");
		assertEquals("{age=20, name=jane}",map.toString());
	}
}
