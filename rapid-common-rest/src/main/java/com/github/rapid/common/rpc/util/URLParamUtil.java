package com.github.rapid.common.rpc.util;

import java.util.Map;
import java.util.Set;

public class URLParamUtil {

	public static String serial2UrlParams(Map map) {
		if(map == null || map.isEmpty()) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		Set<Map.Entry> entrySet = map.entrySet();
		for(Map.Entry entry : entrySet) {
			Object key = entry.getKey();
			Object value = entry.getValue();
			if(value == null) {
				continue;
			}
			sb.append(key).append("=").append(value).append("&");
		}
		return sb.toString();
	}

	
}
