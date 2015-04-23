package com.github.rapid.common.util;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import com.github.rapid.common.beanutils.BeanUtils;
import com.github.rapid.common.beanutils.PropertyUtils;
import com.github.rapid.common.spring.jdbc.BeanPropertyRowMapper;
import com.github.rapid.common.test.dbunit.DBUnitFlatXmlHelper;
import com.github.rapid.common.util.Application;
import com.github.rapid.common.util.Asserter;
import com.github.rapid.common.util.CollectionHelper;
import com.github.rapid.common.util.JVMUtil;
import com.github.rapid.common.util.MapAndObject;
import com.github.rapid.common.util.Money;
import com.github.rapid.common.util.Profiler;
import com.github.rapid.common.util.ScanClassUtils;
import com.github.rapid.common.util.ScriptEngineUtil;
import com.github.rapid.common.util.ValidationErrorsUtils;
import com.github.rapid.common.util.yymsg.YYMsnSender;

public class AsserterTest extends TestCase {

	public void test_notEmpty() {
		try {
			Asserter.hasLength("", new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		String str = null;
		try {
			Asserter.hasLength(str, new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		str = " ";
		Asserter.hasLength(str, new RuntimeException());
		
		str = "1";
		Asserter.hasLength(str, new RuntimeException());
	}

	public void test_notBlank() {
		try {
			Asserter.hasText("", new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		String str = null;
		try {
			Asserter.hasText(str, new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		str = " ";
		try {
			Asserter.hasText(str, new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		str = "\n\t\r\f";
		try {
			Asserter.hasText(str, new RuntimeException());
			fail();
		} catch (RuntimeException e) {
		}
		
		str = "1";
		Asserter.hasText(str, new RuntimeException());
	}
	
	@Test
	public void test2() throws ClassNotFoundException {
		ClassTestUtil.invokeAllMethods(Asserter.class);
		ClassTestUtil.invokeAllMethods(DBUnitFlatXmlHelper.class);
		ClassTestUtil.invokeAllMethods(BeanUtils.class);
		ClassTestUtil.invokeAllMethods(Money.class);
//		ClassTestUtil.invokeAllMethods(Flash.class);
//		ClassTestUtil.invokeAllMethods(DataSourceTemplateLoader.class);
//		ClassTestUtil.invokeAllMethods(FreemarkerTemplateProcessor.class);
		ClassTestUtil.invokeAllMethods(Application.class);
		ClassTestUtil.invokeAllMethods(YYMsnSender.class);
		ClassTestUtil.invokeAllMethods(CollectionHelper.class);
		ClassTestUtil.invokeAllMethods( ValidationErrorsUtils.class);
		ClassTestUtil.invokeAllMethods( ScriptEngineUtil.class);
		ClassTestUtil.invokeAllMethods( PropertyUtils.class);
		ClassTestUtil.invokeAllMethods( BeanPropertyRowMapper.class);
		ClassTestUtil.invokeAllMethods(  MapAndObject.class);
		ClassTestUtil.invokeAllMethods(Profiler.class);
		ClassTestUtil.invokeAllMethods(JVMUtil.class);

		invokeAllPackageClassMethod("com.duowan.common.util");
		invokeAllPackageClassMethod("com.duowan.common.io.freemarker");
		invokeAllPackageClassMethod("com.duowan.common.lang.enums");
		invokeAllPackageClassMethod("com.duowan.common.spring.jdbc");
	}

	private void invokeAllPackageClassMethod(String pkg) throws ClassNotFoundException {
		List<String> clazzList = ScanClassUtils.scanPackages(pkg);
		for(String clazz : clazzList) {
			Class cla = Class.forName(clazz);
			if(clazz.contains("Test")) {
				continue;
			}
			ClassTestUtil.invokeAllMethods(cla);
		}
	}
}
