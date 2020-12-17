package com.github.rapid.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
/**
 * @author badqiu
 */
public class MapUtil {
	
	@SuppressWarnings("all")
	public static Map trimKeyValue(Map p) {
		if(p == null) return null;
		
		Map result = new HashMap();
		p.forEach((key,value) -> {
			result.put(trimObject(key), trimObject(value));
		});
		return result;
	}

	private static Object trimObject(Object v) {
		if(v == null) return null;
		if(v instanceof String) {
			return StringUtils.trim((String)v);
		}
		return v;
	}
	
	@SuppressWarnings("all")
	public static void putIfNull(Map map,Object key,Object defaultValue) {
		if(key == null)
			throw new IllegalArgumentException("key must be not null");
		if(map == null)
			throw new IllegalArgumentException("map must be not null");
		if(map.get(key) == null) {
			map.put(key, defaultValue);
		}
	}
	
	/**
	 * 将所有key 转换为小写
	 * @param list
	 * @return
	 */
	public static List<Map<String, Object>> allMapKey2LowerCase(
			List<Map<String, Object>> list) {
		List<Map<String,Object>> result = new ArrayList(list.size());
		for(Map<String,Object> row : list) {
			Map newRow = new HashMap();
			for(Map.Entry<String, Object> entry : row.entrySet()) {
				newRow.put(StringUtils.lowerCase(entry.getKey()), entry.getValue());
			}
			result.add(newRow);
		}
		return result;
	}
	
	public static Map newMap(Object... args) {
		Map map = new HashMap();
		for(int i = 0; i < args.length; i+=2) {
			map.put(args[i], args[i+1]);
		}
		return map;
	}
	
	public static Map newLinkedMap(Object... args) {
		Map map = new LinkedHashMap();
		for(int i = 0; i < args.length; i+=2) {
			map.put(args[i], args[i+1]);
		}
		return map;
	}
	
	public static Map toMap(Object[] array,String...keys) {
		return ArrayUtil.toMap(array, keys);
	}
	
	/**
	 * Map进行string join
	 * @param map
	 * @param seperator 分隔符
	 * @param itemBuilder　k,v字符串构建器
	 * @return
	 */
	public static String mapStringJoin(Map map,String seperator,BiFunction<String, String, String> itemBuilder) {
		if(MapUtils.isEmpty(map)) return "";
		
		StringBuilder sb = new StringBuilder();
		boolean first = true;
		Set<Map.Entry<Object,Object>> entrySet = map.entrySet();
		for(Map.Entry<Object,Object> entry : entrySet) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			String k = StringUtils.trim(key == null ? "" : String.valueOf(key));
			String v = StringUtils.trim(value == null ? "" : String.valueOf(value));
			
			if(first) {
				first = false;
			}else {
				sb.append(seperator);
			}
			sb.append(itemBuilder.apply(k, v));
		}
		
		return sb.toString();
	}
	
}
