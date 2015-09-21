package com.github.rapid.common.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Properties;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class FreemarkerSqlGeneratorTest {

	@Test
	public void test() throws Exception {
		FreemarkerSqlGenerator fsg = new FreemarkerSqlGenerator();
		Properties p = new Properties();
		p = PropertiesLoaderUtils.loadProperties(new ClassPathResource("/freemarker_sql/DemoTable.sql.xml"));
		fsg.setProperties(p);
		fsg.afterPropertiesSet();
		
		String sql = fsg.getSql("demoTable.findPage", MapUtil.newMap("username","badqiu"));
		System.out.println(sql);
		assertTrue(sql.contains("and username = :username"));
		
		sql = fsg.getSql("demoTable.findPage", null);
		System.out.println(sql);
		assertTrue(sql.contains("id ,username ,age ,birth_date"));
		assertTrue(!sql.contains("and username = :username"));
	}

}
