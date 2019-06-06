package com.github.rapid.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class ExcelGeneratorTest {

	ExcelGenerator g = new ExcelGenerator();
	@Test
	public void test() throws Exception {
		OutputStream output = new FileOutputStream(new File("/tmp/test_output.xls"));
		LinkedHashMap<String, String> headMap = new LinkedHashMap();
		headMap.put("name", "姓名");
		headMap.put("sex", "姓别");
		headMap.put("age", "年龄");
		headMap.put("create_time", "创建时间");
		headMap.put("mills", "mills时间");
		headMap.put("doubleValue", "doubleValue");
		headMap.put("man", "是否男性");
		headMap.put("floatValue", "floatValue");
		
		List datas = new ArrayList();
		for(int i = 0; i < 100; i++) {
			Map data = new HashMap();
			data.put("name", "name_"+i);
			data.put("sex", "sex"+i);
			data.put("age", +i);
			data.put("high", "name_"+i);
			data.put("man", false);
			data.put("create_time", new Date());
			data.put("mills", System.currentTimeMillis());
			data.put("doubleValue", Double.MAX_VALUE);
			data.put("floatValue", 99.128219832);
			datas.add(data);
		}
		
		g.execute("测试表格",datas, headMap, output);
	}

}
