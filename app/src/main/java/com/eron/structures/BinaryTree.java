package com.eron.structures;

import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 自己实现搜索二叉树等树的结构 
// 树的创建和搜索 删除是有规律/固定规则的, 不能像 普通容器 没有插入规则
// ref : https://github.com/eugenp/tutorials/blob/master/data-structures/src/main/java/com/baeldung/tree/BinaryTree.java
public class BinaryTree<T extends Comparable<T>> implements Iterable<T>{ 
	
	private static Logger log = LoggerFactory.getLogger(BinaryTree.class);
	
	public static void main(String[] args) {
        // 整体性的测试使用 
	    BinaryTree<Integer> bt = new BinaryTree<>() {{  // 这里面的 T 代表了bst的id 复合结构中需要实现 
	        add(14);
	        add(23);
	        add(1);
	        add(56);
	        add(5);
	        add(10);
	        add(44);
	        add(90);
	        add(3);
	    }};
	    log.info("获取大小 --> {}", bt.getSize());  // 获取当前树存储key的数量
	    log.info("进行先序遍历 -------->");
	    bt.traversePreOrder(bt.root);
	    log.info("进行中序遍历 ------->");
	    bt.traverseInOrder(bt.root);
	    log.info("后序遍历=========");
	    bt.traversePostOrder(bt.root);
	    log.info("判断是否包含--> {}", bt.containsNode(10));  // 使用key包含函数 
	    log.info("层次遍历 ----->");
	    bt.travelLevelSeperation(bt.root);
	    
	    bt.delete(10);
	    log.info("判断是否删除 10 --> {}", bt.containsNode(10));
	    log.info("遍历一遍输出检查");
	    bt.traverseInOrder(bt.root);
    }
	
	public static class Node<T extends Comparable<T>> {
		public T value;
		public Node<T> left;
		public Node<T> right;
		
		public Node() {}
		public Node(T value) {
			this.value = value;
		}
	}
	
	public Node<T> root;  // 根节点 
	public void buildTree(List<T> nodes){
	    nodes.forEach(node -> this.add(node));
	}
	
	// 小的在左边, 大的在右边子树
	// 往树中添加节点 
	public void add(T value) {
	    root = addRecursive(root, value);
	}
	
	private Node<T> addRecursive(Node<T> current, T value) {
	    if (current == null) {  // 如果当前节点不村, 则直接添加  
	        return new Node<T>(value);
	    }
	    
	    // 如果新添加的值 小 = 左边  大 = 右边 
	    if (value.compareTo(current.value) < 0) {
	        current.left = addRecursive(current.left, value);
	    } else if (value.compareTo(current.value) > 0) {
	        current.right = addRecursive(current.right, value);
	    } else {
	        // value already exists
	        return current;
	    }

	    return current;
	}
	
	// 查找值  是否存在 
	public Boolean containsNode(T value) {
	    return containsNodeRecursive(root, value);
	}

	private Boolean containsNodeRecursive(Node<T> current, T value) {
	    if (current == null) {
	        return false;
	    } 
	    if (value.compareTo(current.value) == 0) {
	        return true;
	    } 
	    // 如果查找的值比当前节点的小 左子树查找, 如果大, 则右子树查找 
	    return value.compareTo(current.value) < 0 
	      ? containsNodeRecursive(current.left, value)
	      : containsNodeRecursive(current.right, value);
	    
	}
	
	// 删除节点
	public void delete(T value) {
	    root = deleteRecursive(root, value);
	}
	private Node<T> deleteRecursive(Node<T> current, T value) {
	    if (current == null) {
	        return null;
	    }
	    
	    // 找到了删除的节点 
	    if (value.compareTo(current.value) == 0) {
	        // Node to delete found
	        // ... code to delete the node will go here
	    	// 从树的之阵中移除当前节点, 并返回当前节点 
	    	if(current.left == null && current.right == null) {
	    		return null;
	    	}
	    	if(current.right == null) {
	    		return current.left;
	    	}
	    	if(current.left == null) {
	    		return current.right;
	    	}
	    	T smallestOfRight = findSmallestValue(current.right);
	    	current.value = smallestOfRight;
	    	current.right = deleteRecursive(current.right, smallestOfRight);
	    	
	    	return current;
	    }
	    if (value.compareTo(current.value) < 0) {
	        current.left = deleteRecursive(current.left, value);
	        return current;
	    }
	    current.right = deleteRecursive(current.right, value);
	    return current;
	    
	}
	
	// 查找root节点下的最小值 
	public T findSmallestValue(Node<T> root) {
		return root.left == null ? root.value : findSmallestValue(root.left);
	}
	
	// 获取树的所有节点数 
	public Integer getSize() {
		return getSizeRecursive(this.root);
	}
	private Integer getSizeRecursive(Node<T> current) {
		return current == null ? 0 : getSizeRecursive(current.left) + 1 + getSizeRecursive(current.right);
	}
	
