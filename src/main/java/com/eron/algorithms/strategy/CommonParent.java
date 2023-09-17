/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eron.algorithms.strategy;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eron.structures.BinaryTree;
import com.eron.structures.BinaryTree.Node;

/**
 * 树结构中 寻找两个节点的最近公共祖先
 *
 * @author ERON_AMD
 */
public class CommonParent {

    private static Logger log = LoggerFactory.getLogger(CommonParent.class);

    public static void main(String[] args) {
        // 构建一个二叉树 
        BinaryTree<Integer> tree = new BinaryTree<Integer>() {{
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

        Node<Integer> root = tree.root;
        Node<Integer> p = new Node<Integer>(44);
        Node<Integer> q = new Node<Integer>(3);
        getCommonParent(root, root, root);
        // 找到最近的公共祖先 遍历一边保存父节点信息 根据父节点 依次找上级,有相同的表示最早的公共祖先 

        // 镜像树的判断 
        boolean result = mirrorTreeCheck(root.left, root.right);
        log.info("检查结果 -> {}", result);

        // 获取树的最宽长度 就是树全部展开后可以达到的最大长度 
        maxLengthOfLeaf(root);
        log.info("最大宽度->{}", maxWidth);

        // 普通方法执行 
        maxWidth = 0;
        CommonParent commonSolution = new CommonParent();
        commonSolution.maxWidthOfTree(root);
        log.info("最后计算结果,最大宽度 -> {}", maxWidth);
    }

    //  将两个节点从下到上寻找父节点, 最近的公共父节点就是两者最近祖先
    private static Map<Integer, Node<Integer>> parents = new HashMap<Integer, Node<Integer>>();
    private static Set<Integer> visited = new HashSet<Integer>();

    public static void travel(Node<Integer> root) {
        if (root == null) return;

        if (root.left != null) {
            parents.put(root.left.value, root);
            travel(root.left);
        }

        if (root.right != null) {
            parents.put(root.right.value, root);
            travel(root.right);
        }
    }

    public static Node<Integer> getCommonParent(Node<Integer> root, Node<Integer> p, Node<Integer> q) {
        travel(root);  // 保存遍历信息 
        parents.entrySet().forEach(infos -> {
            Integer key = infos.getKey();
            Integer parent = infos.getValue().value;
            log.info("获取遍历信息 --> {} = {}", key, parent);
        });

        while (p != null) {
            visited.add(p.value);
            p = parents.get(p.value);
        }

        while (q != null) {
            if (visited.contains(q.value)) {
                return q;
            }

            q = parents.get(q.value);
        }

        return null;
    }

    public static boolean mirrorTreeCheck(Node<Integer> p, Node<Integer> q) {
        if (p == null && q == null) return true;
        if (p != null && q == null) return false;
        if (p == null && q != null) return false;

        return p.value == q.value && mirrorTreeCheck(p.left, q.right) && mirrorTreeCheck(p.right, q.left);
    }

    // 最长直径 
    private static int maxWidth = 0;  // 全局最长直径 

    public static int maxLengthOfLeaf(Node<Integer> root) {
        if (root == null) return 0;
        int maxLeft = maxDepth(root.left);
        int maxRight = maxDepth(root.right);
        int res = maxLeft + maxRight + 1;

        maxWidth = Math.max(maxWidth, res);
        return res;
    }

    public static int maxDepth(Node<Integer> root) {
        if (root == null) return 0;
        int left = maxDepth(root.left);
        int right = maxDepth(root.right);
        int res = Math.max(left, right) + 1;
        return res;
    }

    // 最长直径的改进版本  后序遍历 相当于自底向上
    public int maxWidthOfTree(Node<Integer> root) {
        if (root == null) return 0;
        int left = maxWidthOfTree(root.left);
        int right = maxWidthOfTree(root.right);
        int max = Math.max(left, right) + 1;

        maxWidth = Math.max(maxWidth, left + right + 1);
        return max;
    }
}










