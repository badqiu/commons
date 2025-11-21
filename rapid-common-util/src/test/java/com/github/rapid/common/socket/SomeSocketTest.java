package com.github.rapid.common.socket;

import java.net.Socket;

public class SomeSocketTest {

	static {
		Socket socket = new Socket();
		System.out.println("SomeSocketTest:"+socket.getClass().getName());
	}
	public  static void newSocket() {
//        socket.connect(new InetSocketAddress("www.163.com",1000));
	}
	
}
