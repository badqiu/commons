package com.github.rapid.common.util;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.springframework.util.ResourceUtils;

public class ExcelUtilTest {

	@Rule public TestName testName = new TestName();
	
	@Before
	public void before() {
		System.out.println("\n------------------ "+testName.getMethodName()+" ----------------------\n");
	}
	
	@Test
	public void test_excel_2003() throws Exception {
		String filename = "classpath:excel_test/test_2003.xls";
		Map row = testExcel(filename);
		assertEquals(row.get("name"),"张三");
		assertEquals(row.get("sum"),"42");
		assertEquals("{name=张三, sex=f, age=20, power=2.1, sum=42, full_name=null, mobile=13700001111}",row.toString());
	}
	
	@Test
	public void test_excel_2007() throws Exception {
		String filename = "classpath:excel_test/test_2007.xlsx";
		Map row = testExcel(filename);
		assertEquals(row.get("name"),"张三");
		assertEquals(row.get("sum"),"40");
	}

	private Map testExcel(String filename) throws FileNotFoundException, Exception {
		File file = ResourceUtils.getFile(filename);
		InputStream input = new FileInputStream(file);
		List<Map> rows = ExcelUtil.readExcelData(input,"name,sex,age,power,sum,full_name,mobile",1);
		for(Map row : rows) {
			System.out.println(row);
		}
		Map row = rows.get(0);
		return row;
	}

}
