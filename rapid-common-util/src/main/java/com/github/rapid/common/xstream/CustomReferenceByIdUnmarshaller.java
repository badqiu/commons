package com.github.rapid.common.xstream;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.core.ReferenceByIdUnmarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;


/**
 * 通过自定义 xstream的reference功能,可以引用spring,或者是自定义的属性
 * 
 * @author badqiu
 *
 */
public class CustomReferenceByIdUnmarshaller extends ReferenceByIdUnmarshaller {

	private ApplicationContext applicationContext;
	private Map<Object,Object> beans = new HashMap<Object,Object>();
	
	public CustomReferenceByIdUnmarshaller(Object root,
			HierarchicalStreamReader reader, ConverterLookup converterLookup,
			Mapper mapper) {
		super(root, reader, converterLookup, mapper);
	}

	public void setBeans(Map<Object, Object> beans) {
		this.beans = beans;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	protected Object convert(Object parent, Class type, Converter converter) {
		for(String key : new String[]{"reference","ref"}) {
			Object result = lookupObject(key);
			if(result != null) {
				return result;
			}
		}
		return super.convert(parent, type, converter);
	}

	private Object lookupObject(String attributeKey) {
		if(StringUtils.isBlank(attributeKey)) {
			return null;
		}
		String attributeName = getMapper().aliasForSystemAttribute(attributeKey);
		String reference = attributeName == null ? null : reader.getAttribute(attributeName);;
		if(StringUtils.isBlank(reference)) {
			return null;
		}
		return lookupObjectByKey(reference);
	}

	protected Object lookupObjectByKey(String reference) {
		if(applicationContext != null && applicationContext.containsBean(reference)) {
			return applicationContext.getBean(reference);
		}
		if(beans != null && beans.containsKey(reference)) {
			return beans.get(reference);
		}
		return null;
	}
}
