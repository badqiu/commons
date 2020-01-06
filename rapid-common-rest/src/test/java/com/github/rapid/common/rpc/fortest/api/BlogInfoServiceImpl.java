package com.github.rapid.common.rpc.fortest.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;

import com.github.rapid.common.rpc.WebServiceException;
import com.github.rapid.common.rpc.fortest.api.model.Blog;
import com.github.rapid.common.rpc.fortest.api.model.ComplexObject;
import com.github.rapid.common.rpc.fortest.api.request.BlogQuery;
import com.github.rapid.common.rpc.fortestinvoker.UserInfo;
import com.github.rapid.common.rpc.fortestinvoker.UserTypeEnum;
import com.github.rapid.common.util.page.Page;
import com.github.rapid.common.util.page.Paginator;


public class BlogInfoServiceImpl implements BlogInfoService {

	public Blog emptyParam() {
		return createBlog();
	}
	
	public static Blog createBlog() {
		return createWithBlog("1");
	}
	
	public static Blog createWithBlog(String id) {
		Blog r = new Blog(id);
		r.setByteObject((byte)100);
		r.setByteValue((byte)100);
//		r.setBigDecimal(new BigDecimal("100"));
		r.setBigInteger(new BigInteger("100"));
		r.setLongValue(100L);
		r.setLongObject(100L);
		r.setFloatValue((float)100.1);
		r.setFloatObject((float)100.1);
		r.setCharValue('A');
		r.setCharacter(new Character('a'));
		r.setDoubleObject((double)100);
		r.setDoubleValue((double)100);
		
		try {
			Date date = DateUtils.parseDate("2011-10-10", new String[]{"yyyy-MM-dd"});
			r.setDate(date);
			r.setTimestamp(new java.sql.Timestamp(date.getTime()));
			r.setSqlTime(new java.sql.Time(date.getTime()));
			r.setSqlDate(new java.sql.Date(date.getTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return r;
	}
	
	public Blog findSingleBlog(String group, String blogId) {
		return createBlog();
	}

	public Blog findByBlogQuery(BlogQuery query) {
		return createBlog();
	}
	
	public BlogQuery returnBlogQuery(BlogQuery query) {
		return query;
	}
	
	public LinkedList<Blog> findBlogLinkedList(String key) {
		LinkedList<Blog> list = new LinkedList<Blog>();
		list.add(createBlog());
		return list;
	}

	public List<Blog> findBlogList(String key) {
		LinkedList<Blog> list = new LinkedList<Blog>();
		list.add(createBlog());
		return list;
	}
	
	public List<Blog> genBlogList(int size) {
		List<Blog> list = new ArrayList<Blog>();
		for(int i = 0; i < size; i++) {
			list.add(createWithBlog(i+""));
		}
		return list;
	}

	public List<Map<String, Blog>> findBlogListMap(String key) {
		LinkedList<Map<String, Blog>> list = new LinkedList<Map<String, Blog>>();
		list.add(findBlogMap(key));
		return list;
	}

	public Collection<Blog> findBlogCollection(String key) {
		LinkedList<Blog> list = new LinkedList<Blog>();
		list.add(createBlog());
		return list;
	}

	public Set<Blog> findBlogSet(String key) {
		HashSet<Blog> list = new HashSet<Blog>();
		list.add(createBlog());
		return list;
	}

	public Blog[] findBlogArray(String key) {
		return new Blog[]{createWithBlog("1"),createWithBlog("2")};
	}

	public Map<String, Blog> findBlogMap(String key) {
		return findBlogLinkedHashMap(key);
	}

	public Map<String, List<Blog>> findBlogMapList(String key) {
		HashMap<String, List<Blog>> result = new HashMap<String, List<Blog>>();
		result.put("1", findBlogList(key));
		result.put("2", findBlogList(key));
//		result.put("3", null);
		return result;
	}

	public Map<String, String> nullValueMapList(String key) {
		HashMap<String,String> result = new HashMap();
		result.put("1", "1");
		result.put("null_key", null);
		return result;
	}
	
	public Map<String, List> emptyValueMapList(String key) {
		HashMap<String,List> result = new HashMap();
		result.put("3", Arrays.asList(1,2,3));
		result.put("empty_key", new ArrayList());
		return result;
	}
	
	public LinkedHashMap<String, Blog> findBlogLinkedHashMap(String key) {
		LinkedHashMap map = new LinkedHashMap();
		map.put("1", createBlog());
		map.put("2", createBlog());
		return map;
	}

	public UserTypeEnum findUserTypeEnum(String key) {
		return UserTypeEnum.SYSTEM;
	}

	public UserTypeEnum[] findUserTypeEnumArray(String key) {
		return Arrays.asList(UserTypeEnum.SYSTEM,UserTypeEnum.CUSTOM).toArray(new UserTypeEnum[0]);
	}

	public Date findDate(String key) {
		return new Date(199999999);
	}

	public double findDouble(String key) {
		return 100.1;
	}

	public Double findDoubleObject(String key) {
		return 100.1;
	}

	public ComplexObject findComplexObject(String key) {
		ComplexObject r = new ComplexObject();
		r.setUserInfoList(Arrays.asList(new UserInfo()));
		return r;
	}

	public Blog throwException(String key) {
		throw new RuntimeException("custom_unknow_error");
	}

	public Blog throwWebServiceException(String key) {
		throw new WebServiceException("test_error_no","test_error_message");
	}

	public String complexArguments(Blog blog, String[] names, int[] args,
			Map<String, String> parameters, List<String> sex) {
		return "complexArguments()"+blog+" names="+Arrays.toString(names)+" args="+Arrays.toString(args)+" parameters="+parameters+" sex="+sex;
	}

	public String AComplex__methodDDDD123Name() {
		return "AComplex__methodDDDD123Name() executed";
	}

	public void void_method() {
	}

	public Blog null_return() {
		return null;
	}
	
	public String return_input(String input) {
		return input;
	}
	
	public String return_input_2_arg(String input,String age) {
		return input+age;
	}

	public TreeSet<String> tree_set(String input) {
		return new TreeSet(treeMap().keySet());
	}

	public Set<String> tree_set_but_return_set(String input) {
		return new TreeSet(treeMap().keySet());
	}

	public LinkedHashSet<String> linked_hash_set(String input) {
		return new LinkedHashSet(linkedHashMap().keySet());
	}

	public Set<String> linked_hash_set_but_return_set(String input) {
		return new LinkedHashSet(linkedHashMap().keySet());
	}

	public LinkedHashMap<String, String> linked_hash_map(String input) {
		return stringLinkedHashMap();
	}

	public Map<Integer, Integer> linked_hash_map_but_return_map(String input) {
		return integerlinkedHashMap();
	}

	public TreeMap<String, String> tree_map(String input) {
		return treeMap();
	}

	public Map<String, String> tree_map_but_return_map(String input) {
		return treeMap();
	}
	
	private static TreeMap treeMap() {
		TreeMap map = new TreeMap();
		map.put("abc", 123);
		map.put("100", 100);
		map.put("1","1");
		map.put("9000", 100);
		map.put("1000", 100);
		map.put("4000", 100);
		map.put("5000", 100);
		map.put("8000", 100);
		return map;
	}
	
	private static LinkedHashMap<Integer,Integer> integerlinkedHashMap() {
		LinkedHashMap<Integer,Integer> map = new LinkedHashMap<Integer,Integer>();
		map.put(100, 100);
		map.put(5, 100);
		return map;
	}
	
	
	private static LinkedHashMap linkedHashMap() {
		LinkedHashMap map = new LinkedHashMap();
		map.put(100, 100);
		map.put("9000", 100);
		map.put("1000", 100);
		map.put("4000", 100);
		map.put("5000", 100);
		map.put("8000", 100);
		map.put(5, 100);
		return map;
	}
	
	private static LinkedHashMap stringLinkedHashMap() {
		LinkedHashMap map = new LinkedHashMap();
		map.put("9000", 100);
		map.put("1000", 100);
		map.put("4000", 100);
		map.put("5000", 100);
		map.put("8000", 100);
		return map;
	}

	public void voidThrowException(String key) {
		throw new RuntimeException("voidThrowException");
	}

	public RuntimeException return_exception() {
		return new RuntimeException("return_exception");
	}

	public Map map_arg(Map<String, String> map) {
		LinkedHashMap result = stringLinkedHashMap();
		result.putAll(map);
		return result;
	}

	public List list_arg(List<Integer> list) {
		List result = list;
		result.add(1);
		return result;
	}

	public String[] array_arg(String[] array) {
		return new String[]{Arrays.toString(array)};
	}

	@Override
	public Page<String> pageQuery(int page, int pageSize) {
		List list = new ArrayList();
		int total = 20;
		for(int i = 0; i < total; i++) {
			list.add(i);
		}
		Paginator p = new Paginator(page, pageSize, total);
		return new Page(list.subList(p.getStartRow(),p.getEndRow()),p);
	}

	@Override
	public String listKeys(List<String> keys) {
		return StringUtils.join(keys,",");
	}


}
