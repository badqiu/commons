package com.github.rapid.common.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
/**
 * 实现LRUMap功能,用于cache使用
 * 注意: 在多线程环境下使用,需要同步
 * 
 * @author badqiu
 *
 * @param <K>
 * @param <V>
 */
public class LRUMap<K,V> extends LinkedHashMap<K,V>
{
    protected final int _maxEntries;
    
    private ReadWriteLock globalLock = new ReentrantReadWriteLock();
    private Lock readLock = globalLock.readLock();
    private Lock writeLock = globalLock.writeLock();
    
    public LRUMap(int initialEntries, int maxEntries)
    {
        super(initialEntries, 0.8f, true);
        _maxEntries = maxEntries;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K,V> eldest)
    {
        return size() > _maxEntries;
    }

    @Override
    public V get(Object key) {
    	try {
    		readLock.lock();
    		return super.get(key);
    	}finally {
    		readLock.unlock();
    	}
    }
    
    @Override
    public V put(K key, V value) {
    	try {
    		writeLock.lock();
    		return super.put(key, value);
    	}finally {
    		writeLock.unlock();
    	}
    }
    
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
    	try {
    		writeLock.lock();
    		super.putAll(m);
    	}finally {
    		writeLock.unlock();
    	}
    }
    
}
