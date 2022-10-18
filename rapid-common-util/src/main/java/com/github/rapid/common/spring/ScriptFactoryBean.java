package com.github.rapid.common.spring;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.Assert;

import com.github.rapid.common.util.ScriptEngineUtil;
/**
 * 通过动态语言创建spring对象
 * 
 * 语言通过lang定义,可以为: javascript(默认值),groovy
 * @author badqiu
 *
 */
public class ScriptFactoryBean implements FactoryBean{
	private String lang = "javascript";
	private String script;
	private Object object;
	
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public void setLanguage(String lang) {
		setLang(lang);
	}
	
	public String getScript() {
		return script;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public synchronized Object getObject()  {
		Assert.hasText(script,"'script' must be not empty");
		if(object == null) {
			object = ScriptEngineUtil.eval(lang, script);
		}
		Assert.notNull(object,"eval 'script' result must be not null,script:"+script);
		return object;
	}

	public Class getObjectType() {
		return getObject().getClass();
	}

	public boolean isSingleton() {
		return true;
	}
	
}
