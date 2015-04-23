


package com.github.rapid.common.util.page;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.github.rapid.common.util.page.Page;
import com.github.rapid.common.util.page.Paginator;


public class PageListTest{
    protected Page<AssertionError> pageList = new Page<AssertionError>();
    
    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void test_toPaginator() throws Throwable{
        
        Paginator result = pageList.getPaginator();
        assertNotNull(result);
    }
    
    
}
