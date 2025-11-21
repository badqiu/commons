package com.github.rapid.common.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 超时Socket类加载器
 * 用自定义的ModifiedSocket替换java.net.Socket
 */
public class TimeoutSocketClassLoader extends URLClassLoader {
    
    private static final String TARGET_CLASS_NAME = "java.net.Socket";
    public static final String REPLACEMENT_CLASS_NAME = "com.github.rapid.common.socket.ModifiedSocket";
    
    private final Map<String, Class<?>> loadedClasses = new HashMap<>();
    
    public TimeoutSocketClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
    
    public TimeoutSocketClassLoader(URL[] urls) {
        super(urls);
    }
    
    public TimeoutSocketClassLoader(ClassLoader parent) {
        super(new URL[0], parent);
    }
    
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // 首先检查是否已加载
            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass != null) {
                return loadedClass;
            }
            
            // 如果是要替换的Socket类，返回我们的自定义实现
            if (TARGET_CLASS_NAME.equals(name)) {
                return loadReplacementClass();
            }
            
            // 对于其他类，使用默认的类加载机制
            try {
                // 先尝试父类加载器（遵循双亲委派）
                return super.loadClass(name, resolve);
            } catch (ClassNotFoundException e) {
                // 如果父类加载器找不到，尝试自己加载
                return findClass(name);
            }
        }
    }
    
    /**
     * 加载替换的Socket类
     */
    private Class<?> loadReplacementClass() throws ClassNotFoundException {
        try {
            // 加载我们的ModifiedSocket类
            return loadClass(REPLACEMENT_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException("Failed to load replacement Socket class: " + REPLACEMENT_CLASS_NAME, e);
        }
    }
    
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        // 检查是否已经加载过
        if (loadedClasses.containsKey(name)) {
            return loadedClasses.get(name);
        }
        
        // 尝试从类路径加载
        try {
            String path = name.replace('.', '/') + ".class";
            InputStream is = getResourceAsStream(path);
            if (is == null) {
                throw new ClassNotFoundException("Class not found: " + name);
            }
            
            byte[] bytes = readFully(is);
            Class<?> clazz = defineClass(name, bytes, 0, bytes.length);
            loadedClasses.put(name, clazz);
            return clazz;
        } catch (IOException e) {
            throw new ClassNotFoundException("Failed to load class: " + name, e);
        }
    }
    
    /**
     * 读取输入流的所有字节
     */
    private byte[] readFully(InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[1024];
        while ((nRead = is.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }
        return buffer.toByteArray();
    }
    
    /**
     * 创建使用此ClassLoader的线程
     */
    public Thread createThreadWithThisClassLoader(Runnable runnable, String name) {
        return new Thread(() -> {
            Thread.currentThread().setContextClassLoader(this);
            runnable.run();
        }, name);
    }
    
    /**
     * 在当前线程中设置此类加载器
     */
    public void setAsContextClassLoader() {
        Thread.currentThread().setContextClassLoader(this);
    }
    
    /**
     * 检查类加载器是否工作正常
     */
    public boolean isWorking() {
        try {
            Class<?> socketClass = loadClass("java.net.Socket");
            return socketClass.getName().equals(REPLACEMENT_CLASS_NAME);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}