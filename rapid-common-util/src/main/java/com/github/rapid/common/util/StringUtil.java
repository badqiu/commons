package com.github.rapid.common.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {
	
	/**
	 * Convert a name in camelCase to an underscored name in lower case.
	 * Any upper case letters are converted to lower case with a preceding underscore.
	 * @param name the string containing original name
	 * @return the converted name
	 */
	public static String underscore(String name) {
		if(StringUtils.isBlank(name)) {
			return name;
		}
		
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toLowerCase());
			for (int i = 1; i < name.length(); i++) {
				char c = name.charAt(i);
				if (Character.isUpperCase(c) && !Character.isDigit(c)) {
					result.append("_");
					result.append(Character.toLowerCase(c));
				}
				else {
					result.append(c);
				}
			}
		}
		return result.toString();
	}
	
	public static String camelCase(String name) {
		if(StringUtils.isBlank(name)) {
			return name;
		}
		
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toLowerCase());
			
			boolean preIsUnderscore = false;
			for (int i = 1; i < name.length(); i++) {
				char c = name.charAt(i);
				if (c == '_') {
					preIsUnderscore = true;
					continue;
				}
				
				if(preIsUnderscore) {
					result.append(Character.toUpperCase(c));
				}else {
					result.append(c);
				}
				preIsUnderscore = false;
			}
		}
		return result.toString();
	}
}
