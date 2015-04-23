package com.github.rapid.common.util;

import java.util.WeakHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 正则表达式工具类
 * @author badqiu
 *
 */
public class RegexUtils {
	
	private static WeakHashMap<String, Pattern> regexCache = new WeakHashMap<String, Pattern>();
	
	/**
	 * 通过正则找到相匹配的字符串
	 * 
	 * @param input 输入字符串
	 * @param regex 正则表达式
	 * @param regexGroup 正则表达式的group
	 * @return 返回匹配正则表达式的group字符串
	 */
	public static String findByRegexGroup(String input,String regex,int regexGroup) {
		if(input == null) return null;
		if(regex == null || regex.isEmpty()) throw new IllegalArgumentException("'regex' must be not null");
		Pattern p = getPatternFromCache(regex);
		
		Matcher m = p.matcher(input);
		if(m.find()) {
			return m.group(regexGroup);
		}
		return null;
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