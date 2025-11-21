package com.github.rapid.common.socket;

import java.lang.reflect.Field;
import java.net.Socket;
import java.net.SocketAddress;

import javax.net.SocketFactory;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.reflect.FieldUtils;

public class NetworkTimeoutConfig {
    
	  /**
     * 通过反射设置全局默认 SocketFactory
     */
    public static void setDefaultSocketFactory(SocketFactory factory) {
        try {
            // 获取 SocketFactory 类的 theFactory 字段
            Field theFactoryField = SocketFactory.class.getDeclaredField("theFactory");
            theFactoryField.setAccessible(true);
            
            // 设置字段值为我们的自定义工厂
            theFactoryField.set(null, factory);
            
            System.out.println("全局 SocketFactory 设置成功");
        } catch (NoSuchFieldException e) {
            System.err.println("未找到 theFactory 字段，可能是Java版本不兼容: " + e.getMessage());
        } catch (IllegalAccessException e) {
            System.err.println("无法访问 theFactory 字段: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("设置全局 SocketFactory 时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 配置全局网络超时设置
     */
    public static void configureGlobalTimeouts() {
        // 系统属性设置
        System.setProperty("sun.net.client.defaultConnectTimeout", "5000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        System.setProperty("sun.net.client.defaultSocketTimeout", "15000");
        System.setProperty("http.keepAlive.timeout", "5000");
        
        // URLConnection默认设置
//        URLConnection.setDefaultConnectTimeout(5000);
//        URLConnection.setDefaultReadTimeout(10000);
        
        // 设置自定义Socket工厂
//        SocketFactory.setDefault(new CustomSocketFactory(5000, 10000));
        setDefaultSocketFactory(new CustomSocketFactory(5000, 5000));
//        String fieldName = "theFactory";
//		try {
//			setFieldValue(fieldName);
//		} catch (IllegalAccessException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        
        // 设置HTTPS
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
            HttpsURLConnection.setDefaultSSLSocketFactory(
                new CustomSSLSocketFactory(sslContext.getSocketFactory(), 5000, 10000)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // 禁用DNS缓存
        java.security.Security.setProperty("networkaddress.cache.ttl", "0");
        java.security.Security.setProperty("networkaddress.cache.negative.ttl", "0");
    }

	private static void setFieldValue(String fieldName) throws IllegalAccessException {
		Field field = FieldUtils.getDeclaredField(SocketFactory.class, fieldName);
        field.setAccessible(true);
        field.set(SocketFactory.class, new CustomSocketFactory(5000, 10000));
	}
    
    /**
     * 针对特定协议的超时设置
     */
    public static void configureProtocolSpecificTimeouts() {
        // HTTP协议相关设置
        System.setProperty("http.connection.timeout", "5000");
        System.setProperty("http.socket.timeout", "10000");
        
        // FTP协议相关设置
        System.setProperty("ftp.client.connectTimeout", "5000");
        System.setProperty("ftp.client.dataTimeout", "10000");
        
        // 连接池设置
        System.setProperty("http.keepAlive", "true");
        System.setProperty("http.maxConnections", "100");
    }
    
    public static void main(String[] args) {
    	NetworkTimeoutConfig.configureGlobalTimeouts();
    	NetworkTimeoutConfig.configureProtocolSpecificTimeouts();
    	
    }
}

// 在应用启动时调用
