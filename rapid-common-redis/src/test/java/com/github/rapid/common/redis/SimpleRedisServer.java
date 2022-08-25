package com.github.rapid.common.redis;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;

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
						Writer output = new OutputStreamWriter(socket.getOutputStream());
						output.write("+OK\n");
						output.flush();
						
						OutputStreamWriter sysout = new OutputStreamWriter(System.out);
						copyLarge(input, sysout);
						
					}catch(Exception e) {
						e.printStackTrace();
					}
				}
	        }).start();
	        
        }
	}
	
    public static long copyLarge(Reader input, Writer output) throws IOException {
        char[] buffer = new char[1];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    
	@Test
	public void startSimpleRedisServer() throws IOException {
		new SimpleRedisServer().start();
	}
}
