package com.github.rapid.common.rpc.fortest.api;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.github.rapid.common.rpc.fortest.api.model.Blog;
import com.github.rapid.common.rpc.fortest.api.model.ComplexObject;
import com.github.rapid.common.rpc.fortest.api.request.BlogQuery;
import com.github.rapid.common.rpc.fortestinvoker.UserTypeEnum;
import com.github.rapid.common.util.page.Page;

public interface BlogInfoService {
	Blog emptyParam();
	
	Blog findSingleBlog(String group,String blogId);
	
	Blog findByBlogQuery(BlogQuery query);
	
	public BlogQuery returnBlogQuery(BlogQuery query);
	
	LinkedList<Blog> findBlogLinkedList(String key);
	
	List<Blog> findBlogList(String key);
	
	List<Blog> genBlogList(int size);
	
	List<Map<String,Blog>> findBlogListMap(String key); //存在无限递归的可能
	
	Collection<Blog> findBlogCollection(String key);
	
	Set<Blog> findBlogSet(String key);
	
	Blog[] findBlogArray(String key);

	Map<String,Blog> findBlogMap(String key);
	
	Map<String,List<Blog>> findBlogMapList(String key);
	
	LinkedHashMap<String,Blog> findBlogLinkedHashMap(String key);
	
	UserTypeEnum findUserTypeEnum(String key);
	
	UserTypeEnum[] findUserTypeEnumArray(String key);
	
	Date findDate(String key);
	
	double findDouble(String key);
	
	Double findDoubleObject(String key);
	
	ComplexObject findComplexObject(String key);
	
	void voidThrowException(String key);
	
	Blog throwException(String key);
	
	Blog throwWebServiceException(String key);
	
	String complexArguments(Blog blog,String[] names,int[] args,Map<String,String> parameters,List<String> sex);
	
	String AComplex__methodDDDD123Name();
	
	void void_method();
	
	Blog null_return();
	
	RuntimeException return_exception();
	
	Map<String,String> map_arg(Map<String,String> map);
	List<Integer> list_arg(List<Integer> list);
	String[] array_arg(String[] array);
	
	public String return_input(String input);
	
	public String return_input_2_arg(String input,String age);
	
	public TreeSet<String> tree_set(String input);
	public Set<String> tree_set_but_return_set(String input);
	
	public LinkedHashSet<String> linked_hash_set(String input);
	public Set<String> linked_hash_set_but_return_set(String input);
	
	public LinkedHashMap<String,String> linked_hash_map(String input);
	public Map<String,String> linked_hash_map_but_return_map(String input);
	
	public TreeMap<String,String> tree_map(String input);
	public Map<String,String> tree_map_but_return_map(String input);
	
	public Page<String> pageQuery(int page,int pageSize);
}
