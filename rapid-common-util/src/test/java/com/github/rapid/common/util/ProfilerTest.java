package com.github.rapid.common.util;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.text.StrSubstitutor;
import org.junit.Test;

import com.github.rapid.common.util.Profiler;

public class ProfilerTest {

	public static void main(String[] args) throws InterruptedException {
		
		Profiler.start("start-" + 1);

		messageFormatPerf();
		javaQuery();

		Profiler.release();
		
		System.out.println(Profiler.dump() + "\n\n");
	}

	private static void javaQuery() throws InterruptedException {
		Profiler.enter("java query");
		Thread.sleep(600);
		Profiler.release(new RuntimeException("abc",new IllegalAccessError()));
	}

	private static void abcInsert() throws InterruptedException {
		Profiler.enter("abcInsert()");
		Thread.sleep(600);
		Profiler.release();
		decimalFormatPerf();
	}

	private static void messageFormatPerf() throws InterruptedException {
		Profiler.enter("messageFormatPerf()");
		int count = 100000;
		MessageFormat format = new MessageFormat("[totalCost:{2,number}ms  {1} {2} {4} ]");
		Object[] params = new Object[]{100,200,300,400,500};
		for(int i = 0; i < count; i++){
			format.format(params);
		}
		blogQuery();
		Profiler.release(count,3000);
	}
	
	private static void decimalFormatPerf() throws InterruptedException {
		Profiler.enter("decimalFormatPerf()");
		int count = 100000;
		DecimalFormat format = new DecimalFormat("##.####");
		for(int i = 0; i < count; i++){
			format.format(count);
		}
		Profiler.release(count,3000);
	}

	private static void blogQuery() throws InterruptedException {
		Profiler.enter("blog query",10000);
		Thread.sleep(800);
		abcInsert();
		strSubstitutor_perf();
		Profiler.release();
	}
	
	private static void strSubstitutor_perf() throws InterruptedException {
		Map<String, String> values = new HashMap<String, String>();
		values.put("value", "100");
		values.put("column", "age");
		StrSubstitutor sub = new StrSubstitutor(values);
		
		int count = 100000;
		Profiler.enter("StrSubstitutor_perf",count);
		String result = null;
		for(int i = 0; i < count; i++) {
			result = sub.replace("There's an incorrect value '${value}' in column :${column},${username}");
		}
		System.out.println(StrSubstitutor.replaceSystemProperties("You are running with java.version = ${java.version} and os.name = ${os.name}. username:${username}"));

		System.out.println(result);
		Profiler.release();
	}
	
	@Test
	public void test_dump() throws InterruptedException {
		try {
			Profiler.start("businessProcess");

			Thread.sleep(1100);
			doSomething();
		} finally {
			Profiler.release(10000,800);
			System.out.println(Profiler.dump());
		}
	}
	
	@Test
	public void test_new_object() throws InterruptedException {
		try {
			int count = 1000000000;
			Profiler.start("test_new_object",count);
			for(int i = 0; i< count; i++) {
				new Object();
			}
		} finally {
			Profiler.release();
			System.out.println(Profiler.dump());
		}
	}

	public void doSomething() throws InterruptedException {
		try {
			Profiler.enter("doSomething");

			// do some thing...
			Thread.sleep(380);

		} finally {
			Profiler.release();
		}
	}

}
