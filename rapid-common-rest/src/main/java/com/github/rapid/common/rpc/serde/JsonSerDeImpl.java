package com.github.rapid.common.rpc.serde;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
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

//	ObjectMapper objectMapper = new ObjectMapper();
//	{
////		objectMapper.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		objectMapper.setSerializationInclusion(Include.NON_NULL);
//	}
	
	public void serialize(Object object, OutputStream output,Map<String,Object> params) throws SerializeException {
		try {
			JSON.writeJSONString(output, object);
		} catch (Exception e) {
			throw new RuntimeException("serialize error",e);
		}
	}

	
	public Object deserialize(InputStream input, Type returnType,Map<String,Object> params) throws SerializeException {
		String text = null;
		try {
			text = IOUtils.toString(input);
			return JSON.parseObject(text, returnType);
		} catch (Exception e) {
			throw new RuntimeException("deserialize error,text:"+text+" returnType:"+returnType,e);
		}
//		try {
//			return objectMapper.readValue(input, objectMapper.constructType(returnType));
//		} catch (JsonParseException e) {
//			throw new SerializeException(e);
//		} catch (JsonMappingException e) {
//			throw new SerializeException(e);
//		} catch (IOException e) {
//			throw new SerializeException(e);
//		}
	}
	
	public String getContentType() {
		return "application/json";
	}

}
