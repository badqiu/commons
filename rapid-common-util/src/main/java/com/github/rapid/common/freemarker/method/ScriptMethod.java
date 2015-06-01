package com.github.rapid.common.freemarker.method;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.rapid.common.util.CollectionUtil;
import com.github.rapid.common.util.ScriptEngineUtil;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class ScriptMethod implements TemplateMethodModel{

	private static Logger logger = LoggerFactory.getLogger(ScriptMethod.class);
	
	public Object exec(List args) throws TemplateModelException {
		if(args == null || args.size() < 2) {
			throw new TemplateModelException("args.size>2 must true, example: script('groovy','script',ignoreError), current args:"+args);
		}
		String lang = (String)args.get(0);
		String script = (String)args.get(1);
		boolean ignoreError = (Boolean)CollectionUtil.safeGet(args,2,false);
		if(ignoreError) {
			try {
				return ScriptEngineUtil.eval(lang, script);
			}catch(Exception e) {
				logger.error("script eval error,lang="+lang+" script:"+script);
				return null;
			}
		}else {
			return ScriptEngineUtil.eval(lang, script);
		}
	}


	
}
