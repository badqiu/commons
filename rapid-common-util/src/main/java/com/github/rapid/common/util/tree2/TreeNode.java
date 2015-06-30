package com.github.rapid.common.util.tree2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 代表一个树节点
 * 
 * @author badqiu
 *
 */
@SuppressWarnings("rawtypes")
public class TreeNode<T> implements Comparable {
	private T id;
	private T parentId;
	private int depth = -1; //树节点的深度
	private TreeNode<T> parent = null;
	private List<TreeNode<T>> childs = new ArrayList<TreeNode<T>>();
	
	public TreeNode() {
	}
	
	public TreeNode(T id, T parentId) {
		super();
		this.id = id;
		this.parentId = parentId;
	}
	
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
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public TreeNode<T> getParent() {
		return parent;
	}
	public void setParent(TreeNode<T> parent) {
		this.parent = parent;
	}
	public List<TreeNode<T>> getChilds() {
		return childs;
	}
	public void setChilds(List<TreeNode<T>> childs) {
		this.childs = childs;
	}
	
	/**
	 * 是否是叶子节点
	 * @return
	 */
	public boolean isLeaf() {
		return childs == null || childs.isEmpty();
	}

	public int compareTo(Object o) {
		return 0;
	}
}
