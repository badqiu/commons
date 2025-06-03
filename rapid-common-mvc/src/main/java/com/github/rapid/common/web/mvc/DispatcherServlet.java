package com.github.rapid.common.web.mvc;

import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 从spring DispatcherServlet继承，以便于扩展
 * 
 * @author badqiu
 *
 */
public class DispatcherServlet extends org.springframework.web.servlet.DispatcherServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void render(ModelAndView mv, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		super.render(mv, request, response);
	}
}