	/**
	 * 遍历二叉树的实现
	 * @param root
	 */
	// 中序遍历 
	public void traverseInOrder(Node<T> root) {
		if (root != null) {
			traverseInOrder(root.left);
			log.info("traverseInOrder -> {}", root.value);
			traverseInOrder(root.right);
		}
	}
	
	// 先序遍历
	public void traversePreOrder(Node<T> root) {
		if(root != null) {
			log.info("traversePreOrder -> {}", root.value);
			traversePreOrder(root.left);
			traversePreOrder(root.right);
		}
	}
	
	// 后序遍历
	public void traversePostOrder(Node<T> root) {
		if(root != null) {
			traversePostOrder(root.left);
			traversePostOrder(root.right);
			log.info("traversePostOrder -> {}", root.value);
		}
	}
	
	// 层次遍历  按照顺序一层一层的遍历 
	public void traversesLevelOrder(Node<T> root) {
		if(root == null) {
			return;
		}
		Queue<Node<T>> queue = new LinkedBlockingDeque<>();
		queue.add(root);
		
		while (!queue.isEmpty()) {  // 如果需要指导每层的详细呢？ 提供每层的索引 
			Node<T> current = queue.poll();  // remove 也可以
			log.info("traversesLevelOrder -> {}", current.value);
			
			if(current.left != null) {
				queue.add(current.left);
			}
			if(current.right != null) {
				queue.add(current.right);
			}
			
		}
	}
	// 层次分别遍历 每一层单独list 每层都输出一个list 
	public void travelLevelSeperation(Node<T> root) {
	    if(root == null) {
	        return;
	    }
	    Queue<Node<T>> queue = new LinkedBlockingQueue<>();
	    queue.add(root);
	    // List<List<Node<T>>> travels = new LinkedList<>();
	    int curLevel = 0; // 当前处于第几层 
	    int levelCount = 1; // 当前遍历层的数量 
	    
	    while(!queue.isEmpty()) {
	        int curCount = levelCount; 
	        levelCount = 0;
	        log.info("当前遍历树 {} 层, {} 个节点 ", curLevel, curCount);
	        
	        while(curCount > 0) {  // 遍历当前层 
	            Node<T> x = queue.poll();
	            log.info("取出当前层 {} = {}", curCount, x.value);
	            if(x.left != null) {
	                queue.add(x.left);
	                levelCount++;
	            }
	            if(x.right != null) {
	                queue.add(x.right);
	                levelCount++;
	            }
	            
	            curCount--;
	        }
	        curLevel++;
	    }
	}
	
	/**
	 *  不使用递归  的遍历树方法 
	 */
	public void traverseInOrderWithoutRecursion() {  // 中序遍历  中根遍历 
        Stack<Node<T>> stack = new Stack<>();
        Node<T> current = root;  // 根节点

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            // 如果 到了最左边的叶子节点 则取出stack top 
            Node<T> top = stack.pop();
            log.info("traverseInOrderWithoutRecursion -> {}", top.value);
            
            current = top.right;
        }
    }

    public void traversePreOrderWithoutRecursion() {  // 先序遍历  就是先根遍历 
        Stack<Node<T>> stack = new Stack<>();
        Node<T> current = root;
        stack.push(root);

        while (current != null && !stack.isEmpty()) {
            current = stack.pop();
            log.info("traversePreOrderWithoutRecursion -> {}", current.value);

            if (current.right != null) {
                stack.push(current.right);
            }

            if (current.left != null) {
                stack.push(current.left);
            }
        }
    }
    
    public void traversePostOrderWithoutRecursion() {  // 后序遍历 
        Stack<Node<T>> stack = new Stack<>();
        Node<T> prev = root;
        Node<T> current = root;
        stack.push(root);

        while (current != null && !stack.isEmpty()) {
            current = stack.peek();
            boolean hasChild = (current.left != null || current.right != null);
            boolean isPrevLastChild = (prev == current.right || (prev == current.left && current.right == null));

            if (!hasChild || isPrevLastChild) {
                current = stack.pop();
                log.info("traversePreOrderWithoutRecursion -> {}", current.value);
                prev = current;
            } else {
                if (current.right != null) {
                    stack.push(current.right);
                }
                if (current.left != null) {
                    stack.push(current.left);
                }
            }
        }   
    }
    
	@Override
	public Iterator<T> iterator() {
		// 实现迭代器 
				
		Iterator<T> iterator = new Iterator<T>() {  // 不实现移除功能 
			
			Stack<Node<T>> stack = new Stack<>();
			Node<T> current = root;  // 获取根引用   并在迭代中指向下一个节点 
			
			@Override 
			public boolean hasNext() {
				return current != null || !stack.isEmpty();
			}
			
			@Override 
			public T next() {  // 中根遍历 
				
				while (current != null) {
	                stack.push(current);
	                current = current.left;
	            }
				
	            // 如果 到了最左边的叶子节点 则取出stack top 
	            Node<T> top = stack.pop();
	            log.info("traverseInOrderWithoutRecursion -> {}", top.value);
	            current = top.right;
	            
				return top.value;
			}
			
		}; 
		
		return iterator;
		
	}
    
	
}













