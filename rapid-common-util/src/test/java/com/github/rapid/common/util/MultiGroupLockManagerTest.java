package com.github.rapid.common.util;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.Before;
import org.junit.Test;

/**
 * MultiGroupLockManager 单元测试
 */
public class MultiGroupLockManagerTest {

    private MultiGroupLockManager lockManager;

    @Before
    public void setUp() {
        lockManager = MultiGroupLockManager.getInstance();
        
        // 设置较短的过期时间和清理间隔便于测试
        lockManager.setLockExpiry(2, TimeUnit.SECONDS);
        lockManager.setCleanupInterval(1, TimeUnit.SECONDS);
        
    }

    @Test
    public void testBasicLockAndUnlock() throws InterruptedException {
        String groupId = "tenant1";
        String lockName = "resource1";

        // 获取锁
        lockManager.lock(groupId, lockName);
        
        newThreadTryLock(groupId, lockName,false);

        // 释放锁
        lockManager.unlock(groupId, lockName);
        
        // 验证锁已释放
        newThreadTryLock(groupId, lockName,true);

        lockManager.unlock(groupId, lockName);
    }

    @Test
    public void testLockIsolationBetweenGroups() throws InterruptedException {
        String group1 = "tenant1";
        String group2 = "tenant2";
        String lockName = "commonResource";

        CountDownLatch latch = new CountDownLatch(2);
        AtomicInteger group1LockCount = new AtomicInteger(0);
        AtomicInteger group2LockCount = new AtomicInteger(0);

        // 线程1: 租户1
        new Thread(() -> {
            lockManager.lock(group1, lockName);
            group1LockCount.incrementAndGet();
            latch.countDown();
            lockManager.unlock(group1, lockName);
        }).start();

        // 线程2: 租户2
        new Thread(() -> {
            lockManager.lock(group2, lockName);
            group2LockCount.incrementAndGet();
            latch.countDown();
            lockManager.unlock(group2, lockName);
        }).start();

        latch.await(2, TimeUnit.SECONDS);

        assertEquals("租户1应成功获取锁",1, group1LockCount.get());
        assertEquals("租户2应成功获取锁",1, group2LockCount.get() );
        // 测试通过表明不同租户的锁互不干扰
    }

    @Test
    public void testLockExpiry() throws InterruptedException {
        String groupId = "testGroup";
        String lockName = "testLock";
        
        // 获取并释放锁
        lockManager.lock(groupId, lockName);
        lockManager.unlock(groupId, lockName);
        
        int initialCount = lockManager.getLockCount();
        assertTrue("锁应该存在", initialCount > 0);
        
        // 等待锁过期
        Thread.sleep(3000);
        
        // 手动触发清理
        lockManager.cleanupExpiredLocks();
        
        int finalCount = lockManager.getLockCount();
        assertEquals("过期锁应该被清理", 0, finalCount);
    }
    
    @Test
    public void testMultiGroupIsolation() {
        String group1 = "group1";
        String group2 = "group2";
        String lockName = "sameLockName";
        
        // 为两个不同租户获取锁
        lockManager.lock(group1, lockName);
        lockManager.lock(group2, lockName);
        
        // 验证两个锁是独立的
        assertNotSame("不同租户的锁应该是不同的对象", 
                     lockManager.getLockNullable(group1, lockName), 
                     lockManager.getLockNullable(group2, lockName));
        
        // 验证两个锁都被持有
        assertTrue("组1的锁应该被持有", lockManager.getLockNullable(group1, lockName).isLocked());
        assertTrue("组2的锁应该被持有", lockManager.getLockNullable(group2, lockName).isLocked());
        
        // 释放锁
        lockManager.unlock(group1, lockName);
        lockManager.unlock(group2, lockName);
        
        assertFalse("组1的锁应该被释放", lockManager.getLockNullable(group1, lockName).isLocked());
        assertFalse("组2的锁应该被释放", lockManager.getLockNullable(group2, lockName).isLocked());
    }
    
