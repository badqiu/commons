package com.github.rapid.common.util.page;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 带分页信息的List
 * @param <T>
 */
public class PageList<T> extends ArrayList<T> {

	private static final long serialVersionUID = 1L;
	
	/** 分页大小 */
	private long pageSize;
	/** 页数 */
	private long page;
	/** 总记录数 */
	private long totalItems;

	public PageList() {
		super();
	}

	public PageList(Collection<T> list) {
		super(list);
	}

	public PageList(long page, long pageSize, long totalItems) {
		super();
		this.page = page;
		this.pageSize = pageSize;
		this.totalItems = totalItems;
	}
	
	public PageList(Collection<T> list,long page, long pageSize, long totalItems) {
		super(list);
		this.page = page;
		this.pageSize = pageSize;
		this.totalItems = totalItems;
	}

	public long getPageSize() {
		return pageSize;
	}

	public void setPageSize(long pageSize) {
		this.pageSize = pageSize;
	}

	public long getPage() {
		return page;
	}

	public void setPage(long page) {
		this.page = page;
	}

	public long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}
	
	

	
}
