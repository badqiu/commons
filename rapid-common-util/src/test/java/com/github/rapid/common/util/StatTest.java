package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class StatTest {

    private Stat stat;

    @Before
    public void setUp() {
        stat = new Stat();
    }

    // 初始状态测试
    @Test
    public void testInitialState() {
        assertEquals(0.0, stat.getSum(), 0.001);
        assertEquals(0, stat.getCount());
        assertEquals(Double.POSITIVE_INFINITY,stat.getMin(), 0.001);
        assertEquals(Double.NEGATIVE_INFINITY,stat.getMax(), 0.001);
    }

    // 单个数值测试
    @Test
    public void testSingleNumber() {
        stat.addNumber(5.0);
        
        assertEquals(5.0, stat.getSum(), 0.001);
        assertEquals(1, stat.getCount());
        assertEquals(5.0, stat.getMin(), 0.001);
        assertEquals(5.0, stat.getMax(), 0.001);
        assertEquals(5.0, stat.getAvg(), 0.001);
    }

    // 多个数值测试
    @Test
    public void testMultipleNumbers() {
        stat.addNumber(3.0);
        stat.addNumber(7.0);
        stat.addNumber(2.0);
        
        assertEquals(12.0, stat.getSum(), 0.001);
        assertEquals(3, stat.getCount());
        assertEquals(2.0, stat.getMin(), 0.001);
        assertEquals(7.0, stat.getMax(), 0.001);
        assertEquals(4.0, stat.getAvg(), 0.001);
        System.out.println(stat);
    }

    // 负数测试
    @Test
    public void testNegativeNumbers() {
        stat.addNumber(-1.0);
        stat.addNumber(-5.0);
        
        assertEquals(-6.0, stat.getSum(), 0.001);
        assertEquals(-5.0, stat.getMin(), 0.001);
        assertEquals(-1.0, stat.getMax(), 0.001);
    }


    // 相同数值测试
    @Test
    public void testSameNumbers() {
        stat.addNumber(10.0);
        stat.addNumber(10.0);
        
        assertEquals(10.0, stat.getMin(), 0.001);
        assertEquals(10.0, stat.getMax(), 0.001);
        assertEquals(10.0, stat.getAvg(), 0.001);
    }

    @Test
    public void testNullNumbers() {
    	Long nullNum = null;
    	long v = 10;
        stat.addNumber(nullNum);
        stat.addNumber(nullNum);
        stat.addNumber(v);
        
        assertEquals(10.0, stat.getMin(), 0.001);
        assertEquals(10.0, stat.getMax(), 0.001);
        assertEquals(10.0, stat.getAvg(), 0.001);
        assertEquals(1, stat.getCount());
    }
    
    // 浮点数精度测试
    @Test
    public void testPrecision() {
        stat.addNumber(0.1);
        stat.addNumber(0.2);
        
        assertEquals(0.3, stat.getSum(), 0.0000001);
    }

    // 边界测试：空数据时的平均值处理
    @Test
    public void testAverageWithZeroCount() {
        Stat emptyAggr = new Stat();
        double avg = emptyAggr.getAvg(); // 应该抛出 ArithmeticException
        System.out.println("testAverageWithZeroCount, avg="+avg);
        assertEquals(String.valueOf(avg),"0.0");
    }
    
    @Test
    public void testToString() {
    	
    	for(int i = 0; i < 100; i++) {
    		stat.addNumber(i * i);
    		System.out.println(stat);
    	}
    }
    
}