package com.duowan.common.freemarker.loader;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;

import freemarker.cache.TemplateLoader;
/**
 * 
 * @author badqiu
 *
 */
public class MapTemplateLoader implements TemplateLoader {
	private Map<String, String> templateMap = null;

	public MapTemplateLoader() {
	}

	public MapTemplateLoader(Properties templates) {
		super();
		this.templateMap = (Map) templates;
	}

	public MapTemplateLoader(Map<String, String> templates) {
		super();
		this.templateMap = templates;
	}

	public void setTemplateMap(Map<String, String> templateMap) {
		this.templateMap = templateMap;
	}

	public Object findTemplateSource(String name) throws IOException {
		return templateMap.get(name);
	}

	public long getLastModified(Object templateSource) {
		return -1;
	}

	public Reader getReader(Object templateSource, String encoding)
			throws IOException {
		String templateContent = (String) templateSource;
		return new StringReader(templateContent);
	}

	public void closeTemplateSource(Object templateSource) throws IOException {
	}

}