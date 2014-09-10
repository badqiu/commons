package com.duowan.common.web.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

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
