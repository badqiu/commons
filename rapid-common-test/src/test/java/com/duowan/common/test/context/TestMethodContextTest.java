package com.duowan.common.test.context;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

public class TestMethodContextTest {
    @Rule public TestName testName = new TestName();
    
    @Test
    public void test_alibaba() {
        System.out.println(getClass());
        System.out.println("current test method:"+ testName.getMethodName());
        Assert.assertEquals("test_alibaba", testName.getMethodName());
    }

    @Test
    public void test_foo() {
        Assert.assertEquals("test_foo", testName.getMethodName());
    }
    
    @Test
    public void test() {
    	System.out.println("061805FB9A8049DD92B7270159DD967E".getBytes().hashCode()%100);
    	System.out.println("061805FB9A8049DD92B7270159DD967E".hashCode()%100);
    }
    
}
