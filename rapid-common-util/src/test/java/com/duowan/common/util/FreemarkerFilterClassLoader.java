package com.duowan.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;

import com.duowan.common.io.freemarker.FreemarkerInputStream;

/**
 * 扩展ClassLoader,getResource()将返回通过Freemarker处理过的resource
 * 
 * @author badqiu
 * 
 */
public class FreemarkerFilterClassLoader extends ClassLoader {
	private static Logger logger = LoggerFactory.getLogger(FreemarkerFilterClassLoader.class);
	
	private Map<String, Object> variables = new HashMap<String, Object>();
	private Set<String> needFilterResources = new HashSet<String>();
	private AntPathMatcher antPathMatcher = new AntPathMatcher();
	
	@SuppressWarnings("all")
	public FreemarkerFilterClassLoader() {
		variables.put("env", System.getenv());
		Map properties = System.getProperties();
		variables.putAll(properties);
	}
	
	public Set<String> getNeedFilterResources() {
		return needFilterResources;
	}

	public void setNeedFilterResources(Set<String> needFilterResources) {
		this.needFilterResources = needFilterResources;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		if (needFilter(name)) {
			logger.info("filterBy Freemarker resource:" + name);
			return filterBy(super.getResourceAsStream(name));
		} else {
			return super.getResourceAsStream(name);
		}
	}

	@Override
	public URL getResource(String name) {
		if (needFilter(name)) {
			return filterBy(super.getResource(name));
		} else {
			return super.getResource(name);
		}
	}
	
	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		if (needFilter(name)) {
			Enumeration<URL> urls = super.getResources(name);
			Vector<URL> list = new Vector();
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if(url != null) {
					URL filterByFreemarker = filterBy(url);
					list.add(filterByFreemarker);
				}
			}
			return list.elements();
		} else {
			return super.getResources(name);
		}
	}
	
	private static AtomicLong sequence = new AtomicLong(System.currentTimeMillis());
	private URL filterBy(URL resource) {
		if (resource == null)
			return null;
		try {
			String dir = getTmpDir();
			File tmpOutputFile = new File(dir, "filter_freemarker_classloader_" + sequence.incrementAndGet() + "_" + new File(resource.getPath()).getName());
			
			logger.info("filterBy Freemarker resource:" + resource + " tmp_filter_output:" + tmpOutputFile);

			InputStream input = null;
			FileOutputStream output = null;
			try {
				input = resource.openStream();
				InputStream finput = filterBy(input);
				output = new FileOutputStream(tmpOutputFile);
				IOUtils.copy(finput, output);
			} finally {
				IOUtils.closeQuietly(input);
				IOUtils.closeQuietly(output);
			}
			return new URL("file:///" + tmpOutputFile.getAbsolutePath());
		} catch (Exception e) {
			throw new IllegalStateException(
					"filterByFreemarker has error,resource:" + resource, e);
		}
	}

	/**
	 * 通过freemarker引擎处理
	 * @param input
	 * @return
	 */
	protected InputStream filterBy(InputStream input) {
		InputStream finput = new FreemarkerInputStream(input, variables);
		return finput;
	}

	private static String getTmpDir() {
		return System.getProperty("java.io.tmpdir");
	}

	// TODO 判断XML文件如果有 <?freemarker?> 则过滤, 判断properties文件，如果有: #!freemarker 标记，则处理数据
	private boolean needFilter(String name) {
		if(needFilterResources.contains(name)) {
			return true;
		}
		for(String pattern : needFilterResources) {
			if(antPathMatcher.match(pattern, name)) {
				return true;
			}
		}
		return false;
	}

}
