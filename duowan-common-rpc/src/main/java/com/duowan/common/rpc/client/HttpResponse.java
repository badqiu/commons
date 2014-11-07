package com.duowan.common.rpc.client;

import java.io.InputStream;
import java.util.Map;

public class HttpResponse {

	private InputStream body;
	private Map<String,String> headers;
	
	public HttpResponse(){
	}
	
	public HttpResponse(InputStream body, Map<String, String> headers) {
		super();
		this.body = body;
		this.headers = headers;
	}
	
	public InputStream getBody() {
		return body;
	}
	public void setBody(InputStream body) {
		this.body = body;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	
	
}
