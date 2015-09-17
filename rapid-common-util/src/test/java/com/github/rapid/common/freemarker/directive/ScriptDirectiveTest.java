package com.github.rapid.common.freemarker.directive;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.springframework.util.ResourceUtils;

import freemarker.template.Configuration;

public class ScriptDirectiveTest {

	Configuration conf = new Configuration();
	FreemarkerTemplateProcessor processor = new FreemarkerTemplateProcessor();
	
	@Before
	public void setUp() throws FileNotFoundException, IOException{
		processor.setConfiguration(conf);
		conf.setSharedVariable("block", new BlockDirective());
		conf.setSharedVariable("override", new OverrideDirective());
		conf.setSharedVariable("extends", new ExtendsDirective());
		conf.setSharedVariable("super", new SuperDirective());
		conf.setSharedVariable("script", new ScriptDirective());
		File dir = ResourceUtils.getFile("classpath:fortest_freemarker");
		conf.setDirectoryForTemplateLoading(dir);
		System.out.println(dir.getAbsolutePath());
	}
	
	@Test
	public void test() {
		String str = processor.processTemplate("script_directive.ftl",new HashMap());
		assertEquals("name:badqiu,sex:",str.trim());
	}

}
