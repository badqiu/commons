package com.github.rapid.common.rpc.serde;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.github.rapid.common.rpc.RPCConstants;
import com.github.rapid.common.rpc.SerDe;
import com.github.rapid.common.rpc.SerializeException;

/**
 * 用于支持jsonp的json序列化实现
 * 
 * @author badqiu
 * 
 */
public class JsonpSerDeImpl extends JsonSerDeImpl implements SerDe {
	static String JSONCALLBACK_KEY = RPCConstants.JSONCALLBACK_KEY;

//	ObjectMapper objectMapper = new ObjectMapper();
//	{
////		objectMapper.getDeserializationConfig().set(Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//		objectMapper.setSerializationInclusion(Include.NON_NULL);
//	}
	
	public void serialize(Object object, OutputStream output,Map<String,Object> serializeParams) throws SerializeException {
		if(serializeParams == null) {
			throw new IllegalArgumentException("serializeParams must be not null");
		}
		String callback = (String)serializeParams.get(JSONCALLBACK_KEY);
		
		checkCallback(callback);
		
		try {
			
			String prefix = callback+"(";
			output.write(prefix.getBytes());
			output.write(JSON.toJSONBytes(object));
			output.write(")".getBytes());
		}catch(IOException e) {
			throw new SerializeException(e);
		}
	}

	public static void checkCallback(String callback) {
		if(StringUtils.isBlank(callback)) {
			throw new IllegalArgumentException("not found jsonp callback function name:"+JSONCALLBACK_KEY);
		}
		Assert.isTrue(callback.matches("[\\w_]+"),"callback only number or a-z or _");
	}

	@Override
	public String getContentType() {
		return "application/javascript";
	}
}
