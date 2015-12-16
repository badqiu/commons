package com.github.rapid.common.web.mvc;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
/**
 * 约定大于配置的HandlerMapping
 * 为RequestMapping标注的method 生成默认path
 * 示例:  UserController.create() => /create.do 的url默认映射
 * 
 * @author badqiu
 *
 */
public class ConventionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

	public static final String DEFAULT_ACTION_EXT = ".do";
	
	private RequestMappingInfo.BuilderConfiguration config = new RequestMappingInfo.BuilderConfiguration();
	
	@Override
	protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
		RequestMappingInfo info = createRequestMappingInfo(method);
		if (info != null) {
			RequestMappingInfo typeInfo = createRequestMappingInfo(handlerType);
			if (typeInfo != null) {
				info = typeInfo.combine(info);
			}
		}
		return info;
	}
	
	/**
	 * Delegates to {@link #createRequestMappingInfo(RequestMapping, RequestCondition)},
	 * supplying the appropriate custom {@link RequestCondition} depending on whether
	 * the supplied {@code annotatedElement} is a class or method.
	 * @see #getCustomTypeCondition(Class)
	 * @see #getCustomMethodCondition(Method)
	 */
	private RequestMappingInfo createRequestMappingInfo(AnnotatedElement element) {
		RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
		RequestCondition<?> condition = (element instanceof Class<?> ?
				getCustomTypeCondition((Class<?>) element) : getCustomMethodCondition((Method) element));
		return (requestMapping != null ? createRequestMappingInfo(requestMapping, condition,element) : null);
	}
	
	protected RequestMappingInfo createRequestMappingInfo(RequestMapping requestMapping,RequestCondition<?> customCondition,AnnotatedElement element) {
		String[] path = requestMapping.path();
		if(path == null || path.length == 0) {
			if(element instanceof Method) {
				Method method = (Method)element;
				path = new String[]{method.getName()+DEFAULT_ACTION_EXT};
			}
		}
		
		return RequestMappingInfo
				.paths(resolveEmbeddedValuesInPatterns(path))
				.methods(requestMapping.method())
				.params(requestMapping.params())
				.headers(requestMapping.headers())
				.consumes(requestMapping.consumes())
				.produces(requestMapping.produces())
				.mappingName(requestMapping.name())
				.customCondition(customCondition)
				.options(config)
				.build();
	}
	
	
	@Override
	public void afterPropertiesSet() {
		this.config = new RequestMappingInfo.BuilderConfiguration();
		this.config.setPathHelper(getUrlPathHelper());
		this.config.setPathMatcher(getPathMatcher());
		this.config.setSuffixPatternMatch(useSuffixPatternMatch());
		this.config.setTrailingSlashMatch(useTrailingSlashMatch());
		this.config.setRegisteredSuffixPatternMatch(useRegisteredSuffixPatternMatch());
		this.config.setContentNegotiationManager(getContentNegotiationManager());

		super.afterPropertiesSet();
	}
	
}
