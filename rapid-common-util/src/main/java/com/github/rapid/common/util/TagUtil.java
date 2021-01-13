package com.github.rapid.common.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

public class TagUtil {
	/**
	 * 对tags进行规整格式化
	 * @param tags
	 * @return
	 */
	public static String format(String tags) {
		return toString(parseTags(tags));
	}
	
	/**
	 * 解析tags并返回Set
	 * @param tags
	 * @return
	 */
	public static Set<String> parseTags(String tags) {
		if(StringUtils.isBlank(tags)) {
			return new HashSet<String>();
		}
		
		String[] tagsArray = StringUtils.split(tags, ',');
		return trimArray(tagsArray);
	}

	private static Set<String> trimArray(String[] tagsArray) {
		Set<String> result = new HashSet<String>();
		for(int i = 0; i < tagsArray.length; i++) {
			String tag = StringUtils.trim(tagsArray[i]);
			if(StringUtils.isBlank(tag)) {
				continue;
			}
			result.add(tag);
		}
		return  result;
	}
	
	/**
	 * tags进行join,并排重
	 * @param tags
	 * @return
	 */
	public static String toString(Collection tags) {
		if(tags == null || tags.isEmpty()) return null;
		
		String tagsStr = StringUtils.join(new HashSet(tags),",");
		return tagsStr;
	}
	
}
