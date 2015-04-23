package com.duowan.common.log.aop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.duowan.common.log.Profiled;

public class BlogServer {
	@Profiled
	public List<String> array_loop(int a,String[] array) throws InterruptedException {
		Thread.sleep(100);
		return Arrays.asList(array);
	}
	
	public List<String> map_loop(int a,Map map,Map mab2) throws InterruptedException {
		Thread.sleep(300);
		return new ArrayList(map.keySet());
	}
	
	public List<String> list_loop(int a,List list,Map mab2) throws InterruptedException {
		Thread.sleep(200);
		return list;
	}
	
}