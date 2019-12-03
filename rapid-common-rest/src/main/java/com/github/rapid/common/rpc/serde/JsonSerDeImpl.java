package com.github.rapid.common.rpc.serde;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory.Feature;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.rapid.common.rpc.SerDe;
import com.github.rapid.common.rpc.SerializeException;

/**
 * 序列化的Json实现
 * 
 * @author badqiu
 * 
 */
public class JsonSerDeImpl implements SerDe {

	private static final Logger logger = LoggerFactory.getLogger(JsonSerDeImpl.class);

	ObjectMapper objectMapper = new ObjectMapper();
	{
//		objectMapper.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	public void serialize(Object object, OutputStream output,Map<String,Object> params) throws SerializeException {
		try {
			objectMapper.writeValue(output, object);
		} catch (JsonGenerationException e) {
			throw new SerializeException(e);
		} catch (JsonMappingException e) {
			throw new SerializeException(e);
		} catch (IOException e) {
			throw new SerializeException(e);
		}
	}

	
	public Object deserialize(InputStream input, Type returnType,Map<String,Object> params) throws SerializeException {
		try {
			return objectMapper.readValue(input, objectMapper.constructType(returnType));
		} catch (JsonParseException e) {
			throw new SerializeException(e);
		} catch (JsonMappingException e) {
			throw new SerializeException(e);
		} catch (IOException e) {
			throw new SerializeException(e);
		}
	}
	
	public String getContentType() {
		return "application/json";
	}

}
