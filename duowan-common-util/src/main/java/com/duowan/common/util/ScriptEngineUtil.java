package com.duowan.common.util;

import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang.StringUtils;

public class ScriptEngineUtil {
	public static ScriptEngineManager factory = new ScriptEngineManager();
	
	private static Map<String,ScriptEngine> scriptEngineCache = new HashMap<String,ScriptEngine>();
	private static Map<String,CompiledScript> scriptCache = new HashMap<String,CompiledScript>();
	
	public static Object eval(String scriptEngineName,String script) {
		return eval(scriptEngineName,script,new HashMap<String,Object>());
	}
	
	public static Object eval(String scriptEngineName,String script,Map<? extends String, ? extends Object> context) {
		if(StringUtils.isBlank(script)) {
			return null;
		}
        ScriptEngine engine = getScriptEngine(scriptEngineName);
        Bindings bindings = engine.createBindings();
        bindings.putAll(context);
        try {
        	CompiledScript compiledScript = getCompiledScript(script, engine);
        	if(compiledScript == null) {
        		return engine.eval(script, bindings);
        	}else {
        		return compiledScript.eval(bindings);
        	}
		} catch (ScriptException e) {
			throw new RuntimeException("script_exception,script:"+script+" context:"+context+" info:"+e,e);
		}
	}

	private static CompiledScript getCompiledScript(String script, ScriptEngine engine) throws ScriptException {
		CompiledScript compiledScript = scriptCache.get(script);
        if(compiledScript == null) {
	        if(engine instanceof Compilable) {
	        	Compilable compilable = (Compilable)engine;
				compiledScript = compilable.compile(script);
				scriptCache.put(script, compiledScript);
	        }
	        return compiledScript;
        }
        return null;
	}
	
	public static ScriptEngine getScriptEngine(String scriptEngineName) {
		ScriptEngine engine = scriptEngineCache.get(scriptEngineName);
		if(engine == null) {
			engine = factory.getEngineByName(scriptEngineName);
			scriptEngineCache.put(scriptEngineName, engine);
		}
		if(engine == null)
			throw new IllegalStateException("not found ScriptEngine by name:"+scriptEngineName);
		return engine;
	}
	
	
}
