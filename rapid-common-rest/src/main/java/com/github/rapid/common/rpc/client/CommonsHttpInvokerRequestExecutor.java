/*
 * Copyright 2002-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.rapid.common.rpc.client;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

import com.github.rapid.common.rpc.client.ssl.MySecureProtocolSocketFactory;

/**
 * {@link HttpInvokerRequestExecutor} implementation that uses
 * <a href="http://jakarta.apache.org/commons/httpclient">Jakarta Commons HttpClient</a>
 * to execute POST requests. Requires Commons HttpClient 3.0 or higher.
 *
 * <p>Allows to use a pre-configured {@link org.apache.commons.httpclient.HttpClient}
 * instance, potentially with authentication, HTTP connection pooling, etc.
 * Also designed for easy subclassing, providing specific template methods.
 *
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @since 1.1
 * @see SimpleHttpInvokerRequestExecutor
 */
public class CommonsHttpInvokerRequestExecutor extends AbstractHttpInvokerRequestExecutor implements InitializingBean {

	private HttpClient httpClient;


	/**
	 * Create a new CommonsHttpInvokerRequestExecutor with a default
	 * HttpClient that uses a default MultiThreadedHttpConnectionManager.
	 * Sets the socket read timeout to {@link #DEFAULT_READ_TIMEOUT_MILLISECONDS}.
	 * @see org.apache.commons.httpclient.HttpClient
	 * @see org.apache.commons.httpclient.MultiThreadedHttpConnectionManager
	 */
	public CommonsHttpInvokerRequestExecutor() {
		
	}

	public CommonsHttpInvokerRequestExecutor(boolean init) {
		if(init) {
			afterPropertiesSet();
		}
	}

