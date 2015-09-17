package com.github.rapid.common.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.ResourceUtils;


public class ScriptEngineUtilTest extends Assert{
	
	@Test
	public void test_groovy_class() throws Exception {
		File file = ResourceUtils.getFile("classpath:fortest_groovy/GroovyTest.txt");
		String groovy = FileUtils.readFileToString(file);
		Map param = MapUtil.newMap("name","badqiu","age",20);
		List<Map> params = new ArrayList<Map>();
		for(int i = 0; i < 1000000; i++) {
			Map row = MapUtil.newMap("name","badqiu"+i,"age",20+i);
			params.add(row);
		}
		System.out.println("--------eval with List<map> -------");
		ScriptEngineUtil.eval("groovy", groovy, MapUtil.newMap("rows",params));
		Profiler.start("prppertyMissing",100);
		for(int i = 0; i < 100; i++) {
			ScriptEngineUtil.eval("groovy", groovy, MapUtil.newMap("rows",params));
		}
		Profiler.release();
		System.out.println(Profiler.dump());
	}
	
	@Test
	public void test_perf() {
		evalPerfTest("groovy",50000);
		evalPerfTest("javascript",3000);
	}

	private void evalPerfTest(String lang,int count) {
		String script = "1+1";
		ScriptEngineUtil.eval(lang, script);
		
		Profiler.start(lang,count);
		for(int i = 0; i < count; i++) {
			assertEquals(2,ScriptEngineUtil.eval(lang, script));
		}
		Profiler.release();
		System.out.println(Profiler.dump());
	}
	
	@Test
	public void getBinding() throws ScriptException {
		ScriptEngine engine = ScriptEngineUtil.getScriptEngine("groovy");
	    Bindings bindings = engine.createBindings();
	    Object result = engine.eval("this.name = 'badqiu'; sex='hello'",bindings);
	    System.out.println("result:"+result+" bindings:"+bindings);
	    for(Object key :bindings.keySet()) {
	    	System.out.println(key+":"+bindings.get(key));
	    }
	}
}
