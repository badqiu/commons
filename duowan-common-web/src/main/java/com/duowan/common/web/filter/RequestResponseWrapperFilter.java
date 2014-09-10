package com.duowan.common.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
/**
 * 为HttpServletRequest,HttpServletResponse增加各种Wrapper的Filter <br/>
 * Wrapper可以实现FilterConfiger得到Filter的参数配置
 * 
 * <pre>
 * <b>配置参数</b>
 * requestWrappers: request的wrapper类名称，使用空格，逗号分隔多个类名称 
 * responseWrappers: response的wrapper类名称，使用空格，逗号分隔多个类名称 
 * </pre>
 * 
 * @author badqiu
 *
 */
public class RequestResponseWrapperFilter extends OncePerRequestFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(RequestResponseWrapperFilter.class);
	
	public List<Class<ServletRequestWrapper>> requestWrappers = new ArrayList<Class<ServletRequestWrapper>>();
	public List<Class<ServletResponseWrapper>> responseWrappers = new ArrayList<Class<ServletResponseWrapper>>();
	
	@Override @SuppressWarnings("all")
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		requestWrappers = FilterConfigHelper.getClassesInitParameter(getFilterConfig(),"requestWrappers");
		responseWrappers = FilterConfigHelper.getClassesInitParameter(getFilterConfig(),"responseWrappers");
		
		logger.info("RequestWrappers:"+requestWrappers);
		logger.info("ResponseWrappers:"+responseWrappers);
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest finalRequest = request;
		HttpServletResponse finalResponse = response;
		
		try {
			for(Class<ServletRequestWrapper> i : requestWrappers) {
				finalRequest = (HttpServletRequest)i.getConstructor(HttpServletRequest.class).newInstance(finalRequest);
				if(finalRequest instanceof FilterConfiger) {
					((FilterConfiger)finalRequest).setFilterConfig(getFilterConfig());
				}
			}
			for(Class<ServletResponseWrapper> i : responseWrappers) {
				finalResponse = (HttpServletResponse)i.getConstructor(HttpServletResponse.class).newInstance(finalResponse);
				if(finalResponse instanceof FilterConfiger) {
					((FilterConfiger)finalResponse).setFilterConfig(getFilterConfig());
				}
			}
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
		
		filterChain.doFilter(finalRequest, finalResponse);
	}

	private static class FilterConfigHelper {
		@SuppressWarnings("all")
		static List getClassesInitParameter(FilterConfig filterConfig,String paramName) {
			String str = filterConfig.getInitParameter(paramName);
			String[] array = StringUtils.tokenizeToStringArray(str, " \t\n,");
			List classes = new ArrayList();
			for(String item : array) {
				try {
					classes.add(ClassUtils.forName(item, ClassUtils.getDefaultClassLoader()));
				} catch (ClassNotFoundException e) {
					throw new RuntimeException(e);
				} catch (LinkageError e) {
					throw new RuntimeException(e);
				}
			}
			return classes;
		}
	}
	
	/**
	 * 如果wrapper需要配置参数，可以通过filterConfig得到配置
	 */
	public interface FilterConfiger {
		public void setFilterConfig(FilterConfig filterConfig);
	}
	
}