    @Test
    public void testReentrantLock() {
        String groupId = "tenant1";
        String lockName = "reentrantResource";

        // 第一次获取锁
        lockManager.lock(groupId, lockName);
        
        // 同一线程再次获取（应成功，因为可重入）
        boolean reentered = lockManager.tryLock(groupId, lockName);
        assertTrue("可重入锁应支持同一线程多次获取",reentered);

        // 第一次释放（锁仍被持有）
        lockManager.unlock(groupId, lockName);
        
        // 第二次释放（完全释放）
        lockManager.unlock(groupId, lockName);

        // 验证锁已完全释放
        boolean acquired = lockManager.tryLock(groupId, lockName);
        assertTrue("锁完全释放后应能重新获取",acquired);
        lockManager.unlock(groupId, lockName);
    }

    @Test
    public void testTryLockWithTimeout() throws InterruptedException {
        String groupId = "tenant1";
        String lockName = "timeoutResource";

        // 先让一个线程持有锁
        lockManager.lock(groupId, lockName);

        newThreadTryLock(groupId, lockName,false);
        
        lockManager.unlock(groupId, lockName);
    }

	private void newThreadTryLock(String groupId, String lockName,boolean expectedAcquired) throws InterruptedException {
		Thread t = new Thread(() -> {
	        // 另一个线程尝试超时获取
        	try {
		        boolean acquired = lockManager.tryLock(groupId, lockName, 100, TimeUnit.MILLISECONDS);
		        assertEquals("锁被占有时应获取超时",acquired,expectedAcquired);
        	}catch(Exception e) {
        		throw new RuntimeException(e);
        	}
        });
        t.start();
        t.join();
	}

    @Test
    public void testConcurrentAccess() throws InterruptedException {
        String groupId = "tenantConcurrent";
        String lockName = "concurrentResource";
        int threadCount = 10;
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch finishLatch = new CountDownLatch(threadCount);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        for (int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();
                    if (lockManager.tryLock(groupId, lockName, 500, TimeUnit.MILLISECONDS)) {
                        System.out.println("tryLock success:"+Thread.currentThread().getName());
                    	successCount.incrementAndGet();
                        Thread.sleep(600); // 模拟操作
                        lockManager.unlock(groupId, lockName);
                    } else {
                        failCount.incrementAndGet();
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    finishLatch.countDown();
                }
            }).start();
        }

        startLatch.countDown(); // 同时开始
        finishLatch.await();

        System.out.println("successCount:"+successCount);
        assertEquals("只有一个线程应获取锁成功",1, successCount.get());
        assertEquals("其他线程应获取失败",threadCount - 1, failCount.get());
    }

    @Test
    public void testCleanupTenant() {
        String groupId = "tempTenant";
        String lockName = "tempLock";

        // 先创建一些锁
        lockManager.lock(groupId, lockName);
        lockManager.unlock(groupId, lockName);

        // 清理租户
        lockManager.cleanupGroupLocks(groupId);

        // 再次获取锁（会创建新锁对象）
        ReentrantLock newLock = lockManager.getLock(groupId, lockName);
        assertNotNull( "清理后应能重新创建锁",newLock);
        lockManager.unlock(groupId, lockName);
    }

    @Test
    public void testUnlockByNonOwner() throws InterruptedException {
        String groupId = "tenant1";
        String lockName = "testLock";

        // 线程1获取锁
        lockManager.lock(groupId, lockName);

        // 线程2尝试释放（应无效）
        new Thread(() -> {
            lockManager.unlock(groupId, lockName);
            // 无异常抛出即为通过
        }).start();

        // 线程1应仍持有锁
        newThreadTryLock(groupId, lockName, false);

        lockManager.unlock(groupId, lockName); // 真正释放
    }
    
    
    @Test
    public void testTryLockWithTimeout2() throws InterruptedException {
        String groupId = "testGroup";
        String lockName = "testLock";
        
        // 线程1获取锁并保持一段时间
        Thread thread1 = new Thread(() -> {
            lockManager.lock(groupId, lockName);
            try {
                Thread.sleep(1000); // 持有锁1秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lockManager.unlock(groupId, lockName);
            }
        });
        
        thread1.start();
        
        // 主线程稍等一会确保线程1先获取锁
        Thread.sleep(100);
        
        // 尝试获取锁，设置超时时间
        boolean acquired = lockManager.tryLock(groupId, lockName, 2, TimeUnit.SECONDS);
        
        assertTrue("应该在超时时间内成功获取锁", acquired);
        
        if (acquired) {
            lockManager.unlock(groupId, lockName);
        }
        
        thread1.join();
    }
}