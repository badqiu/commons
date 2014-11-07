package com.duowan.common.rpc.serde;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.Map;

import com.caucho.hessian.io.AbstractHessianOutput;
import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;
import com.duowan.common.rpc.SerDe;
import com.duowan.common.rpc.SerializeException;

public class HessianSerDeImpl implements SerDe {

	public void serialize(Object object, OutputStream output,
			Map<String, Object> serializeParams) throws SerializeException {
		try {
			AbstractHessianOutput out = new Hessian2Output(output);
			SerializerFactory serializerFactory = getSerializerFactory();
			out.setSerializerFactory(serializerFactory);
			out.writeObject(object);
			out.flush();
		}catch(Exception e) {
			throw new RuntimeException("serialize error by hessian",e);
		}
	}

	private SerializerFactory getSerializerFactory() {
		return null;
	}

	public Object deserialize(InputStream input, Type returnType,
			Map<String, Object> serializeParams) throws SerializeException {
		try {
			Hessian2Input in = new Hessian2Input(input);
			in.setSerializerFactory(getSerializerFactory());
			Object result = in.readObject();
			return result;
		}catch(Exception e) {
			throw new RuntimeException("deserialize error by hessian",e);
		}
	}

	public String getContentType() {
		return "application/hessian";
	}

}
