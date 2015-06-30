package com.github.rapid.common.util.tree2;

import java.util.ArrayList;
import java.util.List;


/**
 * 代表一个树节点
 * 
 * @author badqiu
 *
 */
@SuppressWarnings("rawtypes")
public abstract class Node<T> implements Comparable<T>{
	private T id;
	private T parentId;
	private int level = -1;
	private Node parent = null;
	private List<Node<T>> childs = new ArrayList<Node<T>>();
	public T getId() {
		return id;
	}
	public void setId(T id) {
		this.id = id;
	}
	public T getParentId() {
		return parentId;
	}
	public void setParentId(T parentId) {
		this.parentId = parentId;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Node getParent() {
		return parent;
	}
	public void setParent(Node parent) {
		this.parent = parent;
	}
	public List<Node<T>> getChilds() {
		return childs;
	}
	public void setChilds(List<Node<T>> childs) {
		this.childs = childs;
	}
	
	/**
	 * 是否是叶子节点
	 * @return
	 */
	public boolean isLeaf() {
		return childs == null || childs.isEmpty();
	}
}
