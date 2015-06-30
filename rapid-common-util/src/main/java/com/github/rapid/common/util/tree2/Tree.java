package com.github.rapid.common.util.tree2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.util.Assert;



public class Tree <T> {

	private List<TreeNode<T>> nodes = new ArrayList<TreeNode<T>>();
	private TreeNode<T> root;
	
	public void addNode(TreeNode<T> node) {
		nodes.add(node);
	}
	
	public List<TreeNode<T>> getNodes() {
		return nodes;
	}

	public void setNodes(List<TreeNode<T>> nodes) {
		this.nodes = nodes;
	}

	public TreeNode<T> getRoot() {
		return root;
	}

	public void setRoot(TreeNode<T> root) {
		this.root = root;
	}

	public void init(Object rootNodeId) {
		TreeNode rootNode = findNode(nodes,rootNodeId);
		Assert.notNull(rootNode,"not found rootNode by id:"+rootNodeId);
		rootNode.setParent(findParentNode(nodes,rootNode));
		rootNode.setChilds(findChilds(nodes,rootNodeId));
		rootNode.setDepth(0);
		setChildsTree(nodes,rootNode,1);
		this.root = rootNode;
	}
	
	private void setChildsTree(List<TreeNode<T>> nodes, TreeNode<T> current,int depth) {
		List<TreeNode<T>> childs = current.getChilds();
		for(TreeNode<T> child : childs) {
			child.setParent(findParentNode(nodes,child));
			child.setChilds(findChilds(nodes,child.getId()));
			child.setDepth(depth);
			setChildsTree(nodes,child,depth+1);
		}
	}

	private TreeNode<T> findParentNode(List<TreeNode<T>> nodes,TreeNode current) {
		for(TreeNode<T> node : nodes) {
			if(ObjectUtils.equals(node.getId(),current.getParentId())) {
				return node;
			}
		}
		return null;
	}

	private TreeNode<T> findNode(List<? extends TreeNode<T>> nodes, Object id) {
		for(TreeNode<T> node : nodes) {
			if(node.getId().equals(id)) {
				return node;
			}
		}
		return null;
	}
	
	private List<TreeNode<T>> findChilds(List<TreeNode<T>> nodes, Object parentId) {
		List childs = new ArrayList<TreeNode<T>>();
		for(TreeNode<T> node : nodes) {
			Object nodeParentId = node.getParentId();
			if(nodeParentId != null && nodeParentId.equals(parentId)) {
				childs.add(node);
			}
		}
		Collections.sort(childs);
		return childs;
	}
}
