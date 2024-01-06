package com.github.rapid.common.util.page;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.github.rapid.common.util.PrintUtil;

import junit.framework.TestCase;

public class PaginatorTest extends TestCase {
	
	Paginator p = new Paginator(10,10,101);
	
	public void setUp() {
//	    p.setPage(10);
//		p.setPageSize(10);
//		p.setTotalItems(101);		
	}
	
	public void test_getTotalPages() {
		assertEquals(p.getTotalPages(), 11);
	}
	
	public void test_is() {
		assertEquals(p.toString(),p.isFirstPage(),false);
		assertEquals(p.isLastPage(),false);
		assertEquals(p.isHasNextPage(),true);
		assertEquals(p.isHasPrePage(),true);
		
		assertFalse(p.isDisabledPage(1));
		assertFalse(p.isDisabledPage(5));
		assertFalse(p.isDisabledPage(11));
		
		assertTrue(p.isDisabledPage(10));
		
		assertTrue(p.isDisabledPage(-1));
		assertTrue(p.isDisabledPage(0));
		assertTrue(p.isDisabledPage(12));
		assertTrue(p.isDisabledPage(30));
		
		p = new Paginator(1,10,101);
		assertEquals(p.isFirstPage(),true);
		assertEquals(p.isLastPage(),false);
		assertEquals(p.isHasPrePage(),false);
		assertEquals(p.isHasNextPage(),true);
		
		p = new Paginator(11,10,101);
		assertEquals(p.isFirstPage(),false);
		assertEquals(p.isLastPage(),true);
		assertEquals(p.isHasPrePage(),true);
		assertEquals(p.isHasNextPage(),false);
		
		p = new Paginator(5,10,101);
		assertEquals(p.isFirstPage(),false);
		assertEquals(p.isLastPage(),false);
		assertEquals(p.isHasPrePage(),true);
		assertEquals(p.isHasNextPage(),true);
		
		p = new Paginator(0,0,0);
        assertEquals(p.isFirstPage(),true);
        assertEquals(p.isLastPage(),true);
        assertEquals(p.isHasPrePage(),false);
        assertEquals(p.isHasNextPage(),false);
        
        p = new Paginator(0,10,0);
        assertEquals(p.isFirstPage(),true);
        assertEquals(p.isLastPage(),true);
        assertEquals(p.isHasPrePage(),false);
        assertEquals(p.isHasNextPage(),false);
        
        
        p = new Paginator(0,0,10);
        assertEquals(p.isFirstPage(),true);
        assertEquals(p.isLastPage(),true);
        assertEquals(p.isHasPrePage(),false);
        assertEquals(p.isHasNextPage(),false);
        
        p = new Paginator(0,10,100);
        assertEquals(p.isFirstPage(),true);
        assertEquals(p.isLastPage(),false);
        assertEquals(p.isHasPrePage(),false);
        assertEquals(p.isHasNextPage(),true);
	}
	
	public void test_get() {
		assertEquals(p.getStartRow(),91);
		assertEquals(p.getEndRow(),100);
		assertEquals(p.getOffset(),90);
		assertEquals(p.getPage(),10);
		assertEquals(p.getPageSize(),10);
		assertEquals(p.getPrePage(),9);
		assertEquals(p.getNextPage(),11);
		assertEquals(p.getTotalPages(),11);
		assertEquals(p.getTotalItems(),101);
	}
	
	public void test_get_with_pageSize() {
	    p = new Paginator(10,5,101);
		assertEquals(p.getStartRow(),46);
		assertEquals(p.getEndRow(),50);
		assertEquals(p.getOffset(),45);
		assertEquals(p.getLimit(),5);
		assertEquals(p.getEndOffset(),50);
		assertEquals(p.getPage(),10);
		assertEquals(p.getPageSize(),5);
		assertEquals(p.getPrePage(),9);
		assertEquals(p.getNextPage(),11);
		assertEquals(p.getTotalPages(),21);
		assertEquals(p.getTotalItems(),101);

		p = new Paginator(10,200,Integer.MAX_VALUE);
		assertEquals(p.getStartRow(),1801);
		assertEquals(p.getEndRow(),2000);
		assertEquals(p.getOffset(),1800);
		assertEquals(p.getLimit(),200);
		assertEquals(p.getEndOffset(),2000);
		assertEquals(p.getPage(),10);
		assertEquals(p.getPageSize(),200);
		assertEquals(p.getPrePage(),9);
		assertEquals(p.getNextPage(),11);
		assertEquals(p.getTotalPages(),10737419);
		assertEquals(p.getTotalItems(),Integer.MAX_VALUE);
		
		for(long i = 1; i <= p.getTotalPages(); i++) {
		}
	}

	
	public void test_limit() {
	    assertEquals(p.getLimit(),10);
	    assertEquals(new Paginator(11,10,101).getLimit(),1);
	}
	
