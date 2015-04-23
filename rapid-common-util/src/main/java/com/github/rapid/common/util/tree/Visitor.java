package com.github.rapid.common.util.tree;
/**
 * 树节点的访问器
 * 
 * @author badqiu
 *
 */
public interface Visitor {
	
	public void visit(NodeWrapper node);
	
}
