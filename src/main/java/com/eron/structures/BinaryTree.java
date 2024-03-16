package com.eron.structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自己实现搜索二叉树等树的结构 树的创建和搜索
 * 删除是有规律/固定规则的, 不能像 普通容器 没有插入规则
 * 参考 <link ref = "https://github.com/eugenp/tutorials/blob/master/data-structures/src/main/java/com/baeldung/tree/BinaryTree.java">
 *
 * @param <T> T 泛型类型
 */
public class BinaryTree<T extends Comparable<T>> implements Iterable<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BinaryTree.class);

    public static void main(String[] args) {
        // 整体性的测试使用 元素应具备唯一性
        List<Integer> nodes = new LinkedList<>() {{
            add(14);
            add(23);
            add(-1);
            add(56);
            add(-5);
            add(10);
            add(44);
            add(90);
            add(3);
        }};
        List<Integer> travelRes = new ArrayList<>();

        // 创建二叉树
        BinaryTree<Integer> bt = BinaryTree.buildTree(nodes);
        bt.add(100);
        LOGGER.info("获取大小 --> {}", bt.getSize());  // 获取当前树存储key的数量
        LOGGER.info("树中是否存在某个元素 --> {}, {}", bt.containsNode(11), bt.containsNode(0));

        travelRes.clear();
        bt.traversePreOrder(bt.getRoot(), travelRes);
        LOGGER.info("进行先序遍历 --------> {}", travelRes);
        travelRes.clear();
        bt.traversePreOrderWithoutRecursion(bt.getRoot(), travelRes);
        LOGGER.info("不使用递归, 先序遍历 ----> {}", travelRes);

        travelRes.clear();
        bt.traverseInOrder(bt.getRoot(), travelRes);
        LOGGER.info("进行中序遍历 -------> {}", travelRes);
        travelRes.clear();
        bt.traverseInOrderWithoutRecursion(bt.getRoot(), travelRes);
        LOGGER.info("不使用递归 中序遍历 ----> {}", travelRes);

        travelRes.clear();
        bt.traversePostOrder(bt.getRoot(), travelRes);
        LOGGER.info("后序遍历 -------> {}", travelRes);
        travelRes.clear();
        bt.traversePostOrderWithoutRecursion(bt.getRoot(), travelRes);
        LOGGER.info("不使用递归 后序遍历 ----> {}", travelRes);

        travelRes.clear();
        bt.travelByLevel(bt.getRoot(), travelRes);
        LOGGER.info("层次遍历 -----> {}", travelRes);

        int delNum = 10;
        LOGGER.info("删除操作, 当前是否包含待元素 --> {}", bt.containsNode(delNum));
        bt.delete(delNum);
        LOGGER.info("判断是否删除 --> {}", bt.containsNode(delNum));

        // 迭代器
        travelRes.clear();
        Iterator<Integer> it = bt.iterator();
        while (it.hasNext()) {
            travelRes.add(it.next());
        }
        LOGGER.info("迭代器 结果 (等同不使用递归的中序遍历) ---> {}", travelRes);
        travelRes.clear();
    }

    private static class BinaryTreeNode<T extends Comparable<T>> {

        public T value;
        public BinaryTreeNode<T> left;  // 简略 get/set方法
        public BinaryTreeNode<T> right;

        public BinaryTreeNode() {
        }

        public BinaryTreeNode(T value) {
            this.value = value;
        }
    }

    private BinaryTreeNode<T> root;  // 根节点

    public BinaryTreeNode<T> getRoot() {
        if (Objects.isNull(root)) {
            throw new IllegalStateException("binary tree root is not exists");
        }

        return root;
    }
    public static <T extends Comparable<T>> BinaryTree<T> buildTree(List<T> nodes) {
        BinaryTree<T> bt = new BinaryTree<T>();
        nodes.forEach(bt::add);

        return bt;
    }

    // 小的在左边, 大的在右边子树: 往树中添加节点
    public void add(T value) {
        root = addRecursive(root, value);
    }

    private BinaryTreeNode<T> addRecursive(BinaryTreeNode<T> current, T value) {
        if (current == null) {  // 如果当前节点不村, 则直接添加
            return new BinaryTreeNode<>(value);
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

    // 查找值 是否存在
    public Boolean containsNode(T value) {
        return this.containsNodeRecursive(root, value);
    }

    private Boolean containsNodeRecursive(BinaryTreeNode<T> current, T value) {
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

    private BinaryTreeNode<T> deleteRecursive(BinaryTreeNode<T> current, T value) {
        if (current == null) {
            return null;
        }

        // 找到了删除的节点
        if (value.compareTo(current.value) == 0) {
            // Node to delete found, code to delete the node will go here
            // 从树的之阵中移除当前节点, 并返回当前节点
            if (current.left == null && current.right == null) {
                return null;
            }
            if (current.right == null) {
                return current.left;
            }
            if (current.left == null) {
                return current.right;
            }

            T smallestOfRight = findSmallestValue(current.right);
            current.value = smallestOfRight;
            current.right = deleteRecursive(current.right, smallestOfRight);

            return current;
        } else if (value.compareTo(current.value) < 0) {
            current.left = deleteRecursive(current.left, value);
            return current;
        } else {
            current.right = deleteRecursive(current.right, value);
            return current;
        }
    }

    // 查找root节点下的最小值
    private T findSmallestValue(BinaryTreeNode<T> root) {
        return root.left == null ? root.value : findSmallestValue(root.left);
    }

    // 获取树的所有节点数
    public Integer getSize() {
        return getSizeRecursive(this.root);
    }

    private Integer getSizeRecursive(BinaryTreeNode<T> current) {
        return current == null ? 0
            : getSizeRecursive(current.left) + 1 + getSizeRecursive(current.right);
    }

    // 中序遍历
    public void traverseInOrder(BinaryTreeNode<T> root, List<T> travelList) {
        if (root != null) {
            traverseInOrder(root.left, travelList);
            travelList.add(root.value);  // 获取值
            traverseInOrder(root.right, travelList);
        }
    }

    // 先序遍历
    public void traversePreOrder(BinaryTreeNode<T> root, List<T> travelList) {
        if (root != null) {
            travelList.add(root.value);
            traversePreOrder(root.left, travelList);
            traversePreOrder(root.right, travelList);
        }
    }

    // 后序遍历
    public void traversePostOrder(BinaryTreeNode<T> root, List<T> travelList) {
        if (root != null) {
            traversePostOrder(root.left, travelList);
            traversePostOrder(root.right, travelList);
            travelList.add(root.value);
        }
    }

    // 层序遍历
    public void travelByLevel(BinaryTreeNode<T> root, List<T> travelList) {
        if (Objects.isNull(root)) {
            return;
        }
        Queue<BinaryTreeNode<T>> q = new LinkedList<>();
        q.add(root);
        int depth = 0;
        while (!q.isEmpty()) {
            int sz = q.size();  // 当前蹭的数量
            LOGGER.info("当前遍历完成到第 {} 层, 本层 {} 个元素", depth, sz);

            while (sz > 0) {
                BinaryTreeNode<T> node = q.remove();
                travelList.add(node.value);
                if (node.left != null) {
                    q.add(node.left);
                }
                if (node.right != null) {
                    q.add(node.right);
                }

                sz--;
            }

            depth++;  // 当前层遍历完成 深度+1
        }
    }

    // 不使用递归 先根遍历
    public void traversePreOrderWithoutRecursion(BinaryTreeNode<T> current, List<T> travelList) {
        if (Objects.isNull(current)) {
            return;
        }

        Stack<BinaryTreeNode<T>> stack = new Stack<>();
//        stack.push(current);
//        while (!stack.isEmpty()) {
//            current = stack.pop();
//            travelList.add(current.value);
//
//            if (current.right != null) {
//                stack.push(current.right);
//            }
//
//            if (current.left != null) {
//                stack.push(current.left);
//            }
//        }

        BinaryTreeNode<T> node = current;
        while (!stack.isEmpty() || node != null) {
            while (node != null) {
                travelList.add(node.value);
                stack.push(node);
                node = node.left;
            }

            node = stack.pop();
            node = node.right;
        }
    }

    // 不使用递归 中序遍历
    public void traverseInOrderWithoutRecursion(BinaryTreeNode<T> current, List<T> travelList) {
        if (Objects.isNull(current)) {
            return;
        }

        Stack<BinaryTreeNode<T>> stack = new Stack<>();
        BinaryTreeNode<T> node = current;
        while (!stack.isEmpty() || node != null) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }

            node = stack.pop();
            travelList.add(node.value);
            node = node.right;
        }
    }

    public void traversePostOrderWithoutRecursion(BinaryTreeNode<T> current, List<T> travelList) {
        if (Objects.isNull(current)) {
            return;
        }

        Stack<BinaryTreeNode<T>> stack = new Stack<>();
        Stack<BinaryTreeNode<T>> reverseStack = new Stack<>();
        BinaryTreeNode<T> node = current;
        while (!stack.isEmpty() || node != null) {
            while (node != null) {
                reverseStack.push(node);  // 先序的反, 根, 左, 右; 所以入栈完全反向
                stack.push(node);
                node = node.right;
            }

            node = stack.pop();
            node = node.left;
        }

        while (!reverseStack.isEmpty()) {
            node = reverseStack.pop();
            travelList.add(node.value);
        }
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() { // 自定义迭代器
            private BinaryTreeNode<T> current = getRoot();  // 获取根引用 并在迭代中指向下一个节点

            private Stack<BinaryTreeNode<T>> stack = new Stack<>();

            @Override
            public boolean hasNext() {
                return current != null || !stack.isEmpty();
            }

            @Override
            public T next() {
                while (current != null) {
                    stack.push(current);
                    current = current.left;
                }

                current = stack.pop();
                T value = current.value;
                current = current.right;

                return value;
            }
        };
    }

    // 检查 树 是否为镜像树
    public boolean isMirrorTree() {
        BinaryTreeNode<T> root = this.getRoot();
        return this.checkIsMirrorNode(root.left, root.right);
    }

    // 迭代检查器
    private boolean checkIsMirrorNode(BinaryTreeNode<T> p, BinaryTreeNode<T> q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }

        return p.value == q.value && this.checkIsMirrorNode(p.right, q.left)
            && this.checkIsMirrorNode(p.left, q.right);
    }

    // 树最大叶子长度 --> 展开树最长是多长
    public long maxLengthOfLeaf() {
        return 1 + this.maxDepth(this.getRoot().left) + this.maxDepth(this.getRoot().right);
    }

    private long maxDepth(BinaryTreeNode<T> node) {
        if (node == null) return 0;
        return Math.max(this.maxDepth(node.left), this.maxDepth(node.right)) + 1;
    }
}













