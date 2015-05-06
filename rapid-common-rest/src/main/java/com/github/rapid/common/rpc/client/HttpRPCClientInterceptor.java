package com.github.rapid.common.rpc.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.util.Arrays;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.remoting.support.RemoteAccessor;

import com.github.rapid.common.rpc.RPCRequest;
import com.github.rapid.common.rpc.RPCResponse;
import com.github.rapid.common.rpc.SerDe;
import com.github.rapid.common.rpc.SerDeMapping;
import com.github.rapid.common.rpc.WebServiceException;
import com.github.rapid.common.rpc.serde.JsonSerDeImpl;
import com.github.rapid.common.rpc.util.CustomParameterizedType;

public class HttpRPCClientInterceptor extends RemoteAccessor implements MethodInterceptor,InitializingBean {
	static protected final Logger logger = LoggerFactory.getLogger(HttpRPCClientInterceptor.class);
	
	private String serviceUrl;

	private HttpInvokerRequestExecutor httpInvokerRequestExecutor;

	private int[] retryIntervalMills = null;
	/**
	 * Set the HttpInvokerRequestExecutor implementation to use for executing
	 * remote invocations.
	 * <p>Default is {@link SimpleHttpInvokerRequestExecutor}. Alternatively,
	 * sophisticated needs.
	 * @see SimpleHttpInvokerRequestExecutor
	 * @see CommonsHttpInvokerRequestExecutor
	 */
	public void setHttpInvokerRequestExecutor(HttpInvokerRequestExecutor httpInvokerRequestExecutor) {
		this.httpInvokerRequestExecutor = httpInvokerRequestExecutor;
		logger.info("RPC: use HttpInvokerRequestExecutor:"+httpInvokerRequestExecutor);
	}
	
	public void setRetryIntervalSeconds(int[] retryIntervalSeconds) {
		int[] mills = new int[retryIntervalSeconds.length];
		for(int i = 0; i < retryIntervalSeconds.length; i++) {
			mills[i] = retryIntervalSeconds[i] * 1000;
		}
		setRetryIntervalMills(mills);
	}

	public void setRetryIntervalMills(int[] retryIntervalMills) {
		this.retryIntervalMills = retryIntervalMills;
	}
	
	
	private static boolean isHttpClientAvaiable = false;
	static {
		try {
			Class.forName("org.apache.commons.httpclient.HttpClient");
			isHttpClientAvaiable = true;
		} catch (ClassNotFoundException e) {
		}
	}
	/**
	 * Return the HttpInvokerRequestExecutor used by this remote accessor.
	 * <p>Creates a default SimpleHttpInvokerRequestExecutor if no executor
	 * has been initialized already.
	 */
	public HttpInvokerRequestExecutor getHttpInvokerRequestExecutor() {
		if (this.httpInvokerRequestExecutor == null) {
			if(isHttpClientAvaiable) {
				logger.info("RPC:found org.apache.commons.httpclient.HttpClient avaiable for RPC HttpInvokerRequestExecutor,use 'CommonsHttpInvokerRequestExecutor'");
				AbstractHttpInvokerRequestExecutor executor = new CommonsHttpInvokerRequestExecutor();
				executor.setBeanClassLoader(getBeanClassLoader());
				this.httpInvokerRequestExecutor = executor;
			}else {
				logger.info("RPC:not found org.apache.commons.httpclient.HttpClient for RPC HttpInvokerRequestExecutor,use 'SimpleHttpInvokerRequestExecutor',please import commons-httpclient.jar in classpath for better performance");
				AbstractHttpInvokerRequestExecutor executor = new SimpleHttpInvokerRequestExecutor();
				executor.setBeanClassLoader(getBeanClassLoader());
				this.httpInvokerRequestExecutor = executor;
			}
		}
		return this.httpInvokerRequestExecutor;
	}

	public void afterPropertiesSet() {
		// Eagerly initialize the default HttpInvokerRequestExecutor, if needed.
		getHttpInvokerRequestExecutor();
		logger.info("remote webservice serviceUrl:"+getServiceUrl()+" retryIntervalMills:"+Arrays.toString(retryIntervalMills));
	}

	public Object invoke(final MethodInvocation methodInvocation) throws Throwable {
		long start = 0;
		if(logger.isDebugEnabled()) {
			start = System.currentTimeMillis();
		}
		
		try {
			return invoke0(methodInvocation);
		}finally {
			if(logger.isDebugEnabled()) {
				long cost = System.currentTimeMillis() - start;
				logger.debug("client invoke rpc:"+getServiceUrl()+"/"+methodInvocation.getMethod().getName()+" cost time:"+(cost/1000f));
			}
		}
	}

	
	private Object invoke0(final MethodInvocation methodInvocation) {
		if (AopUtils.isToStringMethod(methodInvocation.getMethod())) {
			return "HTTP invoker proxy for service URL [" + getServiceUrl() + "]";
		}

		RPCRequest invocation = createRemoteInvocation(methodInvocation);
		RPCResponse result = null;
		try {
			HttpResponse response = retryIfErrorOnExecuteRequest(invocation, methodInvocation);
			ParameterizedType returnType;
			if(methodInvocation.getMethod().getGenericReturnType().equals(void.class)) {
				returnType = new CustomParameterizedType(RPCResponse.class,null,new Type[]{Object.class});
			}else {
				returnType = new CustomParameterizedType(RPCResponse.class,null,new Type[]{methodInvocation.getMethod().getGenericReturnType()});
			}
			result = deserializeByContentType(response.getHeaders().get("Content-Type"),response.getBody(),returnType);
		}
		catch (Throwable ex) {
			throw convertHttpInvokerAccessException(ex,methodInvocation);
		}
		if(StringUtils.isNotEmpty(result.getErrCode())) {
			throw new WebServiceException(result.getErrCode(),result.getErrMsg(),getServiceUrl(),methodInvocation.getMethod().getName());
		}
		if(methodInvocation.getMethod().getGenericReturnType().equals(void.class)) {
			return null;
		}
		try {
			return recreateRemoteInvocationResult(result);
		}
		catch (Throwable ex) {
			throw new WebServiceException(WebServiceException.UNKNOW_ERROR,"Invocation of method [" + methodInvocation.getMethod() +
					"] failed in HTTP invoker remote service at [" + getServiceUrl() + "]",getServiceUrl(),methodInvocation.getMethod().getName(), ex);
		}
	}
	
