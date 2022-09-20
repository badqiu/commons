package com.github.rapid.common.web.util;

import javax.servlet.FilterConfig;

import org.apache.commons.lang.StringUtils;
/**
 * FilterConfig用于得到参数的工具类
 * 
 * @author badqiu
 *
 */
public class FilterConfigUtil {

	public static String getParameter(FilterConfig config,String key,String defaultValue) {
		if(config == null) return defaultValue;
		
		String v = config.getInitParameter(key);
		return StringUtils.isBlank(v) ? defaultValue : v;
	}

	public static String[] getParameterArray(FilterConfig config,String key) {
		String v = config.getInitParameter(key);
		return StringUtils.isBlank(v) ? new String[0] : org.springframework.util.StringUtils.tokenizeToStringArray(v, " \t\n,");
	}
	
   public static boolean getBooleanParameter(FilterConfig config,String key,boolean defaultValue) {
        String v = getParameter(config,key,Boolean.toString(defaultValue));
        try {
            return Boolean.parseBoolean(v);
        }catch(Exception e) {
            throw new IllegalArgumentException("cannot parse value:"+v+" for boolean by key:"+key);
        }
    }
	   
	public static int getIntParameter(FilterConfig config,String key,int defaultValue) {
		String v = getParameter(config,key,Integer.toString(defaultValue));
		try {
		    return Integer.parseInt(v);
		}catch(Exception e) {
		    throw new IllegalArgumentException("cannot parse value:"+v+" for int by key:"+key);
		}
	}

	public static int getIntegerParameter(FilterConfig config,String key,Integer defaultValue) {
		String v = config.getInitParameter(key);
		if(v == null)
			return defaultValue;
		else
			return new Integer(v);
	}
}
