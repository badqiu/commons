package com.github.rapid.common.util.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.IteratorUtils;

/**
 * 包含分页信息 及 List的分页对象
 *  
 * @author badqiu
 *
 * @param <T>
 */
public class Page<T> implements Iterable<T>,Serializable{
	private List<T> itemList = new ArrayList<T>(0);
	private Paginator paginator = new Paginator(0, 0, 0);
	
	public Page() {
	}
	
	public Page(Paginator paginator) {
		setPaginator(paginator);
	}
	
	public Page(List<T> itemList, Paginator paginator) {
		setItemList(itemList);
		setPaginator(paginator);
	}

	public List<T> getItemList() {
		return itemList;
	}

	public void setItemList(List<T> itemList) {
		this.itemList = itemList;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		this.paginator = paginator;
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
		return itemList == null ? IteratorUtils.EMPTY_ITERATOR : itemList.iterator();
	}
	
}
