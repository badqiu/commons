package com.github.rapid.common.rpc.serde;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.github.rapid.common.rpc.serde.JsonpSerDeImpl;


public class JsonpSerDeImplTest extends Assert{
	
	JsonpSerDeImpl serDe = new JsonpSerDeImpl();
	ByteArrayOutputStream output = new ByteArrayOutputStream();
	
	Map seriParameters = new HashMap();
	{
		seriParameters.put(JsonpSerDeImpl.JSONCALLBACK_KEY, "test_json_callback");
	}
	@Test
	public void test() {
		
		serDe.serialize("123", output, seriParameters);
		System.out.println(output.toString());
		assertEquals(output.toString(),"test_json_callback(\"123\")");
	}
	
	@Test
	public void test2() {
		
		Map map = new HashMap();
		map.put("name", "badqiu");
		map.put("age", 100);
		serDe.serialize(map, output,seriParameters);
		System.out.println(output.toString());
		assertEquals(output.toString(),"test_json_callback({\"age\":100,\"name\":\"badqiu\"})");
	}
}
