package com.duowan.common.log.aop;

import java.lang.reflect.Method;

import org.aopalliance.aop.Advice;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationClassFilter;
import org.springframework.aop.support.annotation.AnnotationMethodMatcher;

import com.duowan.common.log.Profiled;

/**
 * 基于Profiled标注的 PointcutAdvisor,通过该类，只拦截 有@Profiled的类及方法
 * 
 * 如果标注写在类上,将拦截类的所有方法
 * 如果标注写在方法上，但不写在类上，只拦截标注的方法
 * 
 * @author badqiu
 *
 */
public class ProfiledAnnotationPointcutAdvisor implements PointcutAdvisor{

	ProfilerMethodInteceptor profilerMethodInteceptor = new ProfilerMethodInteceptor();
	
	static AnnotationClassFilter classFilter = new AnnotationClassFilter(Profiled.class);
	static AnnotationMethodMatcher annotationMethodMatcher = new AnnotationMethodMatcher(Profiled.class){
		public boolean matches(Method method, Class targetClass) {
			if(classFilter.matches(targetClass)) {
				return true;
			}
			return super.matches(method, targetClass);
		};
	};
	
	Pointcut annotationMatchingPointcut = new Pointcut() {
		public ClassFilter getClassFilter() {
			return ClassFilter.TRUE;
		}

		public MethodMatcher getMethodMatcher() {
			return annotationMethodMatcher;
		}
	};

	public Advice getAdvice() {
		return profilerMethodInteceptor;
	}

	public boolean isPerInstance() {
		return true;
	}

	public Pointcut getPointcut() {
		return annotationMatchingPointcut;
	}
	
	public MethodMatcher getMethodMatcher() {
		return annotationMethodMatcher;
	}

}
