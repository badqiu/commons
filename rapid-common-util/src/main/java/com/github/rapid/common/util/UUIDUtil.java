package com.github.rapid.common.util;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class UUIDUtil {

	public static String randomUUID32() {
		return StringUtils.remove(UUID.randomUUID().toString(),'-');
	}
	
}
