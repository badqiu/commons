package com.github.rapid.common.util.tree2;

import static org.junit.Assert.*;

import org.junit.Test;

public class TreeTest {

	public static class TreeNodeImpl extends TreeNode<Integer> {

		public TreeNodeImpl() {
			super();
		}
		
		public TreeNodeImpl(Integer id, Integer parentId) {
			super(id, parentId);
		}
		
	}
	@Test
	public void test() {
		Tree tree = new Tree();
		tree.addNode(new TreeNodeImpl(100,null));
		tree.addNode(new TreeNodeImpl(1,100));
		tree.addNode(new TreeNodeImpl(1,100));
		
		tree.init(100);
		
		assertNotNull(tree.getRoot());
		assertEquals(tree.getRoot().getId(),100);
	}

}
