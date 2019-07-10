/*package com.learn;

import java.util.ArrayList;
import java.util.List;

*//**
 * avl树实现
 * @author chenyunh
 * @since 2019年4月23日
 *//*
public class AVLNode {
	public int key;

	Object value;

	public AVLNode left;

	public AVLNode right;

	public int height = -1;

	public AVLNode(int key, AVLNode left, AVLNode right) {
		this.key = key;
		this.height = 1;
		this.left = left;
		this.right = right;
	}

	public AVLNode(int key) {
		this(key, null, null);
	}

	public static int height(AVLNode tree) {
		return null == tree ? 0 : tree.height;
	}

	public static AVLNode put(AVLNode tree, int key) {
		if (tree == null) {
			tree = new AVLNode(key);
			return tree;
		}
		if (key < tree.key) {
			tree.left = put(tree.left, key);
			if (AVLNode.height(tree.left) - AVLNode.height(tree.right) == 2) {
				if (key < tree.left.key) {
					tree = LLrotate(tree);
				} else {
					tree = LRrotate(tree);
				}
			}
		} else if (key > tree.key) {
			tree.right = put(tree.right, key);
			if (AVLNode.height(tree.right) - AVLNode.height(tree.left) == 2) {
				if (key > tree.right.key) {
					tree = RRrotate(tree);
				} else {
					tree = RLrotate(tree);
				}
			}
		} else {
			throw new RuntimeException("不能像avl树中添加已存在的值" + key + "！");
		}
		tree.height = Math.max(AVLNode.height(tree.left), AVLNode.height(tree.right)) + 1;
		return tree;
	}

	public AVLNode put(int key) {
		return put(this, key);
	}

	public AVLNode remove(AVLNode tree, int key) {
		if (tree == null) {
			return null;
		}
		if (key < tree.key) {
			tree.left = remove(tree.left, key);
			if (AVLNode.height(tree.right) - AVLNode.height(tree.left) == 2) {
				AVLNode right = tree.right;
				if (AVLNode.height(right.left) > AVLNode.height(right.right)) {
					tree = RLrotate(tree);
				} else {
					tree = RRrotate(tree);
				}
			}
		} else if (key > tree.key) {
			tree.right = remove(tree.right, key);
			if (AVLNode.height(tree.left) - AVLNode.height(tree.right) == 2) {
				AVLNode left = tree.left;
				if (AVLNode.height(left.left) > AVLNode.height(left.right)) {
					tree = LRrotate(tree);
				} else {
					tree = LLrotate(tree);
				}
			}
		} else {
			if (tree.left != null && tree.right != null) {
				if (AVLNode.height(tree.left) > AVLNode.height(tree.right)) {
					AVLNode max = maxKey(tree.left);
					tree.key = max.key;
					tree.value = max.value;
					tree.left = remove(tree.left, max.key);
				} else {
					AVLNode max = minKey(tree.right);
					tree.key = max.key;
					tree.value = max.value;
					tree.right = remove(tree.right, max.key);
				}

			} else {
				tree = tree.left == null ? tree.right : tree.left;
			}
		}
		return tree;
	}

	private AVLNode maxKey(AVLNode tree) {
		while (tree.right != null) {
			tree = tree.right;
		}
		return tree;
	}

	public AVLNode maxKey() {
		return maxKey(this);
	}
	
	private AVLNode minKey(AVLNode tree) {
		while (tree.left != null) {
			tree = tree.left;
		}
		return tree;
	}

	public AVLNode minKey() {
		return minKey(this);
	}

	public AVLNode remove(int key) {
		return remove(this, key);
	}

	*//**
	 * 左左旋转
	 * @param tree
	 *//*
	public static AVLNode LLrotate(AVLNode tree) {
		AVLNode left = tree.left;
		tree.left = left.right;
		left.right = tree;
		tree.height = Math.max(AVLNode.height(tree.left), AVLNode.height(tree.right)) + 1;
		left.height = Math.max(AVLNode.height(left.left), tree.height) + 1;
		return left;
	}

	*//**
	 * 右右旋转
	 * @param tree
	 *//*
	public static AVLNode RRrotate(AVLNode tree) {
		AVLNode right = tree.right;
		tree.right = right.left;
		right.left = tree;
		tree.height = Math.max(AVLNode.height(tree.left), AVLNode.height(tree.right)) + 1;
		right.height = Math.max(AVLNode.height(right.right), tree.height) + 1;
		return right;
	}

	public static AVLNode RLrotate(AVLNode tree) {
		tree.right = LLrotate(tree.right);
		return tree = RRrotate(tree);
	}

	public static AVLNode LRrotate(AVLNode tree) {
		tree.left = RRrotate(tree.left);
		return tree = LLrotate(tree);
	}

	private List<Integer> firstFS(AVLNode tree, List<Integer> result) {
		if (tree != null) {
			result.add(tree.key);
			firstFS(tree.left, result);
			firstFS(tree.right, result);
		}
		return result;
	}

	public List<Integer> firstFS() {
		List<Integer> result = new ArrayList<>();
		return firstFS(this, result);
	}

	private List<Integer> midFS(AVLNode tree, List<Integer> result) {
		if (tree != null) {
			midFS(tree.left, result);
			result.add(tree.key);
			midFS(tree.right, result);
		}
		return result;
	}

	public List<Integer> midFS() {
		List<Integer> result = new ArrayList<>();
		return midFS(this, result);
	}

	private List<Integer> lastFS(AVLNode tree, List<Integer> result) {
		if (tree != null) {
			lastFS(tree.left, result);
			lastFS(tree.right, result);
			result.add(tree.key);
		}
		return result;
	}

	public List<Integer> lastFS() {
		List<Integer> result = new ArrayList<>();
		return lastFS(this, result);
	}

	public static void main(String[] args) {
		AVLNode tree = new AVLNode(3);
		System.out.println(tree.midFS());
		tree = tree.put(2);
		System.out.println(tree.midFS());
		tree = tree.put(1);
		System.out.println(tree.midFS());
		tree = tree.put(4);
		System.out.println(tree.midFS());
		tree = tree.put(5);
		System.out.println(tree.midFS());
		tree = tree.put(6);
		System.out.println(tree.midFS());
		tree = tree.put(7);
		System.out.println(tree.midFS());
		tree = tree.put(16);
		System.out.println(tree.midFS());
		tree = tree.put(15);
		System.out.println(tree.midFS());
		tree = tree.put(14);
		System.out.println(tree.midFS());
		tree = tree.put(13);
		System.out.println(tree.midFS());
		tree = tree.put(12);
		System.out.println(tree.midFS());
		tree = tree.put(11);
		System.out.println(tree.midFS());
		tree = tree.put(10);
		System.out.println(tree.midFS());
		tree = tree.put(8);
		System.out.println(tree.midFS());
		tree = tree.put(9);
		System.out.println(tree.firstFS());
		System.out.println(tree.midFS());
		System.out.println(tree.lastFS());
		System.out.println(AVLNode.height(tree));
		System.out.println(tree.maxKey().key);
		tree.remove(8).remove(14);
		System.out.println(tree.remove(4).midFS());
		
	}

}
*/