	/**
	 * Create a new CommonsHttpInvokerRequestExecutor with the given
	 * HttpClient instance. The socket read timeout of the provided
	 * HttpClient will not be changed.
	 * @param httpClient the HttpClient instance to use for this request executor
	 */
	public CommonsHttpInvokerRequestExecutor(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * Set the HttpClient instance to use for this request executor.
	 */
	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * Return the HttpClient instance that this request executor uses.
	 */
	public HttpClient getHttpClient() {
		return this.httpClient;
	}


	/**
	 * Create a PostMethod for the given configuration.
	 * <p>The default implementation creates a standard PostMethod with
	 * "application/x-java-serialized-object" as "Content-Type" header.
	 * @param config the HTTP invoker configuration that specifies the
	 * target service
	 * @return the PostMethod instance
	 * @throws IOException if thrown by I/O methods
	 */
	protected PostMethod createPostMethod(String serviceUrl) throws IOException {
		PostMethod postMethod = new PostMethod(serviceUrl);
		LocaleContext locale = LocaleContextHolder.getLocaleContext();
		if (locale != null) {
			postMethod.addRequestHeader(HTTP_HEADER_ACCEPT_LANGUAGE, StringUtils.toLanguageTag(locale.getLocale()));
		}
		if (isAcceptGzipEncoding()) {
			postMethod.addRequestHeader(HTTP_HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
		}
		return postMethod;
	}

	/**
	 * Set the given serialized remote invocation as request body.
	 * <p>The default implementation simply sets the serialized invocation
	 * as the PostMethod's request body. This can be overridden, for example,
	 * to write a specific encoding and potentially set appropriate HTTP
	 * request headers.
	 * @param config the HTTP invoker configuration that specifies the target service
	 * @param postMethod the PostMethod to set the request body on
	 * @param baos the ByteArrayOutputStream that contains the serialized
	 * RemoteInvocation object
	 * @throws IOException if thrown by I/O methods
	 * @see org.apache.commons.httpclient.methods.PostMethod#setRequestBody(java.io.InputStream)
	 * @see org.apache.commons.httpclient.methods.PostMethod#setRequestEntity
	 * @see org.apache.commons.httpclient.methods.InputStreamRequestEntity
	 */
	protected void setRequestBody(PostMethod postMethod,byte[] parameters)
			throws IOException {
		postMethod.setRequestEntity(new ByteArrayRequestEntity(parameters, getContentType()));
	}

	/**
	 * Execute the given PostMethod instance.
	 * @param config the HTTP invoker configuration that specifies the target service
	 * @param httpClient the HttpClient to execute on
	 * @param postMethod the PostMethod to execute
	 * @throws IOException if thrown by I/O methods
	 * @see org.apache.commons.httpclient.HttpClient#executeMethod(org.apache.commons.httpclient.HttpMethod)
	 */
	protected void executePostMethod(HttpClient httpClient, PostMethod postMethod)
			throws IOException {

		httpClient.executeMethod(postMethod);
	}

	/**
	 * Validate the given response as contained in the PostMethod object,
	 * throwing an exception if it does not correspond to a successful HTTP response.
	 * <p>Default implementation rejects any HTTP status code beyond 2xx, to avoid
	 * parsing the response body and trying to deserialize from a corrupted stream.
	 * @param config the HTTP invoker configuration that specifies the target service
	 * @param postMethod the executed PostMethod to validate
	 * @throws IOException if validation failed
	 * @see org.apache.commons.httpclient.methods.PostMethod#getStatusCode()
	 * @see org.apache.commons.httpclient.HttpException
	 */
	protected void validateResponse(PostMethod postMethod)
			throws IOException {

		if (postMethod.getStatusCode() >= 300) {
			throw new HttpException(
					"Did not receive successful HTTP response: status code = " + postMethod.getStatusCode() +
					", status message = [" + postMethod.getStatusText() + "]");
		}
	}

	/**
	 * Extract the response body from the given executed remote invocation
	 * request.
	 * <p>The default implementation simply fetches the PostMethod's response
	 * body stream. If the response is recognized as GZIP response, the
	 * InputStream will get wrapped in a GZIPInputStream.
	 * @param config the HTTP invoker configuration that specifies the target service
	 * @param postMethod the PostMethod to read the response body from
	 * @return an InputStream for the response body
	 * @throws IOException if thrown by I/O methods
	 * @see #isGzipResponse
	 * @see java.util.zip.GZIPInputStream
	 * @see org.apache.commons.httpclient.methods.PostMethod#getResponseBodyAsStream()
	 * @see org.apache.commons.httpclient.methods.PostMethod#getResponseHeader(String)
	 */
	protected InputStream getResponseBody(PostMethod postMethod)
			throws IOException {
		if (isGzipResponse(postMethod)) {
			return new GZIPInputStream(postMethod.getResponseBodyAsStream());
		}
		else {
			return postMethod.getResponseBodyAsStream();
		}
	}

	/**
	 * Determine whether the given response indicates a GZIP response.
	 * <p>Default implementation checks whether the HTTP "Content-Encoding"
	 * header contains "gzip" (in any casing).
	 * @param postMethod the PostMethod to check
	 * @return whether the given response indicates a GZIP response
	 */
	protected boolean isGzipResponse(PostMethod postMethod) {
		Header encodingHeader = postMethod.getResponseHeader(HTTP_HEADER_CONTENT_ENCODING);
		return (encodingHeader != null && encodingHeader.getValue() != null &&
				encodingHeader.getValue().toLowerCase().indexOf(ENCODING_GZIP) != -1);
	}

	@Override
	protected HttpResponse doExecuteRequest(String url,byte[] parameters,Map<String,String> headers) throws Exception {
		PostMethod postMethod = createPostMethod(url);
		try {
			
			setRequestHeaders(postMethod,headers);
			setRequestBody(postMethod,parameters);
			executePostMethod(getHttpClient(), postMethod);
			
			
			validateResponse(postMethod);
			InputStream responseBody = getResponseBody(postMethod);
			ByteArrayInputStream responseBodyInput = new ByteArrayInputStream(IOUtils.toByteArray(responseBody));
			HttpResponse response = new HttpResponse();
			response.setBody(responseBodyInput);
			response.setHeaders(toHashMap(postMethod.getResponseHeaders()));
			return response;
		}
		finally {
			// Need to explicitly release because it might be pooled.
			postMethod.releaseConnection();
		}
	}

	private void setRequestHeaders(PostMethod postMethod, Map<String, String> headers) {
		if(headers == null) return;
		
		for(Map.Entry<String,String> entry : headers.entrySet()) {
			postMethod.setRequestHeader(entry.getKey(), entry.getValue());
		}
	}

	private Map<String, String> toHashMap(Header[] responseHeaders) {
		Map<String, String> r = new HashMap<String, String>(responseHeaders.length * 2);
		for(Header h : responseHeaders) {
			r.put(h.getName(), h.getValue());
		}
		return r;
	}

	@Override
	public void afterPropertiesSet()  {
		init();
	}

	private void init() {
		MultiThreadedHttpConnectionManager httpConnectionManager = new MultiThreadedHttpConnectionManager();
		HttpConnectionManagerParams params = new HttpConnectionManagerParams();
		params.setDefaultMaxConnectionsPerHost(300);
		params.setMaxTotalConnections(500);
		params.setConnectionTimeout(getConnectionTimeout());
		params.setSoTimeout(getReadTimeout());
		
		httpConnectionManager.setParams(params);
		
		this.httpClient = new HttpClient(httpConnectionManager);
		
		ProtocolSocketFactory fcty = new MySecureProtocolSocketFactory();
		Protocol.registerProtocol("https", new Protocol("https", fcty, 443));
	}

}
