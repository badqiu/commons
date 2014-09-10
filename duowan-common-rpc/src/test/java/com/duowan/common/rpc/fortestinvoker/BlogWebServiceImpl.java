package com.duowan.common.rpc.fortestinvoker;

public class BlogWebServiceImpl implements BlogWebService {
	
	public String say(String name, int age, Long timestamp) {
		System.out.println("BlogWebServiceImpl.say() name:"+name+" age:"+age+" timestamp:"+timestamp);
		return "say()"+name;
	}
	
}
