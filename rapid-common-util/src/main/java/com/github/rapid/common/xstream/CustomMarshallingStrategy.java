package com.github.rapid.common.xstream;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.ApplicationContext;

import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.core.ReferenceByIdMarshallingStrategy;
import com.thoughtworks.xstream.core.TreeMarshaller;
import com.thoughtworks.xstream.core.TreeUnmarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;
/**
 * 方便xstream可以引用外部的bean.  如 datasource ref=""
 * 
 * 
 * 配置示例:
 * <pre>
 * 	CustomMarshallingStrategy marshallingStrategy = new CustomMarshallingStrategy();
 *	marshallingStrategy.setBeans(vars);
 *	marshallingStrategy.setApplicationContext(applicationContext);
 *	xstream.setMarshallingStrategy(marshallingStrategy);
 * </pre>
 * @author badqiu
 *
 */
public class CustomMarshallingStrategy extends ReferenceByIdMarshallingStrategy {
	private ApplicationContext applicationContext;
	private Map<Object,Object> beans = new HashMap<Object,Object>();
	
	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public void setBeans(Map<Object, Object> beans) {
		this.beans = beans;
	}

	public Object put(Object key, Object value) {
		return beans.put(key, value);
	}

	public void putAll(Map<? extends Object, ? extends Object> m) {
		beans.putAll(m);
	}

	protected TreeUnmarshaller createUnmarshallingContext(Object root,
			HierarchicalStreamReader reader, ConverterLookup converterLookup,
			Mapper mapper) {
		CustomReferenceByIdUnmarshaller springReferenceByIdUnmarshaller = new CustomReferenceByIdUnmarshaller(root, reader,
						converterLookup, mapper);
		springReferenceByIdUnmarshaller.setBeans(beans);
		springReferenceByIdUnmarshaller.setApplicationContext(applicationContext);
		return springReferenceByIdUnmarshaller;
	}

	protected TreeMarshaller createMarshallingContext(
			HierarchicalStreamWriter writer, ConverterLookup converterLookup,
			Mapper mapper) {
		CustomReferenceByIdMarshaller springReferenceByIdMarshaller = new CustomReferenceByIdMarshaller(writer, converterLookup,
						mapper);
		return springReferenceByIdMarshaller;
	}

}
