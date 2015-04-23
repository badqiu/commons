package com.duowan.common.rpc;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.duowan.common.rpc.serde.JavaSerDeImpl;
import com.duowan.common.rpc.serde.JsonSerDeImpl;

public class SerDeMapping {

	public static SerDeMapping DEFAULT_MAPPING = new SerDeMapping();
	static {
		DEFAULT_MAPPING.addContentTypeSerDeMapping(new JsonSerDeImpl());
		DEFAULT_MAPPING.addContentTypeSerDeMapping(new JavaSerDeImpl());
//		DEFAULT_MAPPING.addContentTypeSerDeMapping(new HessianSerDeImpl());
	}
	
	Map<String,SerDe> contentTypeSerDeMapping = new HashMap<String,SerDe>();
	Map<String,SerDe> formatSerDeMapping = new HashMap<String,SerDe>();
	
	public void addContentTypeSerDeMapping(SerDe serDe) {
		contentTypeSerDeMapping.put(serDe.getContentType(),serDe);
		formatSerDeMapping.put(extractFormat(serDe.getContentType()), serDe);
	}
	
	public static String extractFormat(String contentType) {
		return StringUtils.split(contentType,"/")[1];
	}
	/**
	 * 根据contentType查找SerDe
	 * @param contentType 类似如: application/json,application/java
	 * @return
	 */
	public SerDe getSerDeByContentType(String contentType) {
		String[] tokens = StringUtils.split(contentType,";");
		String realContentType = null;
		if(tokens != null && tokens.length > 0) {
			realContentType = StringUtils.trim(tokens[0]);
		}
		
		SerDe serDe = contentTypeSerDeMapping.get(realContentType);
		return serDe;
	}
	/**
	 * 根据contentType查找SerDe
	 * @param contentType 类似如: application/json,application/java
	 * @param defaultSerDe
	 * @return
	 */
	public SerDe getSerDeByContentType(String contentType,SerDe defaultSerDe) {
		SerDe serDe = getSerDeByContentType(contentType);
		if(serDe == null) {
			serDe = defaultSerDe;
		}
		return serDe;
	}
	/**
	 * 根据format查找SerDe
	 * @param format 类似如: json,java
	 * @return
	 */
	public SerDe getSerDeByFormat(String format) {
		return formatSerDeMapping.get(format);
	}
	
}
