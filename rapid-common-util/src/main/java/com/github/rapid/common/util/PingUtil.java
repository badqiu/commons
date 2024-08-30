package com.github.rapid.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class PingUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PingUtil.class);

	public static int defaultTimeoutMills = 1000 * 10;
	
	public static boolean socketPing(String server) {
		Assert.hasText(server,"server must be not blank, example 192.168.0.1:8080");
		String[] array = server.split(":");
		String host = array[0];
		int port = Integer.parseInt(array[1]);
		return socketPing(host,port,defaultTimeoutMills);
	}
	
	public static boolean socketPing(String host, int port) {
		return socketPing(host,port,defaultTimeoutMills);
	}
	
	public static boolean socketPing(String host, int port,int timeoutMills) {
		Assert.hasText(host,"host must be not blank");
		Assert.isTrue(port >= 0,"port >= 0 must be true");
		
		Socket socket = null;
		try {
			socket = new Socket();
			socket.setSoTimeout(timeoutMills);
			socket.connect(new InetSocketAddress(host,port), timeoutMills);
//			socket = new Socket(StringUtils.trim(host), port);
			
			return socket.isConnected();
		} catch (IOException e) {
			logger.warn("socketPing error host:"+host+" port:"+port+" timeoutMills:"+timeoutMills+" errorMsg:"+e,e);
			return false;
		} finally {
			close(socket);
		}
	}

	public static void close(Socket socket) {
		if(socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				logger.warn("close socket error",e);
			}
		}
	}
	
	public static boolean uriPing(String uri) {
		Assert.hasText(uri,"uri must be not blank");
		
		try {
			URI u = new URI(uri);
			return socketPing(u.getHost(),u.getPort());
		} catch (URISyntaxException e) {
			throw new RuntimeException("error uri:"+uri,e);
		}
	}
	
	public static String urlPing(String url) {
		return urlPing(url,defaultTimeoutMills);
	}
	
	public static String urlPing(String url,int timeoutMills) {
		Assert.hasText(url,"url must be not blank");
		
		InputStream input = null;
		try {
			URL urlObj = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setReadTimeout(timeoutMills);
            conn.setConnectTimeout(timeoutMills);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            // 检查响应代码
            int responseCode = conn.getResponseCode();
            input = conn.getInputStream();
            String response = IOUtils.toString(input, "UTF-8");
            
            boolean isSuccess = responseCode >= 200 && responseCode < 300;
			if (isSuccess) {
                return response;
            } else {
                throw new RuntimeException("error on url:"+ url + " response:" + response);
            }
		}catch(Exception e) {
			throw new RuntimeException("connect error on url:"+url,e);
		}
	}

}
