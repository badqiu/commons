package com.github.rapid.common.util.tree;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.rapid.common.util.tree.Node;
import com.github.rapid.common.util.tree.NodeWrapper;
import com.github.rapid.common.util.tree.TreeCreator;


public class TreeCreatorTest extends Assert {
	
	public NodeWrapper<MenuBean> root = null;
	
	@Before
	public void setUp() {
		List<Node> nodes = new ArrayList<Node>();
		
		nodes.add(new MenuBean(1,-1));
		
		nodes.add(new MenuBean(4,1));
		nodes.add(new MenuBean(5,1));
		
		nodes.add(new MenuBean(2,1));
		nodes.add(new MenuBean(21,2));
		nodes.add(new MenuBean(22,2));
		nodes.add(new MenuBean(23,2));
		nodes.add(new MenuBean(221,21));
		nodes.add(new MenuBean(2211,221));
		nodes.add(new MenuBean(2212,221));
		
		nodes.add(new MenuBean(3,1));
		nodes.add(new MenuBean(31,3));
		nodes.add(new MenuBean(32,3));
		nodes.add(new MenuBean(33,3));
		nodes.add(new MenuBean(311,31));
		nodes.add(new MenuBean(312,31));
		nodes.add(new MenuBean(323,32));		

		
		TreeCreator creator = new TreeCreator();
		root = creator.createTree(nodes, 1);
		
		List<NodeWrapper<MenuBean>> childs = root.getChilds();
		for(NodeWrapper<MenuBean> child : childs) {
		}
	}
	
	@Test
	public void test_dump() {
		System.out.println(root.dump());
	}
	
	@Test
	public void test_delete() {
		System.out.println("delete before:"+root.dump());
		
		assertNotNull(root.findNode(22));
		System.out.println(root.deleteNode(22));
		
		System.out.println("delete after:"+root.dump());
		
		assertNull(root.findNode(22));
	}
	
	@Test
	public void test_is_leaf() {
		assertTrue(root.findNode(22).isLeaf());
		assertFalse(root.findNode(31).isLeaf());
		assertFalse(root.isLeaf());
	}
	
	static class MenuBean implements Node<Integer> {
		private int id;
		private int parentId;
		private int level;
		
		public MenuBean(){
		}
		
		public MenuBean(int id, int parentId) {
			super();
			this.id = id;
			this.parentId = parentId;
		}
		
		public MenuBean(int id, int parentId, int level) {
			super();
			this.id = id;
			this.parentId = parentId;
			this.level = level;
		}



		public Integer getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
		public Integer getParentId() {
			return parentId;
		}
		public void setParentId(int parentId) {
			this.parentId = parentId;
		}
		public int getOrderd() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}

		public int compareTo(Object o) {
			MenuBean other = (MenuBean)o;
			return new Integer(getOrderd()).compareTo(other.getOrderd());
		}
		
	}
}
