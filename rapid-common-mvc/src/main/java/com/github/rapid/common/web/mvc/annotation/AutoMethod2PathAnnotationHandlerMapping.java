package com.github.rapid.common.web.mvc.annotation;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class AutoMethod2PathAnnotationHandlerMapping extends RequestMappingHandlerMapping {
	static Logger log = LoggerFactory.getLogger(AutoMethod2PathAnnotationHandlerMapping.class);
	
    public AutoMethod2PathAnnotationHandlerMapping() {
        setUseSuffixPatternMatch(false); // 替代 setUseDefaultSuffixPattern(false)
    }

    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
        // 1. 优先调用父类逻辑
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
        
        if(handlerType.getName().contains("springdoc")) {
        	pringInfoLog(method, handlerType, info);
        	return info;
        }
        
        
        // 2. 实现约定逻辑：无显式路径时使用方法名
        RequestMapping methodAnnotation = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        if (methodAnnotation != null && methodAnnotation.value().length == 0) {
            // 生成约定路径（如 /create）
            String path = method.getName();
            if (!path.startsWith("/")) path = "/" + path;
            
            // 3. 合并父类路径与方法路径
            RequestMappingInfo conventionInfo = RequestMappingInfo
                .paths(path)
                .methods(methodAnnotation.method())
                .params(methodAnnotation.params())
                .headers(methodAnnotation.headers())
                .consumes(methodAnnotation.consumes())
                .produces(methodAnnotation.produces())
                .build();
            
            // 4. 与父类路径合并
            info = (info != null) ? info.combine(conventionInfo) : conventionInfo;
        }
        
        pringInfoLog(method, handlerType, info);
        return info;
    }

	private void pringInfoLog(Method method, Class<?> handlerType, RequestMappingInfo info) {
		if(info == null) return;
		
		log.info("getMappingForMethod() "+handlerType.getSimpleName()+"."+method.getName()+"() ==> "+info);
	}

    // 5. 注册映射（Spring 5.3+ 要求）
    @Override
    protected void registerHandlerMethod(Object handler, Method method, RequestMappingInfo mapping) {
        super.registerHandlerMethod(handler, method, mapping);
    }
}