	public void test_zero() {
		p = new Paginator(0,0,0);
		
		assertEquals(p.getStartRow(),0);
		assertEquals(p.getEndRow(),0);
		assertEquals(p.getOffset(),0);
		assertEquals(p.getPage(),1);
		assertEquals(p.getPageSize(),0);
		assertEquals(p.getPrePage(),1);
		assertEquals(p.getNextPage(),1);
		assertEquals(p.getTotalPages(),0);
		assertEquals(p.getTotalItems(),0);
		
		p = new Paginator(10,0,0);
		
		assertEquals(p.getStartRow(),0);
		assertEquals(p.getEndRow(),0);
		assertEquals(p.getOffset(),0);
		assertEquals(p.getPage(),1);
		assertEquals(p.getPageSize(),0);
		assertEquals(p.getPrePage(),1);
		assertEquals(p.getNextPage(),1);
		assertEquals(p.getTotalPages(),0);
		assertEquals(p.getTotalItems(),0);
		
		p = new Paginator(0,10,0);
		
		assertEquals(p.getStartRow(),0);
		assertEquals(p.getEndRow(),0);
		assertEquals(p.getOffset(),0);
		assertEquals(p.getPage(),1);
		assertEquals(p.getPageSize(),10);
		assertEquals(p.getPrePage(),1);
		assertEquals(p.getNextPage(),1);
		assertEquals(p.getTotalPages(),0);
		assertEquals(p.getTotalItems(),0);
		
		p = new Paginator(0,0,10);
		
		assertEquals(p.getStartRow(),0);
		assertEquals(p.getEndRow(),0);
		assertEquals(p.getOffset(),0);
		assertEquals(p.getPage(),1);
		assertEquals(p.getPageSize(),0);
		assertEquals(p.getPrePage(),1);
		assertEquals(p.getNextPage(),1);
		assertEquals(p.getTotalPages(),0);
		assertEquals(p.getTotalItems(),10);

	}
	
//	public void test_getSlider() {
//		System.out.println(PrintUtil.toString(p.getSlider()));
//	}
	
	@Test
	public void test_page_for_list() {
		List list = new ArrayList();
		for(long i = 0; i < 112;i++) {
			list.add(i);
		}
		
		p = new Paginator(0,10,list.size());
		System.out.println(sublist(list,p));
		assertEquals(sublist(list,p).toString(),"[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]");
		
		p = new Paginator(2,10,list.size());
		System.out.println(sublist(list,p));
		assertEquals(sublist(list,p).toString(),"[10, 11, 12, 13, 14, 15, 16, 17, 18, 19]");
		
		p = new Paginator(11,10,list.size());
		System.out.println(sublist(list,p));
		assertEquals(sublist(list,p).toString(),"[100, 101, 102, 103, 104, 105, 106, 107, 108, 109]");
		
		p = new Paginator(12,10,list.size());
		System.out.println(sublist(list,p));
		assertEquals(sublist(list,p).toString(),"[110, 111]");
		
		p = new Paginator(100,10,list.size());
		System.out.println(sublist(list,p));
		assertEquals(sublist(list,p).toString(),"[110, 111]");
	}

	private List sublist(List list,long offset,long endOffset) {
		return list.subList((int)offset, (int)endOffset);
	}
	
	private List sublist(List list,Paginator p) {
		return sublist(list,p.getOffset(),p.getEndOffset());
	}
	
}
