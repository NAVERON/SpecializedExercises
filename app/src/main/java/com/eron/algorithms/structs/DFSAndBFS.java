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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  图的遍历  深度遍历和广度优先遍历 
 * 深度优先使用栈结构, 因为需要回退,   广度优先需要使用队列, 因为需要
 * @author ERON_AMD
 */
public class DFSAndBFS {
    // 深度优先   广度优先
    // 栈    队列
    
    private static Logger log = LoggerFactory.getLogger(DFSAndBFS.class);
    
    public static void main(String[] args) {
        //
    }
    
    public static void travelDFS(){
        // 深度优先
    }
    
    public static void travelBFS(){
        // 广度优先 
    }
    
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> ret = new ArrayList<List<Integer>>();
        if (root == null) {
            return ret;
        }

        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            List<Integer> level = new ArrayList<Integer>();
            int currentLevelSize = queue.size();
            for (int i = 1; i <= currentLevelSize; ++i) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            ret.add(level);
        }
        
        return ret;
    }
    
    public class TreeNode{
        public int val;
        public TreeNode left = null;
        public TreeNode right = null;
    }
}
