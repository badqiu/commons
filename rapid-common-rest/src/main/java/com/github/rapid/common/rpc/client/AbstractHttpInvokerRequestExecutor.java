package com.github.rapid.common.rpc.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.util.Assert;

import com.github.rapid.common.rpc.RPCRequest;
import com.github.rapid.common.rpc.SerDe;
import com.github.rapid.common.rpc.SerDeMapping;
import com.github.rapid.common.rpc.server.MethodInvoker;
import com.github.rapid.common.rpc.util.URLParamUtil;

public abstract class AbstractHttpInvokerRequestExecutor
		implements HttpInvokerRequestExecutor, BeanClassLoaderAware {

	/**
	 * Default content type: "application/x-java-serialized-object"
	 */
	public static final String CONTENT_TYPE_WWW_FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String CONTENT_TYPE_JAVA = "application/java";

	/**
	 * Default timeout value if no HttpClient is explicitly provided.
	 */
	protected static final int DEFAULT_READ_TIMEOUT_MILLISECONDS = (10 * 1000);
	protected static final int DEFAULT_CONNECTION_TIMEOUT_MILLISECONDS = (3 * 1000);
	
	protected static final String HTTP_METHOD_POST = "POST";

	protected static final String HTTP_HEADER_ACCEPT_LANGUAGE = "Accept-Language";

	protected static final String HTTP_HEADER_ACCEPT_ENCODING = "Accept-Encoding";

	protected static final String HTTP_HEADER_CONTENT_ENCODING = "Content-Encoding";

	protected static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";

	protected static final String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";

	protected static final String ENCODING_GZIP = "gzip";

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private String contentType = CONTENT_TYPE_JSON;

	private boolean acceptGzipEncoding = true;

	private ClassLoader beanClassLoader;
	
	protected int readTimeout = DEFAULT_READ_TIMEOUT_MILLISECONDS;
	protected int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT_MILLISECONDS;


	/**
	 * Specify the content type to use for sending HTTP invoker requests.
	 * <p>Default is "application/x-java-serialized-object".
	 */
	public void setContentType(String contentType) {
		Assert.notNull(contentType, "'contentType' must not be null");
		this.contentType = contentType;
	}

	public int getReadTimeout() {
		return readTimeout;
	}

	public void setReadTimeout(int readTimeout) {
		if (readTimeout < 0) {
			throw new IllegalArgumentException("timeout must be a non-negative value");
		}
		this.readTimeout = readTimeout;
	}

	public int getConnectionTimeout() {
		return connectionTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		if (connectionTimeout < 0) {
			throw new IllegalArgumentException("timeout must be a non-negative value");
		}
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * Return the content type to use for sending HTTP invoker requests.
	 */
	public String getContentType() {
		return this.contentType;
	}

	/**
	 * Set whether to accept GZIP encoding, that is, whether to
	 * send the HTTP "Accept-Encoding" header with "gzip" as value.
	 * <p>Default is "true". Turn this flag off if you do not want
	 * GZIP response compression even if enabled on the HTTP server.
	 */
	public void setAcceptGzipEncoding(boolean acceptGzipEncoding) {
		this.acceptGzipEncoding = acceptGzipEncoding;
	}

	/**
	 * Return whether to accept GZIP encoding, that is, whether to
	 * send the HTTP "Accept-Encoding" header with "gzip" as value.
	 */
	public boolean isAcceptGzipEncoding() {
		return this.acceptGzipEncoding;
	}

	public void setBeanClassLoader(ClassLoader classLoader) {
		this.beanClassLoader = classLoader;
	}

	/**
	 * Return the bean ClassLoader that this executor is supposed to use.
	 */
	protected ClassLoader getBeanClassLoader() {
		return this.beanClassLoader;
	}

	public final HttpResponse executeRequest(
			String serviceUrl, RPCRequest invocation) throws Exception {

		
		String method = invocation.getMethod();
		SerDe serDe = SerDeMapping.DEFAULT_MAPPING.getSerDeByContentType(getContentType());
		if(serDe == null) {
			throw new RuntimeException("not found http request serialize SerDe by contentType:"+getContentType());
		}
		invocation.setFormat(SerDeMapping.extractFormat(serDe.getContentType()));
		
		String urlParamsString = URLParamUtil.serial2UrlParams(buildUrlParams(invocation));
		String url = serviceUrl +"/"+method + "?" + urlParamsString;
		
		ByteArrayOutputStream output = new ByteArrayOutputStream(300);
		serDe.serialize((Object)invocation.getArguments(), output, null);
		byte[] parameters = output.toByteArray();
		if (logger.isDebugEnabled()) {
			logger.debug("Sending HTTP invoker request for service at [" + url +
					"], with parameters: " + parameters);
		}
		return doExecuteRequest(url,parameters);
	}

	private Map buildUrlParams(RPCRequest invocation) {
		Map<String,String> localParamsMap = new HashMap<String,String>();
		localParamsMap.put(MethodInvoker.KEY_PROTOCOL, invocation.getFormat());
		localParamsMap.put(MethodInvoker.KEY_FORMAT, invocation.getFormat());
		
		Map<String, String> globalParams = RPCClientContext.getGlobalParams();
		if(globalParams != null) {
			localParamsMap.putAll(globalParams);
		}
		Map<String, String> contextParams = RPCClientContext.getParams();
		if(contextParams != null) {
			localParamsMap.putAll(contextParams);
		}
		return localParamsMap;
	}

	/**
	 * Execute a request to send the given serialized remote invocation.
	 * <p>Implementations will usually call <code>readRemoteInvocationResult</code>
	 * to deserialize a returned RemoteInvocationResult object.
	 * @param config the HTTP invoker configuration that specifies the
	 * target service
	 * @param baos the ByteArrayOutputStream that contains the serialized
	 * RemoteInvocation object
	 * @return the RemoteInvocationResult object
	 * @throws IOException if thrown by I/O operations
	 * @throws ClassNotFoundException if thrown during deserialization
	 * @throws Exception in case of general errors
	 * @see #readRemoteInvocationResult(java.io.InputStream, String)
	 */
	protected abstract HttpResponse doExecuteRequest(String url,byte[] parameters)
			throws Exception;


}