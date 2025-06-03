package com.github.rapid.common.log.filter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.github.rapid.common.log.ProfilerDigestLog;
import com.github.rapid.common.log.ProfilerLogger;
import com.github.rapid.common.util.Profiler;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
/**
 * Profiler过滤器
 * 实现Profiler.start(),Profiler.release()并记录日志ProfilerLogger.info()
 * 
 * @author badqiu
 *
 */
public class ProfilerLoggerFilter extends OncePerRequestFilter implements Filter {

	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if(ProfilerLogger.isInfoEnabled()) {
			Profiler.start(getProfilerStartMessage(request));
			ProfilerDigestLog.getDigestLogContext().put(ProfilerDigestLog.KEY_CLIENT_IP, IPUtil.getRealIp(request));
		}
		
		Exception exception = null;
		try {
			filterChain.doFilter(request, response);
		}catch(RuntimeException e) {
			exception = e;
			throw e;
		}finally {
			
			if(ProfilerLogger.isInfoEnabled()) {
				Profiler.release(exception);
				ProfilerLogger.infoDigestLogAndDump();
			}
			
		}
	}

	protected String getProfilerStartMessage(HttpServletRequest request) {
		return request.getRequestURI();
	}

	private static class IPUtil {
		
		/**
		 * 通过request文件头拿到代理IP地址
		 * 如果代理IP超过5个，则取前面5个
		 */
		public static String getProxyIp(HttpServletRequest request) {
			String ip = request.getHeader("X-Forwarded-For");
			if (StringUtils.isBlank(ip)) {
				return "";
			}
			String[] ips = ip.split(", ");
			if(ips.length > 5)
			{
				return ips[0]+", "+ips[1]+", "+ips[2]+", "+ips[3]+", "+ips[4];
			}
			return ip;
		}
		
		/**
		 * 通过request文件头拿到真实IP地址
		 */
		public static String getRealIp(HttpServletRequest request) {
			String forwardedIp = request.getHeader("X-Forwarded-For");
			if (StringUtils.isBlank(forwardedIp)) {
				return request.getRemoteAddr();
			}
			String[] split = forwardedIp.split(", ");
			if(ArrayUtils.isEmpty(split)) {
				return request.getRemoteAddr();
			}
			
			forwardedIp = split[0].trim();
			if ("127.0.0.1".equals(forwardedIp) || !isLicitIp(forwardedIp)) {
				return request.getRemoteAddr();
			}
			return forwardedIp;
		}

		static String regex = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
		static Pattern p = Pattern.compile(regex);
		public static boolean isLicitIp(String ip) {
			if (StringUtils.isBlank(ip))
				return false;
			Matcher m = p.matcher(ip);
			return m.find();
		}
	}

}
