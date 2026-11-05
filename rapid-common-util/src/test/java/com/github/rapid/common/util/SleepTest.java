package com.github.rapid.common.util;

import java.sql.Timestamp;

public class SleepTest {
	
    public static void main(String[] args) throws InterruptedException {
        System.out.println("开始测试，每1秒打印一次，共10次");
        long start = System.currentTimeMillis();
        for (int i = 1; i <= 100000; i++) {
            long now = System.currentTimeMillis();
            long cost =  now - start;
            System.out.printf("第%d次 | 当前毫秒：%s  cost:%d %n", i, new Timestamp(now),cost);
            // 休眠 2000ms
            Thread.sleep(2000);
        }
        System.out.println("测试结束");
    }
    
}
