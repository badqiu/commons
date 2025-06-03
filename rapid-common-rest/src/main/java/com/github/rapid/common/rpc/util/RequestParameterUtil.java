package com.github.rapid.common.rpc.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;

import jakarta.servlet.ServletRequest;

public class RequestParameterUtil {

	public static Map<String, Object> getParameters(ServletRequest request) {
		
		Assert.notNull(request, "Request must not be null");
		
		Enumeration paramNames = request.getParameterNames();
		Map<String, Object> params = new HashMap<String, Object>();
		while (paramNames != null && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			String[] values = request.getParameterValues(paramName);
			if (values == null || values.length == 0) {
				// Do nothing, no values found at all.
			} else if (values.length > 1) {
				params.put(paramName, values);
			} else {
				params.put(paramName, values[0]);
			}
		}
		return params;
		
	}
}
