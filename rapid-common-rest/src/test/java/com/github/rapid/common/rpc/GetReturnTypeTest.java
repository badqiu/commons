package com.github.rapid.common.rpc;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.UnhandledException;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.junit.Assert;
import org.junit.Test;

import com.github.rapid.common.rpc.fortest.api.BlogInfoService;
import com.github.rapid.common.rpc.fortest.api.model.Blog;
import com.github.rapid.common.rpc.fortestinvoker.UserTypeEnum;

public class GetReturnTypeTest extends Assert{
	
	Class<BlogInfoService> clazz = BlogInfoService.class;
	/*
	 * 
	Blog findSingleBlog(String group,String blogId);
	
	Blog findByBlogQuery(BlogQuery query);
	
	LinkedList<Blog> findBlogLinkedList(String key);
	
	List<Blog> findBlogList(String key);
	
	List<Map<String,Blog>> findBlogListMap(String key);
	
	Collection<Blog> findBlogCollection(String key);
	
	Set<Blog> findBlogSet(String key);
	
	Blog[] findBlogArray(String key);

	Map<String,Blog> findBlogMap(String key);
	
	Map<String,List<Blog>> findBlogMapList(String key);
	
	UserTypeEnum findUserTypeEnum(String key);
	
	UserTypeEnum[] findUserTypeEnumArray(String key);
	
	Date findDate(String key);
	
	double findDouble(String key);
	
	Double findDoubleObject(String key);
	 */
	
	@Test
	public void test_findSingleBlog() throws Exception {
		assertEquals(Blog.class,findMethod("findSingleBlog").getReturnType());
		assertEquals(Blog.class,findMethod("findByBlogQuery").getReturnType());
	}
	
	@Test
	public void test_findBlogList() throws Exception {
		assertEquals(List.class,findMethod("findBlogList").getReturnType());
		
		ParameterizedType parameterizedType = (ParameterizedType)findMethod("findBlogList").getGenericReturnType();
		assertEquals(Blog.class,parameterizedType.getActualTypeArguments()[0]);
		assertEquals(List.class,parameterizedType.getRawType());
	}

	@Test
	public void test_findBlogListMap() throws Exception {
		assertEquals(List.class,findMethod("findBlogList").getReturnType());
		
		ParameterizedType parameterizedType = (ParameterizedType)findMethod("findBlogListMap").getGenericReturnType();
		ParameterizedType mapParameterizedType = (ParameterizedType)parameterizedType.getActualTypeArguments()[0];
		assertEquals(Map.class,mapParameterizedType.getRawType());
		assertEquals(String.class,mapParameterizedType.getActualTypeArguments()[0]);
		assertEquals(Blog.class,mapParameterizedType.getActualTypeArguments()[1]);
		
		assertEquals(List.class,parameterizedType.getRawType());
	}
	
	@Test
	public void test_findBlogLinkedList() throws Exception {
		assertEquals(LinkedList.class,findMethod("findBlogLinkedList").getReturnType());
		
		ParameterizedType parameterizedType = (ParameterizedType)findMethod("findBlogLinkedList").getGenericReturnType();
		assertEquals(Blog.class,parameterizedType.getActualTypeArguments()[0]);
		assertEquals(LinkedList.class,parameterizedType.getRawType());
	}

	@Test
	public void test_findBlogCollection() throws Exception {
		assertEquals(Collection.class,findMethod("findBlogCollection").getReturnType());
		
		ParameterizedType parameterizedType = (ParameterizedType)findMethod("findBlogCollection").getGenericReturnType();
		assertEquals(Blog.class,parameterizedType.getActualTypeArguments()[0]);
		assertEquals(Collection.class,parameterizedType.getRawType());
	}

	@Test
	public void test_findBlogSet() throws Exception {
		assertEquals(Set.class,findMethod("findBlogSet").getReturnType());
		
		ParameterizedType parameterizedType = (ParameterizedType)findMethod("findBlogSet").getGenericReturnType();
		assertEquals(Blog.class,parameterizedType.getActualTypeArguments()[0]);
		assertEquals(Set.class,parameterizedType.getRawType());
	}

	@Test
	public void test_findBlogArray() throws Exception {
		assertEquals(Blog[].class,findMethod("findBlogArray").getReturnType());
		
		Class parameterizedType = (Class)findMethod("findBlogArray").getGenericReturnType();
		assertEquals(Blog[].class,parameterizedType);
	}

	@Test
	public void test_findfindBlogMap() throws Exception {
		assertEquals(Map.class,findMethod("findBlogMap").getReturnType());
		
		ParameterizedType parameterizedType = (ParameterizedType)findMethod("findBlogMap").getGenericReturnType();
		assertEquals(String.class,parameterizedType.getActualTypeArguments()[0]);
		assertEquals(Blog.class,parameterizedType.getActualTypeArguments()[1]);
		assertEquals(Map.class,parameterizedType.getRawType());
	}

	@Test
	public void test_findBlogMapList() throws Exception {
		assertEquals(Map.class,findMethod("findBlogMapList").getReturnType());
		
		ParameterizedType parameterizedType = (ParameterizedType)findMethod("findBlogMapList").getGenericReturnType();
		assertEquals(String.class,parameterizedType.getActualTypeArguments()[0]);
		ParameterizedType listParameterizedType = (ParameterizedType)parameterizedType.getActualTypeArguments()[1];
		assertEquals(List.class,listParameterizedType.getRawType());
		assertEquals(Blog.class,listParameterizedType.getActualTypeArguments()[0]);
		
		assertEquals(Map.class,parameterizedType.getRawType());
	}
	
	@Test
	public void test_findUserTypeEnum() throws Exception {
		assertEquals(UserTypeEnum.class,findMethod("findUserTypeEnum").getReturnType());
		
		Class parameterizedType = (Class)findMethod("findUserTypeEnum").getGenericReturnType();
		assertEquals(UserTypeEnum.class,parameterizedType);
	}

	@Test
	public void test_findDouble() throws Exception {
		assertEquals(double.class,findMethod("findDouble").getReturnType());
		
		Class parameterizedType = (Class)findMethod("findDouble").getGenericReturnType();
		assertEquals(double.class,parameterizedType);
		
		assertFalse(double.class == Double.class);
	}
	
	@Test
	public void test_findUserTypeEnumArray() throws Exception {
		assertEquals(UserTypeEnum[].class,findMethod("findUserTypeEnumArray").getReturnType());
		
		Class parameterizedType = (Class)findMethod("findUserTypeEnumArray").getGenericReturnType();
		assertEquals(UserTypeEnum[].class,parameterizedType);
	}

	@Test
	public void test_findDoubleObject() throws Exception {
		assertEquals(Double.class,findMethod("findDoubleObject").getReturnType());
		
		Class parameterizedType = (Class)findMethod("findDoubleObject").getGenericReturnType();
		assertEquals(Double.class,parameterizedType);
	}

	private Method findMethod(String name) throws NoSuchMethodException {
		for(Method m : clazz.getMethods()) {
			if(m.getName().equals(name)) {
				return m;
			}
		}
		throw new IllegalArgumentException("not found method with name:"+name);
	}
	
}
