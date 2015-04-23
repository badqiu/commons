package com.duowan.common.util.tree;


/**
 * 代表一个树节点
 * 
 * @author badqiu
 *
 */
@SuppressWarnings("rawtypes")
public interface Node<T> extends Comparable{
	/**
	 * 树节点的ID
	 * @return
	 */
	public T getId();
	
	/**
	 * 树节点父亲ID
	 * @return
	 */	
	public T getParentId();
	
}
