package com.duowan.common.io.freemarker;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.helpers.IOUtils;
import org.junit.Assert;
import org.junit.Test;


public class FreemarkerReaderTest extends Assert{

	@Test
	public void test() throws IOException {
		String content = "${java_vm_specification_version} ${name}";
		Map map = new HashMap();
		map.put("name", "jjyy");
		FreemarkerReader input = new FreemarkerReader(new StringReader(content),map);
		assertEquals(IOUtils.toString(input),"1.0 jjyy");
		
		input =  new FreemarkerReader(new StringReader("${java_vm_specification_version}"),(Map)null);
	}
	
}
