package com.github.rapid.common.socket;

import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

// 在应用启动时设置全局Socket工厂
public class GlobalSocketConfig {
    static {
        // 设置自定义Socket工厂为默认工厂
//        SocketFactory.setDefault(new CustomSocketFactory(5000, 10000));
        
        // 设置HTTPS的Socket工厂
        HttpsURLConnection.setDefaultSSLSocketFactory(createSSLSocketFactory());
    }
    
    private static SSLSocketFactory createSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            SSLSocketFactory factory = sslContext.getSocketFactory();
            return new CustomSSLSocketFactory(factory, 5000, 10000);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSL socket factory", e);
        }
    }
}