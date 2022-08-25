package com.github.rapid.common.redis;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class SimpleRedisServer {
	public static int DEFAULT_PORT = 6000;
	
	private int port = DEFAULT_PORT;
	
	public void start() throws IOException {
		ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("启动服务器....");
        while(true) {
	        final Socket socket = serverSocket.accept();
	        new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println("receive socket:"+socket);
						Reader input = new InputStreamReader(socket.getInputStream());
				        IOUtils.copy(input, System.out);
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
	        }).start();
	        
        }
	}
	
	@Test
	public void startSimpleRedisServer() throws IOException {
		new SimpleRedisServer().start();
	}
}
