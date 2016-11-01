package com.github.rapid.common.web.util;

import javax.servlet.http.HttpServletRequest;

public class ServletUtil {

	public static <T> T tryGetFromSession(HttpServletRequest request, T query) {
		String sessionKey = query.getClass().getName();
		if("true".equals(request.getParameter("useSessionParam"))) {
			T sessionQuery = (T)request.getSession().getAttribute(sessionKey);
			if(sessionQuery != null) {
				return sessionQuery;
			}
		};
		request.getSession().setAttribute(sessionKey, query);
		return query;
	}
	
}
