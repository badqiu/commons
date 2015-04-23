package com.duowan.common.util.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.util.Assert;


/**
 * 创建树型结构数据的工具类
 * 
 * @author badqiu
 *
 */
public class TreeCreator {
	
	/**
	 * 创建一棵树
	 * 
	 * @param nodes 所有节点
	 * @param rootNodeId 根节点的ID
	 * 
	 * @return
	 */
	public NodeWrapper createTree(List<? extends Node> nodes,Object rootNodeId) {
		List<NodeWrapper> nodeWrapers = toNodeWrapers(nodes);
		
		Node rootNode = findNode(nodes,rootNodeId);
		Assert.notNull(rootNode,"not found rootNode by id:"+rootNodeId);
		
		NodeWrapper rootWrapper = new NodeWrapper();
		rootWrapper.setNode(rootNode);
		rootWrapper.setParentNode(findParentNode(nodeWrapers,rootWrapper));
		rootWrapper.setChilds(findChilds(nodeWrapers,rootNodeId));
		rootWrapper.setLevel(0);
		
		setChildsTree(nodeWrapers,rootWrapper,1);
		return rootWrapper;
	}

	private List<NodeWrapper> toNodeWrapers(List<? extends Node> nodes) {
		List<NodeWrapper> nodeWrapers = new ArrayList<NodeWrapper>();
		for(Node node : nodes) {
			NodeWrapper wrapper = new NodeWrapper();
			wrapper.setNode(node);
			nodeWrapers.add(wrapper);
		}
		return nodeWrapers;
	}

	private void setChildsTree(List<NodeWrapper> nodes, NodeWrapper current,int level) {
		List<NodeWrapper> childs = current.getChilds();
		for(NodeWrapper child : childs) {
			child.setParentNode(findParentNode(nodes,child));
			child.setChilds(findChilds(nodes,child.getId()));
			child.setLevel(level);
			setChildsTree(nodes,child,level+1);
		}
	}

	private NodeWrapper findParentNode(List<NodeWrapper> nodes,NodeWrapper current) {
		for(NodeWrapper node : nodes) {
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
	
	private List<NodeWrapper> findChilds(List<NodeWrapper> nodes, Object parentId) {
		List<NodeWrapper> childs = new ArrayList<NodeWrapper>();
		for(NodeWrapper node : nodes) {
			Object nodeParentId = node.getParentId();
			if(nodeParentId != null && nodeParentId.equals(parentId)) {
				childs.add(node);
			}
		}
		Collections.sort(childs);
		return childs;
	}
	
}
