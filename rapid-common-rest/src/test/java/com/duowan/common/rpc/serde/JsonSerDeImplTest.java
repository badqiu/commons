package com.duowan.common.rpc.serde;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.LinkedList;

import org.junit.Assert;
import org.junit.Test;

import com.duowan.common.rpc.SerializeException;
import com.duowan.common.rpc.fortest.api.BlogInfoServiceImpl;
import com.duowan.common.rpc.fortest.api.model.Blog;
import com.duowan.common.rpc.serde.JsonSerDeImpl;


public class JsonSerDeImplTest extends Assert{
	
	JsonSerDeImpl json = new JsonSerDeImpl();
	@Test
	public void test_performance() throws SerializeException, SecurityException, NoSuchMethodException {
		BlogInfoServiceImpl service = new BlogInfoServiceImpl();
		
		LinkedList<Blog> list = service.findBlogLinkedList("key");
		
		long start = System.currentTimeMillis();
		long count = 100000;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		json.serialize(service.findBlogLinkedList("key"), output,null);
		for(int i = 0; i < count; i++) {
			ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
			json.deserialize(input, BlogInfoServiceImpl.class.getMethod("findBlogLinkedList",String.class).getGenericReturnType(),null);
		}
		double cost = System.currentTimeMillis() - start;
		
		double tps = count /cost * 1000;
		System.out.println("tps:"+tps);
		
		assertTrue(tps > 7000);
	}
}
