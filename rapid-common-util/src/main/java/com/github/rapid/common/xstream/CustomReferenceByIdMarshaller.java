package com.github.rapid.common.xstream;

import com.thoughtworks.xstream.converters.ConverterLookup;
import com.thoughtworks.xstream.core.ReferenceByIdMarshaller;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.mapper.Mapper;

public class CustomReferenceByIdMarshaller extends ReferenceByIdMarshaller{

	public CustomReferenceByIdMarshaller(HierarchicalStreamWriter writer,
			ConverterLookup converterLookup, Mapper mapper,
			IDGenerator idGenerator) {
		super(writer, converterLookup, mapper, idGenerator);
	}

	public CustomReferenceByIdMarshaller(HierarchicalStreamWriter writer,
			ConverterLookup converterLookup, Mapper mapper) {
		super(writer, converterLookup, mapper);
	}

	

}
