package com.github.rapid.common.util.graph;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * 代表一张有方向的图,各个节点连接有方向
 * 
 * @author badqiu
 *
 */
public class Graph <T extends GraphNode>{

	private List<T> nodes = new ArrayList<T>();
	private List<GraphEdge> edges = new ArrayList<GraphEdge>();
	
	public Graph(){
	}

	/**
	 * 初始化图
	 */
	public void init() {
		initAllNodeDepends();
	}

	private void initAllNodeDepends() {
		for(GraphNode node : nodes) {
			addDepends(node.getId(), node.getDepends());
		}
	}
	
	public List<T> getNodes() {
		return nodes;
	}

	public void setNodes(List<T> nodes) {
		this.nodes = nodes;
	}

	public List<GraphEdge> getEdges() {
		return edges;
	}

	public void setEdges(List<GraphEdge> edges) {
		this.edges = edges;
	}

	public T getNode(String id) {
		for(T node : nodes) {
			if(node.getId().equals(id)) {
				return node;
			}
		}
		return null;
	}
	
	public T getRequiredNode(String id) {
		T n = getNode(id);
		if(n == null) 
			throw new IllegalArgumentException("not found Node by id:"+id);
		return n;
	}
	
	public void addNode(T n) {
		if(!nodes.contains(n)) 
			nodes.add(n);
	}
	
	/**
	 * 得到没有任何依赖的所有节点
	 * @return
	 */
	public List<T> getNoDependNodes() {
		List<T> result = new ArrayList<T>();
		for(T t : nodes) {
			if(CollectionUtils.isEmpty(t.getParents())) {
				result.add(t);
			}
		}
		return result;
	}
	
	/**
	 * 增加依赖
	 * @param id
	 * @param depends
	 */
	private void addDepends(String id,String depends) {
		if(StringUtils.isBlank(depends)) {
			return;
		}
		for(String depend : depends.split("[,\\s]+")){
			if(StringUtils.isNotBlank(depend)) {
				GraphEdge edge = new GraphEdge(depend,id);
				addEdge(edge);
			}
		}
	}
	
	/**
	 * 增加图的边
	 * @param id
	 * @param depends
	 */
	public void addEdge(GraphEdge edge) {
		if(edges.contains(edge)) {
			return;
		}
		
		GraphNode beginNode = getRequiredNode(edge.getBegin());
		GraphNode endNode = getRequiredNode(edge.getEnd());
		beginNode.addChild(endNode);
		endNode.addParent(beginNode);
		edges.add(edge);
	}
}
