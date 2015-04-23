package com.duowan.common.web.wrapper;

import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;

/**
 * 将HttpServletRequest.getRemoteAdd()能够获取真实的IP地址，通过nginx代理服务器代理请求的IP地址会存放在X-
 * Forwarded-For。
 * 
 * @author badqiu
 * 
 */
public class RealRemoteAddrRequestWrapper extends HttpServletRequestWrapper {

	public RealRemoteAddrRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public String getRemoteAddr() {
		return getRealIp((HttpServletRequest) getRequest());
	}

	static String getRealIp(HttpServletRequest request) {

		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isEmpty(ip)) {
			return request.getRemoteAddr();
		}

		int index = ip.lastIndexOf(',');
		String lastip = ip.substring(index + 1).trim();

		if ("127.0.0.1".equals(lastip) || !isLicitIp(lastip)) {
			return request.getRemoteAddr();
		}
		return lastip;
	}

	private static final java.util.regex.Pattern IS_LICIT_IP_PATTERN = java.util.regex.Pattern
			.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$");
	
	/**
	 * 是否合法IP地址 
	 **/
	static boolean isLicitIp(final String ip) {
		if (StringUtils.isEmpty(ip)) {
			return false;
		}

		Matcher m = IS_LICIT_IP_PATTERN.matcher(ip);
		if (!m.find()) {
			return false;
		}
		return true;
	}
}