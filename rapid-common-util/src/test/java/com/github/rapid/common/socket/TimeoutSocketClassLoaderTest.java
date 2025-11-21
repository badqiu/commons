package com.github.rapid.common.socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.github.rapid.common.util.ThreadUtil;

/**
 * TimeoutSocketClassLoader 单元测试
 */
public class TimeoutSocketClassLoaderTest {
    
    private TimeoutSocketClassLoader classLoader;
    private ClassLoader originalClassLoader;
    
    @Before
    public void setUp() {
        // 保存原始类加载器
        originalClassLoader = Thread.currentThread().getContextClassLoader();
        
        // 创建自定义类加载器
        URL[] urls = ((URLClassLoader) getClass().getClassLoader()).getURLs();
        classLoader = new TimeoutSocketClassLoader(urls, getClass().getClassLoader());
    }
    
    @After
    public void tearDown() {
        // 恢复原始类加载器
        Thread.currentThread().setContextClassLoader(originalClassLoader);
    }
    
    @Test
    public void testClassLoaderCreation() {
        assertNotNull("ClassLoader should be created", classLoader);
        assertTrue("ClassLoader should be working", classLoader.isWorking());
    }
    
    @Test
    public void testSocketClassReplacement() throws Exception {
        // 设置自定义类加载器
        Thread.currentThread().setContextClassLoader(classLoader);
        
        // 加载Socket类
        Class<?> socketClass = classLoader.loadClass("java.net.Socket");
        
        // 验证加载的是我们的自定义类
        assertEquals("Socket class should be replaced", 
            "com.github.rapid.common.socket.ModifiedSocket", socketClass.getName());
        System.out.println(socketClass.getSimpleName());
        
        // 验证类加载器
//        assertEquals("Class should be loaded by our class loader", 
//            classLoader, socketClass.getClassLoader());
        
//        Socket socket = new Socket();
//        System.out.println(socket.getClass().getSimpleName());
//        socket.connect(new InetSocketAddress("www.163.com",1000));
        SomeSocketTest.newSocket();
//        Class<?> socketClass = classLoader.loadClass("java.net.Socket");
        ThreadUtil.sleep(1000);
    }
    
    @Test
    public void testSocketCreationWithTimeout() throws Exception {
        SocketTestUtils.TestResult result = SocketTestUtils.testSocketCreation(classLoader);
        
        assertTrue("Socket creation should succeed: " + result.info, result.success);
        assertTrue("Socket should have timeout configured: " + result.info, result.timeoutConfigured);
        assertEquals("Should load our ModifiedSocket class", 
            "com.example.ModifiedSocket", result.socketClass);
        
        System.out.println("Socket creation test: " + result.info);
    }
    
    @Test
    public void testModifiedSocketFunctionality() throws Exception {
        // 直接测试我们的ModifiedSocket类
        ModifiedSocket socket = ModifiedSocket.createTestSocket();
        
        assertTrue("ModifiedSocket should have timeout configured", socket.isTimeoutConfigured());
        
        String timeoutInfo = socket.getTimeoutInfo();
        assertTrue("Timeout info should contain configuration details", 
            timeoutInfo.contains("soTimeout=10000ms"));
        
        System.out.println("ModifiedSocket test: " + timeoutInfo);
    }
    
    @Test
    public void testMultipleSocketCreations() throws Exception {
        Thread.currentThread().setContextClassLoader(classLoader);
        
        // 创建多个Socket实例
        for (int i = 0; i < 3; i++) {
            SocketTestUtils.TestResult result = SocketTestUtils.testSocketCreation(classLoader);
            
            assertTrue("Socket creation " + i + " should succeed", result.success);
            assertTrue("Socket " + i + " should have timeout configured", result.timeoutConfigured);
        }
    }
    
    @Test
    public void testClassLoaderIsolation() throws Exception {
        // 使用默认类加载器创建Socket
        SocketTestUtils.TestResult defaultResult = SocketTestUtils.testSocketCreation(
            getClass().getClassLoader());
        
        // 使用自定义类加载器创建Socket
        SocketTestUtils.TestResult customResult = SocketTestUtils.testSocketCreation(classLoader);
        
        // 验证它们加载了不同的类
        assertEquals("Default classloader should load original Socket", 
            "java.net.Socket", defaultResult.socketClass);
        assertEquals("Custom classloader should load ModifiedSocket", 
            "com.example.ModifiedSocket", customResult.socketClass);
        
        // 验证超时配置
        assertFalse("Original Socket should not have our timeout configured", 
            defaultResult.timeoutConfigured);
        assertTrue("ModifiedSocket should have timeout configured", 
            customResult.timeoutConfigured);
        
        System.out.println("Default loader: " + defaultResult.info);
        System.out.println("Custom loader: " + customResult.info);
    }
    
    @Test
    public void testThreadContextClassLoader() throws Exception {
        // 测试在线程中设置类加载器
        Thread testThread = classLoader.createThreadWithThisClassLoader(() -> {
            try {
                SocketTestUtils.TestResult result = SocketTestUtils.testSocketCreation(
                    Thread.currentThread().getContextClassLoader());
                
                assertTrue("Socket creation in thread should succeed", result.success);
                assertTrue("Socket in thread should have timeout configured", result.timeoutConfigured);
                
                System.out.println("Thread test: " + result.info);
                
            } catch (Exception e) {
                fail("Thread test failed: " + e.getMessage());
            }
        }, "SocketTestThread");
        
        testThread.start();
        testThread.join(5000); // 等待5秒
        
        assertFalse("Test thread should complete", testThread.isAlive());
    }
}