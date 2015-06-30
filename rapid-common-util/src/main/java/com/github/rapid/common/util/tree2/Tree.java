package com.github.rapid.common.util.tree2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.util.Assert;



public class Tree {

	private List<Node> nodes = new ArrayList<Node>();
	private Node root;
	
	public void addNode(Node node) {
		nodes.add(node);
	}
	
	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public Node getRoot() {
		return root;
	}

	public void setRoot(Node root) {
		this.root = root;
	}

	public void init(Object rootNodeId) {
		Node rootNode = findNode(nodes,rootNodeId);
		Assert.notNull(rootNode,"not found rootNode by id:"+rootNodeId);
		rootNode.setParent(findParentNode(nodes,rootNode));
		rootNode.setChilds(findChilds(nodes,rootNodeId));
		rootNode.setLevel(0);
		setChildsTree(nodes,rootNode,1);
		this.root = rootNode;
	}
	
	private void setChildsTree(List<Node> nodes, Node current,int level) {
		List<Node> childs = current.getChilds();
		for(Node child : childs) {
			child.setParent(findParentNode(nodes,child));
			child.setChilds(findChilds(nodes,child.getId()));
			child.setLevel(level);
			setChildsTree(nodes,child,level+1);
		}
	}

	private Node findParentNode(List<Node> nodes,Node current) {
		for(Node node : nodes) {
			if(ObjectUtils.equals(node.getId(),current.getParentId())) {
				return node;
			}
		}
		return null;
	}

	private Node findNode(List<? extends Node> nodes, Object id) {
		for(Node node : nodes) {
			if(node.getId().equals(id)) {
				return node;
			}
		}
		return null;
	}
	
	private List<Node> findChilds(List<Node> nodes, Object parentId) {
		List<Node> childs = new ArrayList<Node>();
		for(Node node : nodes) {
			Object nodeParentId = node.getParentId();
			if(nodeParentId != null && nodeParentId.equals(parentId)) {
				childs.add(node);
			}
		}
		Collections.sort(childs);
		return childs;
	}
}
