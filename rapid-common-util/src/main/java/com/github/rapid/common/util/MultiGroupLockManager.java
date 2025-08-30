package com.github.rapid.common.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多租户本地锁管理器
 * 
 * 特性：
 * 1. 租户隔离：每个租户有独立的锁命名空间
 * 2. 锁复用：相同租户+锁名称复用锁对象
 * 3. 线程安全：ConcurrentHashMap保证并发安全
 * 4. 可重入锁：支持同一线程重复获取锁
 * 5. 自动清理：定期清理过期未使用的锁
 */
public class MultiGroupLockManager {
    
    private final static MultiGroupLockManager _instance = new MultiGroupLockManager();

    // 外层Map: 租户 -> (锁名称 -> 锁包装对象)
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, LockWrapper>> groupLockMap = 
        new ConcurrentHashMap<>();
    
    // 定时清理服务
    private final ScheduledExecutorService cleanupScheduler = Executors.newScheduledThreadPool(1);
    
    // 默认锁过期时间（1天）
    private long defaultLockExpiryMs = TimeUnit.DAYS.toMillis(1);
    
    // 默认清理间隔（N分钟）
    private long defaultCleanupIntervalMs = TimeUnit.MINUTES.toMillis(10);
    
    public static MultiGroupLockManager getInstance() {
        return _instance;
    }
    
    /**
     * 构造函数，启动定时清理任务
     */
    private MultiGroupLockManager() {
        startCleanupTask();
    }
    
    /**
     * 启动定时清理任务
     */
    private void startCleanupTask() {
        cleanupScheduler.scheduleAtFixedRate(() -> {
            cleanupExpiredLocks();
        }, defaultCleanupIntervalMs, defaultCleanupIntervalMs, TimeUnit.MILLISECONDS);
    }
    
    /**
     * 设置锁过期时间
     * @param expiryTime 过期时间
     * @param unit 时间单位
     */
    public void setLockExpiry(long expiryTime, TimeUnit unit) {
        this.defaultLockExpiryMs = unit.toMillis(expiryTime);
    }
    
    /**
     * 设置清理间隔
     * @param interval 间隔时间
     * @param unit 时间单位
     */
    public void setCleanupInterval(long interval, TimeUnit unit) {
        this.defaultCleanupIntervalMs = unit.toMillis(interval);
        // 重启清理任务
        cleanupScheduler.shutdown();
        startCleanupTask();
    }
    
    /**
     * 阻塞式获取锁（直到成功）
     * @param groupId 租户ID
     * @param lockName 锁名称
     */
    public void lock(String groupId, Class lockName) {
        getLock(groupId, lockName).lock();
    }
    
    /**
     * 阻塞式获取锁（直到成功）
     * @param groupId 租户ID
     * @param lockName 锁名称
     */
    public void lock(String groupId, String lockName) {
        getLock(groupId, lockName).lock();
    }

    /**
     * 尝试获取锁（非阻塞）
     * @param groupId 租户ID
     * @param lockName 锁名称
     * @return true=获取成功, false=获取失败
     */
    public boolean tryLock(String groupId, String lockName) {
        return getLock(groupId, lockName).tryLock();
    }

    /**
     * 尝试获取锁（非阻塞）
     * @param groupId 租户ID
     * @param lockName 锁名称
     * @return true=获取成功, false=获取失败
     */
    public boolean tryLock(String groupId, Class lockName) {
        return getLock(groupId, lockName).tryLock();
    }
    
    /**
     * 超时等待获取锁
     * @param groupId 租户ID
     * @param lockName 锁名称
     * @param timeout 等待时间
     * @param unit 时间单位
     * @return true=获取成功, false=超时失败
     * @throws InterruptedException 中断异常
     */
    public boolean tryLock(String groupId, String lockName, long timeout, TimeUnit unit) throws InterruptedException {
        return getLock(groupId, lockName).tryLock(timeout, unit);
    }

    /**
     * 超时等待获取锁
     * @param groupId 租户ID
     * @param lockName 锁名称
     * @param timeout 等待时间
     * @param unit 时间单位
     * @return true=获取成功, false=超时失败
     * @throws InterruptedException 中断异常
     */
    public boolean tryLock(String groupId, Class lockName, long timeout, TimeUnit unit) throws InterruptedException {
        return getLock(groupId, lockName).tryLock(timeout, unit);
    }
    
