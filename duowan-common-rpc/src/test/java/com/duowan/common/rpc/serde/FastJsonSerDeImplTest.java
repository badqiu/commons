package com.duowan.common.rpc.serde;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.output.NullOutputStream;
import org.junit.Test;

import com.duowan.common.rpc.SerDe;
import com.duowan.common.rpc.fortest.api.BlogInfoServiceImpl;
import com.duowan.common.rpc.fortest.api.model.Blog;
import com.duowan.common.rpc.serde.HessianSerDeImpl;
import com.duowan.common.rpc.serde.JavaSerDeImpl;
import com.duowan.common.rpc.serde.JsonSerDeImpl;


public class FastJsonSerDeImplTest {

	@Test
	public void test_perf() {
		int[] array = new int[]{1,10000};
		for(int i : array) {
			testPerf(i,new JavaSerDeImpl());
			testPerf(i,new JsonSerDeImpl());
			testPerf(i,new HessianSerDeImpl());
			testPerf(i,new FastJsonSerDeImpl());
			System.out.println("---------------------------");
		}
	}

	HashMap params = new HashMap();
	private void testPerf(int count,SerDe serDe) {
		System.out.println("\n\n");
		Blog blog = BlogInfoServiceImpl.createWithBlog("100");
		testSerDeFunction(serDe, blog);
		
		singleSer(count * 100, serDe, blog);
		batchSer(count, serDe, blog);
	}

	private void testSerDeFunction(SerDe serDe, Blog blog) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		serDe.serialize(blog, baos, new HashMap());
		Blog returnBlog = (Blog)serDe.deserialize(new ByteArrayInputStream(baos.toByteArray()), Blog.class, new HashMap());
//		assertEquals(serDe.getClass()+"function error",blog.toString(),returnBlog.toString());
	}

	private void singleSer(int count, SerDe serDe, Blog blog) {
		long start = System.currentTimeMillis();
		for(int i = 0; i < count; i++) {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			serDe.serialize(blog, output, params);
		}
		long cost = System.currentTimeMillis() - start;
		printTps(serDe.getClass().getSimpleName()+" serialize single BLOG", count, cost);
	}

	private void batchSer(int count, SerDe serDe, Blog blog) {
		long start;
		long cost;
		List<Blog> blogList = new ArrayList<Blog>();
		for(int i = 0; i < 1000; i++) {
			blogList.add(blog.clone());
		}
		
		start = System.currentTimeMillis();
		OutputStream output = new NullOutputStream();
		for(int i = 0; i < count; i++) {
			serDe.serialize(blogList, output, params);
		}
		cost = System.currentTimeMillis() - start;
		printTps(serDe.getClass().getSimpleName()+" serialize blogList.size:"+blogList.size(), count, cost);
	}

	private void printTps(String info, int count, long cost) {
		int tps = (int)(count * 1000.0 / cost);
		System.out.println(info+"\t\t cost:"+cost+" tps:"+tps+" count:"+count);
	}
}
