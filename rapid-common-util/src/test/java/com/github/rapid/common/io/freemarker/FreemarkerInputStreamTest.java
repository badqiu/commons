package com.github.rapid.common.io.freemarker;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.helpers.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import com.github.rapid.common.io.freemarker.FreemarkerInputStream;


public class FreemarkerInputStreamTest extends Assert {
	
	@Test
	public void test() throws IOException {
		String content = "${java_vm_specification_version} ${name}";
		Map map = new HashMap();
		map.put("name", "jjyy");
		FreemarkerInputStream input = new FreemarkerInputStream(new ByteArrayInputStream(content.getBytes()),map);
		assertEquals(IOUtils.toString(input),"1.8 jjyy");
		
		input = new FreemarkerInputStream(new ByteArrayInputStream("${java_vm_specification_version}".getBytes()),(Map)null);
	}
}
