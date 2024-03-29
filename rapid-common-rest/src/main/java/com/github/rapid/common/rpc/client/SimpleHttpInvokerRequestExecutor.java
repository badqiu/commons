package com.github.rapid.common.rpc.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;

/**
 * HttpInvokerRequestExecutor implementation that uses standard J2SE facilities
 * to execute POST requests, without support for HTTP authentication or
 * advanced configuration options.
 *
 * <p>Designed for easy subclassing, customizing specific template methods.
 * However, consider CommonsHttpInvokerRequestExecutor for more sophisticated
 * needs: The J2SE HttpURLConnection is rather limited in its capabilities.
 *
 * @author Juergen Hoeller
 * @since 1.1
 * @see CommonsHttpInvokerRequestExecutor
 * @see java.net.HttpURLConnection
 */
public class SimpleHttpInvokerRequestExecutor extends AbstractHttpInvokerRequestExecutor {

	/**
	 * Execute the given request through a standard J2SE HttpURLConnection.
	 * <p>This method implements the basic processing workflow:
	 * The actual work happens in this class's template methods.
	 * @see #openConnection
	 * @see #prepareConnection
	 * @see #writeRequestBody
	 * @see #validateResponse
	 * @see #readResponseBody
	 */
	protected HttpResponse doExecuteRequest(String url,byte[] parameters,Map<String,String> headers)
			throws IOException, ClassNotFoundException {

		HttpURLConnection con = openConnection(url);
		prepareConnection(con,parameters.length,headers);
		
		writeRequestBody(con, parameters);
		validateResponse(con);
		InputStream responseBody = readResponseBody(con);
		
		HttpResponse r = new HttpResponse();
		r.setBody(responseBody);
		r.setHeaders(toResponseHeadersMap(con.getHeaderFields()));
		return r;
	}

	private Map<String, String> toResponseHeadersMap(Map<String, List<String>> headerFields) {
		Map m = new HashMap(headerFields.size() * 2);
		for(Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
			List<String> value = entry.getValue();
			if(value != null && !value.isEmpty()) {
				m.put(entry.getKey(), value.get(0));
			}
		}
		return m;
	}
	
	private Map<String, String> toHttpHeadersMap(Map<String, String> headerFields) {
		if(headerFields == null) return Collections.emptyMap();
		return headerFields;
//		Map m = new HashMap(headerFields.size() * 2);
//		for(Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
//			List<String> value = entry.getValue();
//			if(value != null && !value.isEmpty()) {
//				m.put(entry.getKey(), value.get(0));
//			}
//		}
//		return m;
	}

	private void writeRequestBody(
			HttpURLConnection con, byte[] parameters) throws IOException {
		OutputStream outputStream = con.getOutputStream();
		PrintStream print = new PrintStream(outputStream);
		print.write(parameters);
		try {
			print.flush();
		}finally {
			outputStream.close();
		}
	}

	/**
	 * Open an HttpURLConnection for the given remote invocation request.
	 * @param config the HTTP invoker configuration that specifies the
	 * target service
	 * @return the HttpURLConnection for the given request
	 * @throws IOException if thrown by I/O methods
	 * @see java.net.URL#openConnection()
	 */
	protected HttpURLConnection openConnection(String url) throws IOException {
		URLConnection con = new URL(url).openConnection();
		if (!(con instanceof HttpURLConnection)) {
			throw new IOException("Service URL [" + url + "] is not an HTTP URL");
		}
		return (HttpURLConnection) con;
	}

	/**
	 * Prepare the given HTTP connection.
	 * <p>The default implementation specifies POST as method,
	 * "application/x-java-serialized-object" as "Content-Type" header,
	 * and the given content length as "Content-Length" header.
	 * @param con the HTTP connection to prepare
	 * @param contentLength the length of the content to send
	 * @throws IOException if thrown by HttpURLConnection methods
	 * @see java.net.HttpURLConnection#setRequestMethod
	 * @see java.net.HttpURLConnection#setRequestProperty
	 */
	protected void prepareConnection(HttpURLConnection con, int contentLength,Map<String,String> headers) throws IOException {
		con.setConnectTimeout(getConnectionTimeout());
		con.setReadTimeout(getReadTimeout());
		
		con.setDoOutput(true);
		con.setRequestMethod(HTTP_METHOD_POST);
		con.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, getContentType());
		con.setRequestProperty(HTTP_HEADER_CONTENT_LENGTH, Integer.toString(contentLength));
		
		if(headers != null) {
			for(Map.Entry<String,String> entry : headers.entrySet()) {
				con.setRequestProperty(entry.getKey(), entry.getValue());
			}
		}
		
		LocaleContext locale = LocaleContextHolder.getLocaleContext();
		if (locale != null) {
			con.setRequestProperty(HTTP_HEADER_ACCEPT_LANGUAGE, StringUtils.toLanguageTag(locale.getLocale()));
		}
		if (isAcceptGzipEncoding()) {
			con.setRequestProperty(HTTP_HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
		}
	}

	/**
	 * Validate the given response as contained in the HttpURLConnection object,
	 * throwing an exception if it does not correspond to a successful HTTP response.
	 * <p>Default implementation rejects any HTTP status code beyond 2xx, to avoid
	 * parsing the response body and trying to deserialize from a corrupted stream.
	 * @param config the HTTP invoker configuration that specifies the target service
	 * @param con the HttpURLConnection to validate
	 * @throws IOException if validation failed
	 * @see java.net.HttpURLConnection#getResponseCode()
	 */
	protected void validateResponse(HttpURLConnection con)
			throws IOException {

		if (con.getResponseCode() >= 300) {
			throw new IOException(
					"Did not receive successful HTTP response: status code = " + con.getResponseCode() +
					", status message = [" + con.getResponseMessage() + "] on url:"+con.getURL().getPath());
		}
	}

	/**
	 * Extract the response body from the given executed remote invocation
	 * request.
	 * <p>The default implementation simply reads the serialized invocation
	 * from the HttpURLConnection's InputStream. If the response is recognized
	 * as GZIP response, the InputStream will get wrapped in a GZIPInputStream.
	 * @param config the HTTP invoker configuration that specifies the target service
	 * @param con the HttpURLConnection to read the response body from
	 * @return an InputStream for the response body
	 * @throws IOException if thrown by I/O methods
	 * @see #isGzipResponse
	 * @see java.util.zip.GZIPInputStream
	 * @see java.net.HttpURLConnection#getInputStream()
	 * @see java.net.HttpURLConnection#getHeaderField(int)
	 * @see java.net.HttpURLConnection#getHeaderFieldKey(int)
	 */
	protected InputStream readResponseBody(HttpURLConnection con)
			throws IOException {

		if (isGzipResponse(con)) {
			// GZIP response found - need to unzip.
			return new GZIPInputStream(con.getInputStream());
		}
		else {
			// Plain response found.
			return con.getInputStream();
		}
	}

	/**
	 * Determine whether the given response is a GZIP response.
	 * <p>Default implementation checks whether the HTTP "Content-Encoding"
	 * header contains "gzip" (in any casing).
	 * @param con the HttpURLConnection to check
	 */
	protected boolean isGzipResponse(HttpURLConnection con) {
		String encodingHeader = con.getHeaderField(HTTP_HEADER_CONTENT_ENCODING);
		return (encodingHeader != null && encodingHeader.toLowerCase().indexOf(ENCODING_GZIP) != -1);
	}

	@Override
	public void afterPropertiesSet()  {
		
	}


}