package com.github.rapid.common.freemarker.directive; 

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rapid.common.util.ScriptEngineUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author badqiu
 */
public class ScriptDirective implements TemplateDirectiveModel{
	private static Logger logger = LoggerFactory.getLogger(ScriptDirective.class);
	
	public final static String DIRECTIVE_NAME = "script";
	
	public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
		String lang = DirectiveUtils.getRequiredParam(params, "lang");
		Boolean ignoreError = MapUtils.getBoolean(params, "ignoreError", false);
		StringWriter script = new StringWriter();
		body.render(script);
		Map model = new HashMap();
		model.put("env",env);
		try {
			ScriptEngine engine = ScriptEngineUtil.getScriptEngine("groovy");
		    Bindings bindings = engine.createBindings();
		    
		    Object result = engine.eval(script.toString(),bindings);
		    env.getCurrentNamespace().putAll(bindings);
		    
		}catch(ScriptException e) {
			if(ignoreError) {
				logger.error("script eval error,lang="+lang+" script:"+script);
				//ignore
			}else {
				throw new RuntimeException("eval error,script:"+script,e);
			}
		}
	}
		
}
