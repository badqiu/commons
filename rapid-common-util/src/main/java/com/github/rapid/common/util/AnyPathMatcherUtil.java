package com.github.rapid.common.util;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.AntPathMatcher;

public class AnyPathMatcherUtil {

	static AntPathMatcher antPathMatcher = new AntPathMatcher();
	
	public static boolean match(String path, List<String> includePatterns,List<String> excludePatterns) {
		if(CollectionUtils.isEmpty(includePatterns)) return false;
		
		if(AnyPathMatcherUtil.match(path, excludePatterns)) {
			return false;
		}
		
		if(AnyPathMatcherUtil.match(path, includePatterns)) {
			return true;
		}
		return false;
	}
	
	public static boolean match(String path, List<String> patternList) {
		if(CollectionUtils.isEmpty(patternList)) {
			return false;
		}
		
		for(String pattern : patternList) {
			if(path.equals(pattern)) {
				return true;
			}
			
			if(antPathMatcher.match(pattern, path)) {
				return true;
			}
		}
		return false;
	}
	
}
