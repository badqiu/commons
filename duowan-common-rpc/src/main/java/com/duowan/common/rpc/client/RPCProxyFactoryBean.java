package com.duowan.common.rpc.client;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.FactoryBean;

import com.duowan.common.rpc.server.MethodInvoker;
import com.duowan.common.rpc.util.SampleValueUtil;


public class RPCProxyFactoryBean extends HttpRPCClientInterceptor implements FactoryBean<Object>{

	private Object serviceProxy;

	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		
		ProxyFactory proxyFactory = new ProxyFactory(new Class[]{getServiceInterface()});
		proxyFactory.addAdvice(this);
		
		this.serviceProxy = proxyFactory.getProxy(getBeanClassLoader());
		
		for(Method method : getServiceInterface().getDeclaredMethods()) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			String sampleParameters = MethodInvoker.KEY_PARAMETERS+"="+StringUtils.join(SampleValueUtil.getSampleValue(parameterTypes),MethodInvoker.PARAMETERS_SEPERATOR);
			String methodInvokeUrl = getServiceUrl() + "/" + method.getName() + (parameterTypes.length > 0 ? "?" + sampleParameters : "");
			logger.info("method invoke url:"+methodInvokeUrl);
		}
	}

	public Object getObject() {
		return this.serviceProxy;
	}

	public Class<?> getObjectType() {
		return getServiceInterface();
	}

	public boolean isSingleton() {
		return true;
	}

	
}
