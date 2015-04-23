package com.github.rapid.common.rpc.client;

import java.io.IOException;
import java.io.InputStream;

import com.github.rapid.common.rpc.RPCRequest;

/**
 * Strategy interface for actual execution of an HTTP invoker request.
 * Used by HttpInvokerClientInterceptor and its subclass
 * HttpInvokerProxyFactoryBean.
 *
 * <p>Two implementations are provided out of the box:
 * <ul>
 * <li><b>SimpleHttpInvokerRequestExecutor:</b>
 * Uses J2SE facilities to execute POST requests, without support
 * for HTTP authentication or advanced configuration options.
 * <li><b>CommonsHttpInvokerRequestExecutor:</b>
 * Uses Jakarta's Commons HttpClient to execute POST requests,
 * allowing to use a preconfigured HttpClient instance
 * (potentially with authentication, HTTP connection pooling, etc).
 * </ul>
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see HttpInvokerClientInterceptor#setHttpInvokerRequestExecutor
 */
public interface HttpInvokerRequestExecutor {

	/**
	 * Execute a request to send the given remote invocation.
	 * @param config the HTTP invoker configuration that specifies the
	 * target service
	 * @param invocation the RemoteInvocation to execute
	 * @return the RemoteInvocationResult object
	 * @throws IOException if thrown by I/O operations
	 * @throws ClassNotFoundException if thrown during deserialization
	 * @throws Exception in case of general errors
	 */
	HttpResponse executeRequest(String serviceUrl, RPCRequest invocation)
			throws Exception;

}