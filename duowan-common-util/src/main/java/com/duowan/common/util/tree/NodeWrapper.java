package com.duowan.common.util.tree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
/**
 * 树节点的包装类
 * 
 * @author badqiu
 *
 */
public class NodeWrapper<T extends Node> implements Comparable<NodeWrapper>{
	private T node;
	private NodeWrapper<T> parentNode;
	private int level = -1;
	
	private List<NodeWrapper<T>> childs = new ArrayList<NodeWrapper<T>>();
	
	public NodeWrapper(){
	}
	
	public NodeWrapper(T node, NodeWrapper<T> parentNode,
			List<NodeWrapper<T>> childs, int level) {
		super();
		this.node = node;
		this.parentNode = parentNode;
		this.childs = childs;
		this.level = level;
	}



	public T getNode() {
		return node;
	}
	public void setNode(T node) {
		this.node = node;
	}
	public NodeWrapper<T> getParentNode() {
		return parentNode;
	}
	public void setParentNode(NodeWrapper<T> parentNode) {
		this.parentNode = parentNode;
	}
	public  List<NodeWrapper<T>> getChilds() {
		return childs;
	}
	public void setChilds(List<NodeWrapper<T>> childs) {
		this.childs = childs;
	}
	public Object getId() {
		return node.getId();
	}
	public Object getParentId() {
		return node.getParentId();
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	/**
	 * 是否是叶子节点
	 * @return
	 */
	public boolean isLeaf() {
		return childs == null || childs.isEmpty();
	}
	
	public String dump() {
		final StringBuilder dumpInfo = new StringBuilder();
		visitAllNode(new Visitor() {
			public void visit(NodeWrapper node) {
				dumpInfo.append(StringUtils.repeat("\t", node.getLevel())+node.getId()+"\n");
			}
		});
		return dumpInfo.toString();
	}
	
	public String toString() {
		return String.format("NodeWrapper[id=%s , parentId=%s]",getId(),getParentId());
	}
	
	void visitAllNode(Visitor visitor) {
		visitOneNode(this,visitor);
	}
	
	boolean deleteNode(final Object id) {
		DeleteVisistor visitor = new DeleteVisistor();
		visitor.parentId = id;
		visitAllNode(visitor);
		return visitor.deleted;
	}
	
	void addNode(final Node child) {
		visitAllNode(new Visitor() {
			public void visit(NodeWrapper node) {
				if(node.getId().equals(child.getParentId())) {
					NodeWrapper nodeWrapper = new NodeWrapper();
					nodeWrapper.setNode(child);
					nodeWrapper.setParentNode(node);
					nodeWrapper.setLevel(node.getLevel() + 1);
					node.childs.add(nodeWrapper);
				}
			}
		});
	}
	
	NodeWrapper findNode(final Object id) {
		FindVisistor find = new FindVisistor();
		find.id = id;
		visitAllNode(find);
		return find.node;
	}
	
	void visitOneNode(NodeWrapper node,Visitor visitor) {
		visitor.visit(node);
		List<NodeWrapper> childs2 = node.getChilds();
		for(NodeWrapper child : childs2) {
			visitOneNode(child,visitor);
		}
	}
	
	
	public static class FindVisistor implements Visitor {
		NodeWrapper node;
		Object id;
		public void visit(NodeWrapper node) {
			if(ObjectUtils.equals(node.getId(),id)) {
				this.node = node;
			}
		}
	};
	
	public static class DeleteVisistor implements Visitor {
		Object parentId;
		boolean deleted;
		public void visit(NodeWrapper node) {
			if(ObjectUtils.equals(node.getId(),parentId)) {
				List<NodeWrapper> childs = node.getParentNode().getChilds();
				for(Iterator<NodeWrapper> it = childs.iterator();it.hasNext(); ) {
					NodeWrapper nw = it.next();
					if(ObjectUtils.equals(nw.getId(),parentId)) {
						it.remove();
						deleted =true;
						return;
					}
				}
			}
		}
	}

	public int compareTo(NodeWrapper o) {
		return this.node.compareTo(o.getNode());
	};
	
	// TODO 增加将整颗树序列化成:  id,parent_id,ordered 的列表值,以便直接存储ID至数据库
	
}
