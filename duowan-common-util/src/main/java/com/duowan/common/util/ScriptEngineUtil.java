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

public class ScriptEngineUtil {
	public static ScriptEngineManager factory = new ScriptEngineManager();
	
	private static Map<String,ScriptEngine> scriptEngineCache = new HashMap<String,ScriptEngine>();
	private static Map<String,CompiledScript> scriptCache = new HashMap<String,CompiledScript>();
	
	public static Object eval(String scriptEngineName,String script) {
		return eval(scriptEngineName,script,new HashMap<String,Object>());
	}
	
	public static Object eval(String scriptEngineName,String script,Map<? extends String, ? extends Object> context) {
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
	
	private class ScriptEngineWrapper implements ScriptEngine,Compilable,Invocable  {
		private ScriptEngine engine;
		private Compilable compilable;
		private Invocable invocable;
		
		public ScriptEngineWrapper(ScriptEngine engine) {
			this.engine = engine;
			if(engine instanceof Compilable) {
				compilable = (Compilable)engine;
			}
			if(engine instanceof Invocable) {
				invocable = (Invocable)engine;
			}
		}

		public Object eval(String script, ScriptContext context)
				throws ScriptException {
			return engine.eval(script, context);
		}

		public Object eval(Reader reader, ScriptContext context)
				throws ScriptException {
			return engine.eval(reader, context);
		}

		public Object eval(String script) throws ScriptException {
			return engine.eval(script);
		}

		public Object eval(Reader reader) throws ScriptException {
			return engine.eval(reader);
		}

		public Object eval(String script, Bindings n) throws ScriptException {
			return engine.eval(script, n);
		}

		public Object eval(Reader reader, Bindings n) throws ScriptException {
			return engine.eval(reader, n);
		}

		public void put(String key, Object value) {
			engine.put(key, value);
		}

		public Object get(String key) {
			return engine.get(key);
		}

		public Bindings getBindings(int scope) {
			return engine.getBindings(scope);
		}

		public void setBindings(Bindings bindings, int scope) {
			engine.setBindings(bindings, scope);
		}

		public Bindings createBindings() {
			return engine.createBindings();
		}

		public ScriptContext getContext() {
			return engine.getContext();
		}

		public void setContext(ScriptContext context) {
			engine.setContext(context);
		}

		public ScriptEngineFactory getFactory() {
			return engine.getFactory();
		}

		public CompiledScript compile(String script) throws ScriptException {
			return compilable.compile(script);
		}

		public CompiledScript compile(Reader script) throws ScriptException {
			return compilable.compile(script);
		}

		public Object invokeMethod(Object thiz, String name, Object... args)
				throws ScriptException, NoSuchMethodException {
			return invocable.invokeMethod(thiz, name, args);
		}

		public Object invokeFunction(String name, Object... args)
				throws ScriptException, NoSuchMethodException {
			return invocable.invokeFunction(name, args);
		}

		public <T> T getInterface(Class<T> clasz) {
			return invocable.getInterface(clasz);
		}

		public <T> T getInterface(Object thiz, Class<T> clasz) {
			return invocable.getInterface(thiz, clasz);
		}
	}
	
}
