package com.github.rapid.common.util;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
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

}
