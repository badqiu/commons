package com.github.rapid.common.util.page;
/**
 * 分页查询对象
 * @author badqiu
 * @version $Id: PageQuery.java,v 0.1 2010-11-29 下午05:34:12 badqiu Exp $
 */
public class PageQuery implements java.io.Serializable{
    private static final long serialVersionUID = -8000900575354501298L;
    
    public static long DEFAULT_PAGE_SIZE = 10;
    
    /** 页数  */
    private long    page;
    /** 分页大小 */
    private long    pageSize = DEFAULT_PAGE_SIZE;

    public PageQuery() {
    }

    public PageQuery(long pageSize) {
        this.pageSize = pageSize;
    }
    
    public PageQuery(PageQuery query) {
        this.page = query.page;
        this.pageSize = query.pageSize;
    }
    
    public PageQuery(long page, long pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }
    
    public PageQuery page(long page) {
    	this.page = page;
    	return this;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }
    
    public PageQuery pageSize(long pageSize) {
    	this.pageSize = pageSize;
    	return this;
    }

    public String toString() {
    	return "page:"+page+",pageSize:"+pageSize;
    }
    
}
