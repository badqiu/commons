package com.github.rapid.common.web.scope;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;
/**
 * <pre>
 * 配合Flash需要使用的Filter,并且会在request中设置flash
 * request.setAttribute("flash",Flash.current().getData());
 * </pre>
 * 
 * @see Flash
 * @author badqiu
 */
public class FlashFilter  extends OncePerRequestFilter implements Filter{

	private Logger logger = LoggerFactory.getLogger(FlashFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain chain)throws ServletException, IOException {
		try {
			Flash.setCurrent(Flash.restore(request));
			request.setAttribute("flash",Flash.current().getData());
			chain.doFilter(request, response);
		}finally {
			Flash flash = Flash.current();
			Flash.setCurrent(null);
			if(flash != null) {
				try {
					flash.save(request, response);
				}catch(Exception e) {
					//ignore
					logger.warn("Flash.save() error,exception:"+e,e);
				}
			}
		}
	}

}
