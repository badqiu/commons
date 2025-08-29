package com.github.rapid.common.util;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 多租户本地锁管理器
 * 
 * 特性：
 * 1. 租户隔离：每个租户有独立的锁命名空间
 * 2. 锁复用：相同租户+锁名称复用锁对象
 * 3. 线程安全：ConcurrentHashMap保证并发安全
 * 4. 可重入锁：支持同一线程重复获取锁
 */
public class MultiGroupLockManager {
	
	private final static MultiGroupLockManager _instance = new MultiGroupLockManager();

    // 外层Map: 租户 -> (锁名称 -> 锁对象)
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ReentrantLock>> groupLockMap = 
        new ConcurrentHashMap<>();

    public static MultiGroupLockManager getInstance() {
    	return _instance;
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
		unlock(groupId,lockName.getName());
	}
	
    /**
     * 释放锁
     * @param groupId 租户ID
     * @param lockName 锁名称
     */
    public void unlock(String groupId, String lockName) {
        ReentrantLock lock = getLockNullable(groupId, lockName);
        if (lock != null && lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * 获取锁对象（无锁时自动创建）
     */
    ReentrantLock getLock(String groupId, Class lockName) {
        return getLock(groupId,lockName.getName());
    }
    
    /**
     * 获取锁对象（无锁时自动创建）
     */
    ReentrantLock getLock(String groupId, String lockName) {
        return groupLockMap
            .computeIfAbsent(groupId, k -> new ConcurrentHashMap<>())
            .computeIfAbsent(lockName, k -> new ReentrantLock());
    }

    /**
     * 安全获取锁对象（可能返回null）
     */
    private ReentrantLock getLockNullable(String groupId, String lockName) {
        ConcurrentHashMap<String, ReentrantLock> tenantLocks = groupLockMap.get(groupId);
        return (tenantLocks != null) ? tenantLocks.get(lockName) : null;
    }

    /**
     * 清理租户的锁资源（可选）
     * @param groupId 租户ID
     */
    public void cleanupGroupLock(String groupId) {
        groupLockMap.remove(groupId);
    }
}