    public void unlock(String groupId, Class lockName) {
        unlock(groupId, lockName.getName());
    }
    
    /**
     * 释放锁
     * @param groupId 租户ID
     * @param lockName 锁名称
     */
    public void unlock(String groupId, String lockName) {
        LockWrapper lockWrapper = getLockWrapperNullable(groupId, lockName);
        if (lockWrapper != null) {
            lockWrapper.unlock();
        }
    }

    /**
     * 获取锁对象（无锁时自动创建）
     */
    ReentrantLock getLock(String groupId, Class lockName) {
        return getLock(groupId, lockName.getName());
    }
    
    /**
     * 获取锁对象（无锁时自动创建）
     */
    ReentrantLock getLock(String groupId, String lockName) {
        return getLockWrapper(groupId, lockName).getLock();
    }
    
    /**
     * 获取锁包装对象（无锁时自动创建）
     */
    private LockWrapper getLockWrapper(String groupId, String lockName) {
        return groupLockMap
            .computeIfAbsent(groupId, k -> new ConcurrentHashMap<>())
            .computeIfAbsent(lockName, k -> new LockWrapper());
    }

    /**
     * 安全获取锁包装对象（可能返回null）
     */
    private LockWrapper getLockWrapperNullable(String groupId, String lockName) {
        ConcurrentHashMap<String, LockWrapper> tenantLocks = groupLockMap.get(groupId);
        return (tenantLocks != null) ? tenantLocks.get(lockName) : null;
    }
    
    /**
     * 安全获取锁对象（可能返回null）
     */
    private ReentrantLock getLockNullable(String groupId, String lockName) {
        LockWrapper wrapper = getLockWrapperNullable(groupId, lockName);
        return (wrapper != null) ? wrapper.getLock() : null;
    }

    /**
     * 清理租户的锁资源
     * @param groupId 租户ID
     */
    public void cleanupGroupLocks(String groupId) {
        groupLockMap.remove(groupId);
    }
    
    /**
     * 清理所有过期的锁
     */
    public void cleanupExpiredLocks() {
        long currentTime = System.currentTimeMillis();
        long expiryThreshold = currentTime - defaultLockExpiryMs;
        
        // 遍历所有租户
        for (String groupId : groupLockMap.keySet()) {
            ConcurrentHashMap<String, LockWrapper> tenantLocks = groupLockMap.get(groupId);
            if (tenantLocks == null) continue;
            
            // 遍历租户的所有锁
            for (String lockName : tenantLocks.keySet()) {
                LockWrapper wrapper = tenantLocks.get(lockName);
                // 检查锁是否过期且未被持有
                if (wrapper != null && wrapper.getLastAccessTime() < expiryThreshold && 
                    !wrapper.getLock().isLocked()) {
                    // 移除过期锁
                    tenantLocks.remove(lockName);
                }
            }
            
            // 如果租户没有锁了，移除租户条目
            if (tenantLocks.isEmpty()) {
                groupLockMap.remove(groupId);
            }
        }
    }
    
    /**
     * 获取当前管理的锁数量
     */
    public int getLockCount() {
        int count = 0;
        for (ConcurrentHashMap<String, LockWrapper> tenantLocks : groupLockMap.values()) {
            count += tenantLocks.size();
        }
        return count;
    }
    
    /**
     * 获取租户数量
     */
    public int getGroupCount() {
        return groupLockMap.size();
    }
    
    /**
     * 停止清理调度器
     */
    public void shutdown() {
        cleanupScheduler.shutdown();
    }
    
    /**
     * 锁包装类，包含锁对象和最后访问时间
     */
    private class LockWrapper {
        private final ReentrantLock lock;
        private long lastAccessTime;
        
        public LockWrapper() {
            this.lock = new ReentrantLock();
            this.lastAccessTime = System.currentTimeMillis();
        }
        
        public ReentrantLock getLock() {
            lastAccessTime = System.currentTimeMillis();
            return lock;
        }
        
        public long getLastAccessTime() {
            return lastAccessTime;
        }
        
        public void unlock() {
            try {
                lock.unlock();
            } finally {
            	lastAccessTime = System.currentTimeMillis();
            }
        }
    }
}