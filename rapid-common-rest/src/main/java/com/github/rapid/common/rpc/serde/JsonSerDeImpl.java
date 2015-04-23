package com.github.rapid.common.rpc.serde;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.DeserializationConfig.Feature;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		objectMapper.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
			return objectMapper.readValue(input, TypeFactory.type(returnType));
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
