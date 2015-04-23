package com.github.rapid.common.rpc;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;


/**
 * 序列化/反序列化接口，用于序列化，反序列化对象. 需要支持Collection,Map,Array,Date,Enum对象等的序列化
 * 
 * 序列化实现可以有: json,xml,protobuf,thrift
 * 
 * @author badqiu
 * 
 */
public interface SerDe {

	/**
	 * 序列化
	 * 
	 * @param output
	 *            输出
	 * @param object
	 *            序列化的对象
	 * @throws SerializeException
	 * @return
	 */
	public void serialize(Object object, OutputStream output,Map<String,Object> serializeParams) throws SerializeException;

	/**
	 * 反序列化
	 * 
	 * @param input
	 * @param returnType
	 *            返回值的类型
	 * @throws SerializeException
	 * @return
	 */
	public Object deserialize(InputStream input, Type returnType,Map<String,Object> serializeParams) throws SerializeException;
	
	
	public String getContentType();

}
