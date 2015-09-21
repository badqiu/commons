package com.github.rapid.common.util;

import java.io.StringWriter;
import java.util.Properties;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.Assert;

import com.github.rapid.common.freemarker.loader.MapTemplateLoader;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * freemarker 动态sql生成器,使用freemarker语句生成动态sql
 * 
 * @author badqiu
 *
 */
public class FreemarkerSqlGenerator implements InitializingBean{

	private Properties properties = new Properties();
	
	private Resource propertiesFile = null;
	
	private Configuration conf = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	public Resource getPropertiesFile() {
		return propertiesFile;
	}

	public void setPropertiesFile(Resource propertiesFile) {
		this.propertiesFile = propertiesFile;
	}

	public Configuration getConf() {
		return conf;
	}

	public void setConf(Configuration conf) {
		this.conf = conf;
	}

	public String getSql(String sqlId) {
		return getSql(sqlId,null);
	}
	
	public String getSqlByMap(String sqlId,Object... mapKeyValuePairs) {
		return getSql(sqlId,MapUtil.newMap(mapKeyValuePairs));
	}
	
	public String getSql(String sqlId, Object params) {
		String template = (String) properties.get(sqlId);
		Assert.hasText(template,"not empty string by id:"+sqlId);
		try {
			StringWriter out = new StringWriter(template.length() * 2);
			Template t = conf.getTemplate(sqlId);
			t.process(params, out);
			return out.toString();
		} catch (Exception e) {
			throw new RuntimeException("process template error,id:" + sqlId
					+ " params:" + params + " template:" + template,e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(conf,"'conf' must be not null");
		Assert.notNull(properties,"'properties' must be not null");
		if(propertiesFile != null) {
			Properties tempProps = PropertiesLoaderUtils.loadProperties(propertiesFile);
			properties.putAll(tempProps);
		}
		conf.setTemplateLoader(new MapTemplateLoader(properties));
	}

}
