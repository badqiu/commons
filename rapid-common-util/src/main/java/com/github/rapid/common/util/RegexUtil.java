package com.github.rapid.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
/**
 * 正则表达式工具类
 * @author badqiu
 *
 */
public class RegexUtil {
	
	private static WeakHashMap<String, Pattern> regexCache = new WeakHashMap<String, Pattern>();
	
	/**
	 * 通过正则找到相匹配的字符串
	 * 
	 * <pre>
	 * 示例：
	 * input=abc123, regex=(a).*(123), group=0 => abc123  
	 * input=abc123, regex=(a).*(123), group=1 => a
	 * input=abc123, regex=(a).*(123), group=2 => 123
	 * </pre>
	 * @param input 输入字符串
	 * @param regex 正则表达式
	 * @param regexGroup 正则表达式的group
	 * @return 返回匹配正则表达式的group字符串
	 * 
	 * 
	 */
	public static String findByRegexGroup(String input,String regex,int regexGroup) {
		if(input == null) return null;
		if(regex == null || regex.isEmpty()) throw new IllegalArgumentException("'regex' must be not empty");
		Pattern p = getPatternFromCache(regex);
		
		Matcher m = p.matcher(input);
		if(m.find()) {
			return m.group(regexGroup);
		}
		return null;
	}

	public static List<String> findAllByRegexGroup(String input,String regex,int regexGroup) {
		if(StringUtils.isBlank(input))
			return Collections.EMPTY_LIST;
		if(regex == null || regex.isEmpty()) throw new IllegalArgumentException("'regex' must be not empty");
		
		Pattern p = getPatternFromCache(regex);
		Matcher m = p.matcher(input);
		List<String> result = new ArrayList<String>();
		while(m.find()) {
			result.add(m.group(regexGroup));
		}
		return result;
	}
	
	/**
	 * 正则正确式cache
	 * @param regex
	 * @return
	 */
	public static Pattern getPatternFromCache(String regex) {
		Pattern p = regexCache.get(regex);
		if(p == null) {
			p = Pattern.compile(regex);
			synchronized (regexCache) {
				regexCache.put(regex, p);
			}
		}
		return p;
	}
	
}