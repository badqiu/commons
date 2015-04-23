package com.duowan.common.web.filter.xss;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.duowan.common.util.XssUtil;

/**
 * XSS攻击保护filter,如果检查失败，将会抛出XssException
 * 可以通过excludes配置不需要过滤的requestURI
 * @author badqiu
 *
 */
public class XSSFilter extends OncePerRequestFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(XSSFilter.class);
	
	private HashMap<String,Boolean> excludeMap = null;
 
    @Override
    protected void initFilterBean() throws ServletException {
    	String excludes = getFilterConfig().getInitParameter("excludes");
    	if(StringUtils.hasText(excludes)) {
    		excludeMap = new HashMap();
    		for(String str : StringUtils.tokenizeToStringArray(excludes, ",")) {
    			excludeMap.put(str, true);
    		}
    		logger.info("XSS requestURL excludes:"+excludeMap.keySet());
    	}
    }
    
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest req = (HttpServletRequest)request;
		if(excludeMap != null && (excludeMap.get(req.getRequestURI()) != null)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		if(isMultipartContent(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		
		XssUtil.checkXSS(request.getParameterMap());
		filterChain.doFilter(request, response);
	}
	
	/**
     * Part of HTTP content type header.
     */
	private static final String MULTIPART = "multipart/";
	private static final boolean isMultipartContent(
            HttpServletRequest request) {
        if (!"post".equals(request.getMethod().toLowerCase())) {
            return false;
        }
        String contentType = request.getContentType();
        if (contentType == null) {
            return false;
        }
        if (contentType.toLowerCase().startsWith(MULTIPART)) {
            return true;
        }
        return false;
    }
 
}