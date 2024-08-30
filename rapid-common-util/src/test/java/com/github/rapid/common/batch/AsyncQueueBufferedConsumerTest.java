package com.github.rapid.common.batch;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.rapid.common.util.ThreadUtil;

public class AsyncQueueBufferedConsumerTest {
	List<Integer> resultList = new ArrayList<Integer>();
	
	AsyncQueueBufferedConsumer<Integer> f = new AsyncQueueBufferedConsumer<Integer>("test-async-task",(list) -> {
		System.out.println("consumer list:"+list);
		resultList.addAll(list);
	});
	
	@Before 
	public void before() throws Exception {
		f.afterPropertiesSet();
	}
	
	@Test
	public void test() throws Exception {
		
		for(int i = 0; i < 100; i++) {
			f.accept(i);
		}
		
		ThreadUtil.sleepSeconds(1);
		
		assertEquals(resultList.size(),100);

		ThreadUtil.sleepSeconds(1);
		f.putIntoQueue(Arrays.asList(1,2,3));
		
		f.close();
		assertEquals(resultList.size(),103);
	}
	
	
	@Test(expected = IllegalStateException.class)
	public void testClosed() throws Exception {
		
		for(int i = 0; i < 100; i++) {
			f.accept(i);
		}
		
		ThreadUtil.sleepSeconds(1);
		
		f.close();
		
		for(int i = 0; i < 100; i++) {
			f.accept(i);
		}
		
		ThreadUtil.sleepSeconds(1);
	}
	

}
