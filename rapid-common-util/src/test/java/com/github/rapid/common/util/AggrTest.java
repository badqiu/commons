package com.github.rapid.common.util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AggrTest {

    private Aggr aggr;

    @Before
    public void setUp() {
        aggr = new Aggr();
    }

    // 初始状态测试
    @Test
    public void testInitialState() {
        assertEquals(0.0, aggr.getSum(), 0.001);
        assertEquals(0, aggr.getCount());
        assertNull(aggr.getMin());
        assertNull(aggr.getMax());
    }

    // 单个数值测试
    @Test
    public void testSingleNumber() {
        aggr.addNumber(5.0);
        
        assertEquals(5.0, aggr.getSum(), 0.001);
        assertEquals(1, aggr.getCount());
        assertEquals(5.0, aggr.getMin(), 0.001);
        assertEquals(5.0, aggr.getMax(), 0.001);
        assertEquals(5.0, aggr.getAvg(), 0.001);
    }

    // 多个数值测试
    @Test
    public void testMultipleNumbers() {
        aggr.addNumber(3.0);
        aggr.addNumber(7.0);
        aggr.addNumber(2.0);
        
        assertEquals(12.0, aggr.getSum(), 0.001);
        assertEquals(3, aggr.getCount());
        assertEquals(2.0, aggr.getMin(), 0.001);
        assertEquals(7.0, aggr.getMax(), 0.001);
        assertEquals(4.0, aggr.getAvg(), 0.001);
    }

    // 负数测试
    @Test
    public void testNegativeNumbers() {
        aggr.addNumber(-1.0);
        aggr.addNumber(-5.0);
        
        assertEquals(-6.0, aggr.getSum(), 0.001);
        assertEquals(-5.0, aggr.getMin(), 0.001);
        assertEquals(-1.0, aggr.getMax(), 0.001);
    }


    // 相同数值测试
    @Test
    public void testSameNumbers() {
        aggr.addNumber(10.0);
        aggr.addNumber(10.0);
        
        assertEquals(10.0, aggr.getMin(), 0.001);
        assertEquals(10.0, aggr.getMax(), 0.001);
        assertEquals(10.0, aggr.getAvg(), 0.001);
    }

    // 浮点数精度测试
    @Test
    public void testPrecision() {
        aggr.addNumber(0.1);
        aggr.addNumber(0.2);
        
        assertEquals(0.3, aggr.getSum(), 0.0000001);
    }

    // 边界测试：空数据时的平均值处理
    @Test
    public void testAverageWithZeroCount() {
        Aggr emptyAggr = new Aggr();
        double avg = emptyAggr.getAvg(); // 应该抛出 ArithmeticException
        System.out.println("testAverageWithZeroCount, avg="+avg);
        assertEquals(String.valueOf(avg),"NaN");
    }
}