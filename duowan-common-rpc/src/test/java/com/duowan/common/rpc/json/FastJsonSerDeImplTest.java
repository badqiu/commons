package com.duowan.common.rpc.json;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.output.NullOutputStream;
import org.codehaus.jackson.map.ObjectReader;
import org.junit.Test;

import com.duowan.common.rpc.SerDe;
import com.duowan.common.rpc.fortest.api.BlogInfoServiceImpl;
import com.duowan.common.rpc.fortest.api.model.Blog;


public class FastJsonSerDeImplTest {

	@Test
	public void test_perf() {
		int[] array = new int[]{1,10000};
		for(int i : array) {
			SerDe serDe = new FastJsonSerDeImpl();
			testPerf(i,new JsonSerDeImpl());
			testPerf(i,new JavaSerDeImpl());
			testPerf(i,serDe);
			System.out.println("---------------------------");
		}
	}

	HashMap params = new HashMap();
	private void testPerf(int count,SerDe serDe) {
		System.out.println("\n\n");
		Blog blog = BlogInfoServiceImpl.createWithBlog("100");
		singleSer(count, serDe, blog);
		batchSer(count, serDe, blog);
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
			blogList.add(blog);
		}
		
		start = System.currentTimeMillis();
		for(int i = 0; i < count; i++) {
			OutputStream output = new NullOutputStream();
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
