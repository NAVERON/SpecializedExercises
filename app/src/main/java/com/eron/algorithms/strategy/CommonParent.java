/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eron.algorithms.strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  树结构中 寻找两个节点的最近公共祖先
 * @author ERON_AMD
 */
public class CommonParent {
    
    private static Logger log = LoggerFactory.getLogger(CommonParent.class);
    
    public static void main(String[] args) {
        TreeNode root = null;
        TreeNode p = null;
        TreeNode q = null;
        getCommonParent(root, root, root);
        
        // 镜像树的判断 
        boolean result = mirrorTreeCheck(root.leftNode, root.rightNode);
        log.info("检查结果 -> {}", result);
    }
    
    //  将两个节点从下到上寻找父节点, 最近的公共父节点就是两者最近祖先
    public static Map<Integer, TreeNode> parents = new HashMap<Integer, TreeNode>();
    public static Set<Integer> visited = new HashSet<Integer>();
    
    public static void travel(TreeNode root){
        if(root == null) return;
        
        if(root.leftNode != null){
            parents.put(root.leftNode.val, root);
            travel(root.leftNode);
        }
        
        if(root.rightNode != null){
            parents.put(root.rightNode.val, root);
            travel(root.rightNode);
        }
    }
    
    public static TreeNode getCommonParent(TreeNode root, TreeNode p, TreeNode q){
        travel(root);
        while(p != null){
            visited.add(p.val);
            p = parents.get(p.val);
        }
        
        while(q != null){
            if(visited.contains(q.val)){
                return q;
            }
            
            q = parents.get(q.val);
        }
        
        return null;
    }
    
    
    public class TreeNode{
        public int val;
        
        public TreeNode leftNode;
        public TreeNode rightNode;
        
    }
    
    public static boolean mirrorTreeCheck(TreeNode p, TreeNode q) {
        if( p == null && q == null) return true;
        if(p != null && q == null) return false;
        if(p == null && q != null) return false;

        return p.val == q.val && mirrorTreeCheck(p.leftNode, q.rightNode) && mirrorTreeCheck(p.rightNode, q.leftNode);
    }
    
}










