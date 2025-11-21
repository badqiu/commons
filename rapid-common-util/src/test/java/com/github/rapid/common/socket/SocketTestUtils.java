package com.github.rapid.common.socket;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Socket测试工具类
 */
public class SocketTestUtils {
    
    /**
     * 使用反射创建Socket实例（模拟new Socket()）
     */
    public static Object createSocketReflectively(ClassLoader classLoader) throws Exception {
        Class<?> socketClass = classLoader.loadClass("java.net.Socket");
        Constructor<?> constructor = socketClass.getDeclaredConstructor();
        return constructor.newInstance();
    }
    
    /**
     * 检查Socket是否配置了超时
     */
    public static boolean isSocketTimeoutConfigured(Object socket) {
        try {
            Method getSoTimeoutMethod = socket.getClass().getMethod("getSoTimeout");
            int timeout = (Integer) getSoTimeoutMethod.invoke(socket);
            return timeout == 10000; // 检查是否是我们设置的10秒超时
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 获取Socket的超时信息
     */
    public static String getSocketTimeoutInfo(Object socket) {
        try {
            if (socket instanceof ModifiedSocket) {
                return ((ModifiedSocket) socket).getTimeoutInfo();
            }
            
            Method getSoTimeoutMethod = socket.getClass().getMethod("getSoTimeout");
            Method getKeepAliveMethod = socket.getClass().getMethod("getKeepAlive");
            Method getTcpNoDelayMethod = socket.getClass().getMethod("getTcpNoDelay");
            
            int soTimeout = (Integer) getSoTimeoutMethod.invoke(socket);
            boolean keepAlive = (Boolean) getKeepAliveMethod.invoke(socket);
            boolean tcpNoDelay = (Boolean) getTcpNoDelayMethod.invoke(socket);
            
            return String.format("Socket[soTimeout=%dms, keepAlive=%s, tcpNoDelay=%s]", 
                soTimeout, keepAlive, tcpNoDelay);
        } catch (Exception e) {
            return "Unable to get socket info: " + e.getMessage();
        }
    }
    
    /**
     * 使用指定的ClassLoader执行Socket创建测试
     */
    public static TestResult testSocketCreation(ClassLoader classLoader) {
        TestResult result = new TestResult();
        
        try {
            // 保存当前类加载器
            ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
            
            try {
                // 设置自定义类加载器
                Thread.currentThread().setContextClassLoader(classLoader);
                
                // 尝试创建Socket
                Object socket = createSocketReflectively(classLoader);
                result.success = true;
                result.socket = socket;
                result.timeoutConfigured = isSocketTimeoutConfigured(socket);
                result.info = getSocketTimeoutInfo(socket);
                result.socketClass = socket.getClass().getName();
                
            } finally {
                // 恢复原始类加载器
                Thread.currentThread().setContextClassLoader(originalClassLoader);
            }
            
        } catch (Exception e) {
            result.success = false;
            result.error = e;
            result.info = "Failed to create socket: " + e.getMessage();
        }
        
        return result;
    }
    
    /**
     * 测试结果类
     */
    public static class TestResult {
        public boolean success;
        public Object socket;
        public boolean timeoutConfigured;
        public String info;
        public String socketClass;
        public Exception error;
        
        @Override
        public String toString() {
            if (success) {
                return String.format("Success: %s, TimeoutConfigured: %s, Info: %s", 
                    socketClass, timeoutConfigured, info);
            } else {
                return String.format("Failed: %s", info);
            }
        }
    }
}