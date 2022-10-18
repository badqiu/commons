package com.github.rapid.common.xstream;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.core.ReferenceByIdUnmarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.mapper.Mapper;


/**
 * 通过自定义 xstream的reference功能,可以引用spring,或者是自定义的属性
 * 示例配置:
 * <pre>
 *  &lt;dataSource ref=""/>
 * </pre>
 * @author badqiu
 *
 */
public class CustomReferenceByIdUnmarshaller extends ReferenceByIdUnmarshaller {

	private BeanFactory beanFactory;
	
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
		this.beanFactory = applicationContext;
	}
	
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
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
		return lookupObjectByBeanName(reference);
	}

	protected Object lookupObjectByBeanName(String beanName) {
		if(beanFactory != null && beanFactory.containsBean(beanName)) {
			return beanFactory.getBean(beanName);
		}
		if(beans != null && beans.containsKey(beanName)) {
			return beans.get(beanName);
		}
		return null;
	}
}
