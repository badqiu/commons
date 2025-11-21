package com.github.rapid.common.socket;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.SocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomSocketFactory extends SocketFactory {
	protected static final Logger log = LoggerFactory.getLogger(CustomSocketFactory.class);
	
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
    private static final int DEFAULT_SO_TIMEOUT = 10000;
    
    private final int connectTimeout;
    private final int soTimeout;
    
    public CustomSocketFactory() {
        this(DEFAULT_CONNECT_TIMEOUT, DEFAULT_SO_TIMEOUT);
    }
    
    public CustomSocketFactory(int connectTimeout, int soTimeout) {
        this.connectTimeout = connectTimeout;
        this.soTimeout = soTimeout;
    }
    
    @Override
    public Socket createSocket() throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(soTimeout);
        log.info("createSocket,soTimeout:"+soTimeout);
        return socket;
    }
    
    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        Socket socket = new Socket();
        socket.setSoTimeout(soTimeout);
        socket.connect(new java.net.InetSocketAddress(host, port), connectTimeout);
        log.info("createSocket,soTimeout:"+soTimeout);
        return socket;
    }
    
    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(soTimeout);
        socket.connect(new java.net.InetSocketAddress(host, port), connectTimeout);
        log.info("createSocket,soTimeout:"+soTimeout);
        return socket;
    }
    
    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) 
        throws IOException, UnknownHostException {
        Socket socket = new Socket();
        socket.setSoTimeout(soTimeout);
        socket.bind(new java.net.InetSocketAddress(localHost, localPort));
        socket.connect(new java.net.InetSocketAddress(host, port), connectTimeout);
        log.info("createSocket,soTimeout:"+soTimeout);
        return socket;
    }
    
    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) 
        throws IOException {
        Socket socket = new Socket();
        socket.setSoTimeout(soTimeout);
        socket.bind(new java.net.InetSocketAddress(localAddress, localPort));
        socket.connect(new java.net.InetSocketAddress(address, port), connectTimeout);
        log.info("createSocket,soTimeout:"+soTimeout);
        return socket;
    }
}