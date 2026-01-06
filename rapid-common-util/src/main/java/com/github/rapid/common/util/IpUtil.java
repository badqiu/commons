package com.github.rapid.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IP地址相关工具类
 */
public class IpUtil {

	protected static final Logger log = LoggerFactory.getLogger(IpUtil.class);
	private static final String IP_SPLIT = ",";


	/***
	 * 
	 * @return
	 */
	public static String getLocalIpAddr() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.error("getLocalIpAddr.error:{}",e);
			return "127.0.0.1";
		}
	}

	
	/***
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIp(HttpServletRequest request) {
		String ipAddress = null;
		try {
			ipAddress = request.getHeader("x-forwarded-for");
			
			if (isEmptyOrUnknow(ipAddress)) {
				ipAddress = request.getHeader("Proxy-Client-IP");
			}
			if (isEmptyOrUnknow(ipAddress)) {
				ipAddress = request.getHeader("WL-Proxy-Client-IP");
			}
			if (isEmptyOrUnknow(ipAddress)) {
				ipAddress = request.getRemoteAddr();
			}
			ipAddress = getFirstIp(ipAddress);
		} catch (Exception e) {
			ipAddress = "";
			log.error("getIpAddr.error:{}",e);
		}
		return ipAddress;
	}


	private static String getFirstIp(String ipAddress) {
		if(StringUtils.isBlank(ipAddress)) {
			return ipAddress;
		}
		
		// 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
		if (ipAddress != null && ipAddress.length() > 15) {
			// = 15
			if (ipAddress.indexOf(",") > 0) {
				ipAddress = ipAddress.substring(0, ipAddress.indexOf(IP_SPLIT));
			}
		}
		return ipAddress;
	}


	private static boolean isEmptyOrUnknow(String ipAddress) {
		String unknow = "unknown";
		return StringUtils.isEmpty(ipAddress) || unknow.equalsIgnoreCase(ipAddress);
	}

	


}
