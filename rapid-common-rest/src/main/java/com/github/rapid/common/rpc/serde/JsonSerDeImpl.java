package com.github.rapid.common.rpc.serde;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.github.rapid.common.rpc.SerDe;
import com.github.rapid.common.rpc.SerializeException;
import com.github.rapid.common.rpc.util.EmptyValueFilter;

/**
 * 序列化的Json实现
 * 
 * @author badqiu
 * 
 */
public class JsonSerDeImpl implements SerDe {

	private static final Logger logger = LoggerFactory.getLogger(JsonSerDeImpl.class);

	
	SerializeFilter[] filters = new SerializeFilter[]{new EmptyValueFilter()};
	
	public void serialize(Object object, OutputStream output,Map<String,Object> params) throws SerializeException {
		try {
			JSON.writeJSONString(output,object);
//			JSON.writeJSONString(output,UTF8,object,SerializeConfig.globalInstance,filters,null,JSON.DEFAULT_GENERATE_FEATURE);
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
	}
	
	public String getContentType() {
		return "application/json";
	}

}
