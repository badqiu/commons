package com.github.rapid.common.socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class CustomSSLSocketFactory extends SSLSocketFactory {
    private final SSLSocketFactory delegate;
    private final int connectTimeout;
    private final int soTimeout;
    
    public CustomSSLSocketFactory(SSLSocketFactory delegate, int connectTimeout, int soTimeout) {
        this.delegate = delegate;
        this.connectTimeout = connectTimeout;
        this.soTimeout = soTimeout;
    }
    
    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }
    
    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }
    
    @Override
    public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
        SSLSocket socket = (SSLSocket) delegate.createSocket(s, host, port, autoClose);
        socket.setSoTimeout(soTimeout);
        return socket;
    }
    
    @Override
    public Socket createSocket(String host, int port) throws IOException {
        Socket socket = delegate.createSocket();
        socket.setSoTimeout(soTimeout);
        socket.connect(new java.net.InetSocketAddress(host, port), connectTimeout);
        return socket;
    }
    
    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException {
        Socket socket = delegate.createSocket();
        socket.setSoTimeout(soTimeout);
        socket.bind(new java.net.InetSocketAddress(localHost, localPort));
        socket.connect(new java.net.InetSocketAddress(host, port), connectTimeout);
        return socket;
    }
    
    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        Socket socket = delegate.createSocket();
        socket.setSoTimeout(soTimeout);
        socket.connect(new java.net.InetSocketAddress(host, port), connectTimeout);
        return socket;
    }
    
    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        Socket socket = delegate.createSocket();
        socket.setSoTimeout(soTimeout);
        socket.bind(new java.net.InetSocketAddress(localAddress, localPort));
        socket.connect(new java.net.InetSocketAddress(address, port), connectTimeout);
        return socket;
    }
}