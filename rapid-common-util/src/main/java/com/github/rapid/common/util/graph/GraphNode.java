package com.github.rapid.common.util.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * 代表图的某个节点
 * 
 * @author badqiu
 * 
 */
public class GraphNode<T extends GraphNode> implements Serializable {

	private static final long serialVersionUID = 5097996371381799833L;
	
	/**
	 * 节点ID
	 */
	private String id;
	/**
	 * 节点依赖的节点
	 */
	private String depends;

	private Graph<T> graph;
	
	/**
	 * 根据depends,计算得到,当前Node所有的孩子
	 */
	private List<T> childs = new ArrayList<T>();
	/**
	 * 根据depends,计算得到,当前Node所有的父亲
	 */
	private List<T> parents = new ArrayList<T>();
	
	private int value;

	public GraphNode() {
	}
	
	public GraphNode(String id) {
		super();
		this.id = id;
	}
	
	public GraphNode(String id, String depends) {
		super();
		this.id = id;
		this.depends = depends;
	}

	public Graph<T> getGraph() {
		return graph;
	}

	public void setGraph(Graph<T> graph) {
		this.graph = graph;
	}

	public String getDepends() {
		return depends;
	}

	public void setDepends(String depends) {
		this.depends = depends;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public List<T> getChilds() {
		if(childs == null) childs = new ArrayList<T>();
		
		return childs;
	}

	public void setChilds(List<T> childs) {
		this.childs = childs;
	}

	public List<T> getParents() {
		if(parents == null) parents = new ArrayList<T>();
		
		return parents;
	}

	public void setParents(List<T> parents) {
		this.parents = parents;
	}

	public void addChild(T child) {
		if(!getChilds().contains(child))
			getChilds().add(child);
	}

	public void addParent(T parent) {
		if(!getParents().contains(parent))
			getParents().add(parent);
	}
	
	public String dump(int tabsCount) {
		StringBuilder sb = new StringBuilder();
		sb.append(StringUtils.repeat("\t", tabsCount)+getId());
		for(T n : getChilds()) {
			sb.append("\n"+n.dump(tabsCount+1));
		}
		return sb.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GraphNode other = (GraphNode) obj;
		if (getId() == null) {
			if (other.getId() != null)
				return false;
		} else if (!getId().equals(other.getId()))
			return false;
		return true;
	}
	
}
