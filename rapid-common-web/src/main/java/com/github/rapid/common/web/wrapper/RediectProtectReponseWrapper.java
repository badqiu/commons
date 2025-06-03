package com.github.rapid.common.web.wrapper;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.github.rapid.common.web.filter.RequestResponseWrapperFilter.FilterConfiger;
import com.github.rapid.common.web.util.FilterConfigUtil;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
/**
 * 通过白名单的方式，保护重定向的链接是可信的 <br/>
 * 
 * FilterConfig参数名称为: safe_host_white_list
 * @author badqiu
 *
 */
public class RediectProtectReponseWrapper extends HttpServletResponseWrapper implements FilterConfiger{
	private FilterConfig filterConfig;
	private String[] safeHosts = null;
	public RediectProtectReponseWrapper(HttpServletResponse response) {
		super(response);
	}
	
	@Override
	public void sendRedirect(String location) throws IOException {
		if(isSafe(location)) {
			super.sendRedirect(location);
		}else {
			throw new UnsafeRedirectException(location);
		}
	}
	
	/**
	 * 判断URL是否安全 
	 **/
	private boolean isSafe(String location) {
		if(location.startsWith("http:") || location.startsWith("https:")) {
			return isSafeHost(location);
		}else {
			return true;
		}
	}

	/**
	 * 判断URL主机是否安全 
	 **/
	private boolean isSafeHost(String location) {
		try {
			String host = new URL(location).getHost();
			
			for(String safeHost : safeHosts) {
				if(StringUtils.endsWith(StringUtils.trim(host), safeHost)) {
					return true;
				}
			}
			return false;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public void setFilterConfig(FilterConfig filterConfig) {
		this.filterConfig = filterConfig;
		this.safeHosts = FilterConfigUtil.getParameterArray(filterConfig,"safe_host_white_list");
		Assert.noNullElements(safeHosts,"'safe_host_white_list' FilterConfig parameter must be not empty");
	}

	
}
