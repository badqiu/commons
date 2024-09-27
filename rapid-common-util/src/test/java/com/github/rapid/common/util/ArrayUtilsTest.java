package com.github.rapid.common.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.BeanUtils;

import com.github.rapid.common.testbean.TestBeanForArrayUtil;

import junit.framework.TestCase;

public class ArrayUtilsTest extends TestCase {
	String[] array = new String[]{"1","2","3"};
	
	public void testWithOneKey() {
		Map map = ArrayUtil.toMap(array,"age");
		assertNotNull(map);
		assertEquals("1",map.get("age"));
		assertEquals(1,map.size());
		
	}

	public void testWithNullArguments() {
		Map map = ArrayUtil.toMap(null,"age");
		assertNotNull(map);
		assertEquals(0,map.size());
	}
	
	public void testWithManyKey() {
		Map map = ArrayUtil.toMap(array,"age","height","width","many");
		assertNotNull(map);
		assertEquals("1",map.get("age"));
		assertEquals("2",map.get("height"));
		assertEquals("3",map.get("width"));
		assertEquals(3,map.size());
		
	}
	
	public void testToMapWithKeyArray(){
		String[] keys = new String[]{"age", "height", "width", "many"};
		Map map = ArrayUtil.toMap(array, keys);
		assertNotNull(map);
		assertEquals("1", map.get(keys[0]));
		assertEquals("2", map.get(keys[1]));
		assertEquals("3", map.get(keys[2]));
		assertEquals(3, map.size());
	}
	
	public void testToMapBeanClass(){
		try {
			TestBeanForArrayUtil map = ArrayUtil.toBean(array, TestBeanForArrayUtil.class);
			assertNotNull(map);
			String reflectionToString = ToStringBuilder.reflectionToString(map);
			System.out.println(reflectionToString);
			assertTrue(reflectionToString.contains("[name=1,age=2,money=3.0,salary=<null>,sex=<null>]"));
		}catch(Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
	
	public void testPropertyDescriptor() throws IntrospectionException{
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(TestBeanForArrayUtil.class);
		printArrays(targetPds);
		
		System.out.println("getBeanInfo getPropertyDescriptors:---------------------------");
		printArrays(Introspector.getBeanInfo(TestBeanForArrayUtil.class).getPropertyDescriptors());
		
		System.out.println("fields:---------------------------");
		Field[] fields = TestBeanForArrayUtil.class.getDeclaredFields();
		for(Field pd : fields) {
			System.out.println(pd);
		}
	}

	private void printArrays(Object[] targetPds) {
		for(Object pd : targetPds) {
			System.out.println(pd);
		}
	}
}
