package com.duowan.common.rpc.serde;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;

import com.duowan.common.rpc.SerDe;
import com.duowan.common.rpc.SerializeException;

public class JavaSerDeImpl implements SerDe{

	public void serialize(Object object, OutputStream output,Map<String, Object> serializeParams) throws SerializeException {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(output);
			oos.writeObject(object);
			oos.flush();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Object deserialize(InputStream input, Type returnType,
			Map<String, Object> serializeParams) throws SerializeException {
		try {
			ObjectInputStream ois = new ObjectInputStream(input);
			return ois.readObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getContentType() {
		return "application/java";
	}

}