	private SerDe defaultSerDe = new JsonSerDeImpl();
	private RPCResponse deserializeByContentType(String contentType, InputStream body,ParameterizedType returnType) {
		SerDe serDe = SerDeMapping.DEFAULT_MAPPING.getSerDeByContentType(contentType,defaultSerDe);
		RPCResponse result = (RPCResponse)serDe.deserialize(body, returnType,null); 
		return result;
	}
	
	private Object recreateRemoteInvocationResult(RPCResponse result) {
		return result.getResult();
	}

	private RPCRequest createRemoteInvocation(MethodInvocation methodInvocation) {
		RPCRequest request = new RPCRequest();
		request.setArguments(methodInvocation.getArguments());
		request.setMethod(methodInvocation.getMethod().getName());
		return request;
	}

	/**
	 * Execute the given remote invocation via the HttpInvokerRequestExecutor.
	 * Can be overridden to react to the specific original MethodInvocation.
	 * @param invocation the RemoteInvocation to execute
	 * @param originalInvocation the original MethodInvocation (can e.g. be cast
	 * to the ProxyMethodInvocation interface for accessing user attributes)
	 * @return the RemoteInvocationResult object
	 * @throws Exception in case of errors
	 */
	protected HttpResponse executeRequest(
			RPCRequest invocation, MethodInvocation originalInvocation) throws Exception {
		return executeRequest(invocation);
	}

	protected HttpResponse retryIfErrorOnExecuteRequest(
			RPCRequest invocation, MethodInvocation originalInvocation) throws Exception {
		if(retryIntervalMills  == null) {
			return executeRequest(invocation, originalInvocation);
		}else {
			IOException lastException = null;
			for(int i = 0; i < retryIntervalMills .length; i++) {
				try {
					return executeRequest(invocation, originalInvocation);
				}catch(IOException e) {
					logger.warn("sleep "+retryIntervalMills[i]+" mills,retry times:"+i+"occer exception when invode remote webservice call:"+getServiceUrl()+"/"+originalInvocation.getMethod().getName(),e);
					Thread.sleep(retryIntervalMills[i]);
					lastException = e;
				}
			}
			throw lastException;
		}
	}
	
	/**
	 * Execute the given remote invocation via the HttpInvokerRequestExecutor.
	 * <p>Can be overridden in subclasses to pass a different configuration object
	 * to the executor. Alternatively, add further configuration properties in a
	 * subclass of this accessor: By default, the accessor passed itself as
	 * configuration object to the executor.
	 * @param invocation the RemoteInvocation to execute
	 * @return the RemoteInvocationResult object
	 * @throws IOException if thrown by I/O operations
	 * @throws ClassNotFoundException if thrown during deserialization
	 * @throws Exception in case of general errors
	 * @see #getHttpInvokerRequestExecutor
	 */
	protected HttpResponse executeRequest(RPCRequest invocation) throws Exception {
		return getHttpInvokerRequestExecutor().executeRequest(getServiceUrl(), invocation);
	}

	/**
	 * Convert the given HTTP invoker access exception to an appropriate
	 * Spring RemoteAccessException.
	 * @param ex the exception to convert
	 * @return the RemoteAccessException to throw
	 */
	protected WebServiceException convertHttpInvokerAccessException(Throwable ex,MethodInvocation methodInvocation) {
		if (ex instanceof ConnectException) {
			throw new WebServiceException(
					ex.getClass().getSimpleName(),"Could not connect to HTTP invoker remote service at [" + getServiceUrl() + "]",getServiceUrl(),methodInvocation.getMethod().getName(), ex);
		}
		else if (ex instanceof ClassNotFoundException || ex instanceof NoClassDefFoundError ||
				ex instanceof InvalidClassException) {
			throw new WebServiceException(
					ex.getClass().getSimpleName(),"Could not deserialize result from HTTP invoker remote service [" + getServiceUrl() + "]",getServiceUrl(),methodInvocation.getMethod().getName(), ex);
		}
		else {
			throw new WebServiceException(
					ex.getClass().getSimpleName(),"Could not access HTTP invoker remote service at [" + getServiceUrl() + "]",getServiceUrl(),methodInvocation.getMethod().getName(), ex);
		}
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}
	
}
