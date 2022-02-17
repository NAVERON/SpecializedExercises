/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.structs;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 树的结构和遍历
 * 需要尝试实现泛型的树结构     查看源代码
 * @author ERON_AMD
 */
public class TreeStructTest {

    private static final Logger log = LoggerFactory.getLogger(TreeStructTest.class);
    
    private static final List<Integer> numbers = new ArrayList<Integer>();
    private static final Queue<Node> queue = new LinkedList<Node>();

    public static void main(String[] args) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Node<Integer> root = new Node<>(-1);
        createTree(root, arr);
        log.info("创建tree完毕 ! ");
        
        TreeStructTest.numbers.clear();
        preOrderTravel(root);
        log.info("输出先序遍历结果 : {}", TreeStructTest.numbers);
        
        TreeStructTest.numbers.clear();
        midOrderTravel(root);
        log.info("输出中序遍历结果 : {}", TreeStructTest.numbers);
        
        TreeStructTest.numbers.clear();
        postOrderTravel(root);
        log.info("输出后续遍历结果 : {}", TreeStructTest.numbers);
        
        
        numbers.clear();
        log.info("开始广度优先遍历");
        traveBFS(root);
        log.info("输出广度优先遍历结果 : {}", numbers);
    }

    public static void createTree(Node<Integer> rootNode, int[] arr) {
        rootNode.data = arr.length > 0 ? arr[0] : -1;
        for (int i = 1; i < arr.length; i++) {
            insertDataToTree(rootNode, arr[i]);
        }
    }

    public static void insertDataToTree(Node<Integer> rootNode, int data) {
        
        if (data >= rootNode.data) {
            if (rootNode.getLeftChild() == null) {
                rootNode.setLeftChild(new Node(data));
                return;
            }
            insertDataToTree(rootNode.getLeftChild(), data);  // 从根节点开始找，合适的位置插入
        } else {
            if (rootNode.getRightChile() == null) {
                rootNode.setRightChile(new Node(data));
                return;
            }
            insertDataToTree(rootNode.getRightChile(), data);
        }

    }

    // 广度优先遍历
    public static void traveBFS(Node<Integer> rootNode) {  // 前提  二叉树， 多叉树稍微有点变化
        if(rootNode == null){
            return;
        }
        
        queue.add(rootNode);
        while(!queue.isEmpty()){
            Node<Integer> node = queue.remove();
            numbers.add(node.getData());
            
            if(node.getLeftChild() != null){
                queue.add(node.getLeftChild());
            }
            
            if(node.getRightChile() != null){
                queue.add(node.getRightChile());
            }
        }
        
    }

    // 先序遍历
    public static void preOrderTravel(Node<Integer> node) {
        // 不需要判断node isnull， 上一个循环已经判断
        /// 先序遍历
        TreeStructTest.numbers.add(node.getData());
        
        if (node.getLeftChild() != null) {
            preOrderTravel(node.getLeftChild());
        }
        if (node.getRightChile() != null) {
            preOrderTravel(node.getRightChile());
        }
    }
    
    //先序遍历非递归方法
    public static void preOrderTravelWhile(Node<Integer> root){
        List<Integer> outNumbers = new ArrayList();
        Stack<Node> stack = new Stack<>();
        Node<Integer> curNode = root;
        while(curNode != null || !stack.isEmpty()){  // 两者满足其一即可循环
            if(curNode != null){
                outNumbers.add(curNode.data);
                stack.push(curNode);
                curNode = curNode.leftChild;  // 永远用左child先遍历， 所以后面如果当前节点是null，从栈中取右child
            }else{
                Node node = stack.pop();
                curNode = curNode.rightChild;
            }
        }
        
        log.info("输出先序遍历 : {}", outNumbers);
    }

    // 中序遍历
    public static void midOrderTravel(Node<Integer> node) {
        
        if (node.getLeftChild() != null) {
            midOrderTravel(node.getLeftChild());
        }
        
        TreeStructTest.numbers.add(node.getData());

        if (node.getRightChile() != null) {
            midOrderTravel(node.getRightChile());
        }
    }

    // 后序遍历
    public static void postOrderTravel(Node<Integer> node) {

        
        if (node.getLeftChild() != null) {
            postOrderTravel(node.getLeftChild());
        }
        if (node.getRightChile() != null) {
            postOrderTravel(node.getRightChile());
        }
        
        TreeStructTest.numbers.add(node.getData());
    }

    public static class Node<T> {

        private T data;
        private Node leftChild = null;
        private Node rightChild = null;

        public Node(T data) {
            this.data = data;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public Node getLeftChild() {
            return leftChild;
        }

        public void setLeftChild(Node leftChild) {
            this.leftChild = leftChild;
        }

        public Node getRightChile() {
            return rightChild;
        }

        public void setRightChile(Node rightChile) {
            this.rightChild = rightChile;
        }

    }

}
