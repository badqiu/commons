package com.github.rapid.common.util;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class PingUtil {
	
	private static Logger logger = LoggerFactory.getLogger(PingUtil.class);

	public static int defaultTimeout = 1000 * 10;
	
	public static boolean socketPing(String server) {
		Assert.hasText(server,"server must be not blank, example 192.168.0.1:8080");
		String[] array = server.split(":");
		return socketPing(array[0],Integer.parseInt(array[1]),defaultTimeout);
	}
	
	public static boolean socketPing(String host, int port) {
		return socketPing(host,port,defaultTimeout);
	}
	
	public static boolean socketPing(String host, int port,int timeout) {
		Assert.hasText(host,"host must be not blank");
		Assert.isTrue(port >= 0,"port >= 0 must be true");
		
		Socket socket = null;
		try {
			socket = new Socket(StringUtils.trim(host), port);
			socket.setSoTimeout(timeout);
			return socket.isConnected();
		} catch (IOException e) {
			logger.warn("socketPing error host:"+host+" port:"+port+" timeout:"+timeout+" errorMsg:"+e,e);
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
	
	public static boolean urlPing(String url) {
		try {
			URL urlObj = new URL(url);
			URLConnection conn = urlObj.openConnection();
			conn.setReadTimeout(defaultTimeout);
			conn.setConnectTimeout(defaultTimeout);
			conn.connect();
			return true;
		}catch(Exception e) {
			throw new RuntimeException("connect error on url:"+url,e);
		}
	}

}
