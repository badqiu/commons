package com.duowan.common.util.datastruct.tree;

import java.util.List;

public class TreeNode <NODE,ID>{

	private ID nodeId;
	private ID parentId;
	private int nodeLevel = -1; //当前节点在那一层
	
	private NODE parent;
	private List<NODE> childs;
	
	
	public ID getNodeId() {
		return nodeId;
	}
	public void setNodeId(ID nodeId) {
		this.nodeId = nodeId;
	}
	public ID getParentId() {
		return parentId;
	}
	public void setParentId(ID parentId) {
		this.parentId = parentId;
	}
	public int getNodeLevel() {
		return nodeLevel;
	}
	public void setNodeLevel(int level) {
		this.nodeLevel = level;
	}
	public NODE getParent() {
		return parent;
	}
	public void setParent(NODE parent) {
		this.parent = parent;
	}
	public List<NODE> getChilds() {
		return childs;
	}
	public void setChilds(List<NODE> childs) {
		this.childs = childs;
	}
	
	
	
}
