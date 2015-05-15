package com.github.rapid.common.util.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 代表一张有方向的图,各个节点连接有方向
 * 
 * @author badqiu
 *
 */
public class Graph <NODE extends GraphNode> implements Serializable {

	private static final long serialVersionUID = -772398465741515697L;
	private static Logger logger = LoggerFactory.getLogger(Graph.class);
	
	private List<NODE> nodes = new ArrayList<NODE>();
	private List<GraphEdge> edges = new ArrayList<GraphEdge>();
	public Graph(){
	}

	/**
	 * 初始化图
	 */
	public void init() {
		init(false);
	}

	public void init(boolean ignoreNotFoundDependError) {
		initAllNodeDepends(ignoreNotFoundDependError);
	}
	
	private void initAllNodeDepends(boolean ignoreNotFoundDependError) {
		for(GraphNode node : nodes) {
			try {
				addDepends(node.getId(), node.getDepends());
			}catch(RuntimeException e) {
				if(ignoreNotFoundDependError) {
					//ignore
					logger.warn("not found depends"+node.getDepends()+" for node:"+node.getId(),e);
				}else {
					throw e;
				}
			}
		}
	}
	
	public List<NODE> getNodes() {
		return nodes;
	}

	public void setNodes(List<NODE> nodes) {
		this.nodes = nodes;
	}

	public List<GraphEdge> getEdges() {
		return edges;
	}

	public void setEdges(List<GraphEdge> edges) {
		this.edges = edges;
	}

	public NODE getNode(String id) {
		for(NODE node : nodes) {
			if(node.getId().equals(id)) {
				return node;
			}
		}
		return null;
	}
	
	public NODE getRequiredNode(String id) {
		NODE n = getNode(id);
		if(n == null) 
			throw new IllegalArgumentException("not found Node by id:"+id);
		return n;
	}
	
	public void addNode(NODE n) {
		if(nodes == null) {
			new ArrayList<NODE>();
		}
		if(!nodes.contains(n)) 
			nodes.add(n);
	}
	
	/**
	 * 得到没有任何依赖的所有节点
	 * @return
	 */
	public List<NODE> getNoDependNodes() {
		List<NODE> result = new ArrayList<NODE>();
		for(NODE t : nodes) {
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
		for(String depend : depends.trim().split("[,\\s]+")){
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
		if(edges == null) {
			edges = new ArrayList<GraphEdge>();
		}
		if(edges.contains(edge)) {
			return;
		}
		
		GraphNode beginNode = getRequiredNode(edge.getBegin());
		GraphNode endNode = getRequiredNode(edge.getEnd());
		beginNode.addChild(endNode);
		endNode.addParent(beginNode);
		edges.add(edge);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(NODE n : getNoDependNodes()) {
			sb.append(n.dump(0)+"\n");
		}
		return sb.toString();
	}
}
