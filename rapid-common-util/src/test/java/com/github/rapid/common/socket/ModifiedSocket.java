package com.github.rapid.common.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketImpl;

/**
 * 修改后的Socket类，添加默认超时设置
 * 这个类在自定义类加载器中会替换java.net.Socket
 */
public class ModifiedSocket extends Socket {
    
    private static final int DEFAULT_CONNECT_TIMEOUT = 5000;
    private static final int DEFAULT_SO_TIMEOUT = 10000;
    
    public ModifiedSocket() {
        super();
        setTimeoutOptions();
    }
    
    public ModifiedSocket(String host, int port) throws IOException {
        super(host, port);
        setTimeoutOptions();
    }
    
    public ModifiedSocket(InetAddress address, int port) throws IOException {
        super(address, port);
        setTimeoutOptions();
    }
    
    public ModifiedSocket(String host, int port, InetAddress localAddr, int localPort) throws IOException {
        super(host, port, localAddr, localPort);
        setTimeoutOptions();
    }
    
    public ModifiedSocket(InetAddress address, int port, InetAddress localAddr, int localPort) throws IOException {
        super(address, port, localAddr, localPort);
        setTimeoutOptions();
    }
    
    public ModifiedSocket(SocketImpl impl) throws SocketException {
        super(impl);
        setTimeoutOptions();
    }
    
    /**
     * 重写connect方法，设置默认连接超时
     */
    @Override
    public void connect(SocketAddress endpoint) throws IOException {
        // 如果endpoint是InetSocketAddress，使用带超时的连接
        if (endpoint instanceof java.net.InetSocketAddress) {
            java.net.InetSocketAddress inetEndpoint = (java.net.InetSocketAddress) endpoint;
            this.connect(inetEndpoint, DEFAULT_CONNECT_TIMEOUT);
        } else {
            super.connect(endpoint);
        }
    }
    
    @Override
    public void connect(SocketAddress endpoint, int timeout) throws IOException {
        // 如果超时为0，使用默认超时
        int actualTimeout = timeout > 0 ? timeout : DEFAULT_CONNECT_TIMEOUT;
        super.connect(endpoint, actualTimeout);
    }
    
    /**
     * 设置Socket超时选项
     */
    private void setTimeoutOptions() {
        try {
        	System.out.println("setTimeoutOptions timeout:"+DEFAULT_SO_TIMEOUT);
            this.setSoTimeout(DEFAULT_SO_TIMEOUT);
            this.setTcpNoDelay(true);
            this.setKeepAlive(true);
            this.setReuseAddress(true);
        } catch (SocketException e) {
            System.err.println("Failed to set socket timeout options: " + e.getMessage());
        }
    }
    
    /**
     * 获取当前Socket的超时配置信息
     */
    public String getTimeoutInfo() {
        try {
            return String.format("Socket[soTimeout=%dms, connectTimeout=%dms, keepAlive=%s, tcpNoDelay=%s]", 
                this.getSoTimeout(), DEFAULT_CONNECT_TIMEOUT, this.getKeepAlive(), this.getTcpNoDelay());
        } catch (SocketException e) {
            return "Socket[Unable to get timeout info]";
        }
    }
    
    // 添加一些工具方法
    public boolean isTimeoutConfigured() {
        try {
            return this.getSoTimeout() == DEFAULT_SO_TIMEOUT;
        } catch (SocketException e) {
            return false;
        }
    }
    
    /**
     * 创建测试连接（不实际建立连接）
     */
    public static ModifiedSocket createTestSocket() {
        return new ModifiedSocket();
    }
}