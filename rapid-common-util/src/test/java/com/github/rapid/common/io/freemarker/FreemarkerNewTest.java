package com.github.rapid.common.io.freemarker;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

import org.junit.Test;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.core.TemplateClassResolver;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerNewTest {

	@Test
	public void test() throws IOException, TemplateException {
		String template = "<#assign objectConstructor = \"freemarker.template.utility.ObjectConstructor\"?new()> "
				+ "<#assign nowObj2 = objectConstructor(\"java.util.Date\")> ${nowObj2?string('yyyy')}";
		Configuration conf = new Configuration();
		conf.setNewBuiltinClassResolver(TemplateClassResolver.ALLOWS_NOTHING_RESOLVER);
		
		HashMap model = new HashMap();
//		model.put("objectConstructor", new freemarker.template.utility.ObjectConstructor());
		String string = FreeMarkerTemplateUtils.processTemplateIntoString(new Template("test",new StringReader(template),conf), model);
		System.out.println(string);
	}

}
