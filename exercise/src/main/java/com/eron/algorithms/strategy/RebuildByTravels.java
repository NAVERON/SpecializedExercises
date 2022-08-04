package com.eron.algorithms.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 根据前序和中序遍历还原二叉树 
 * @author eron 
 * 
 */
public class RebuildByTravels {
    private static final Logger log = LoggerFactory.getLogger(RebuildByTravels.class);
    public static void main(String[] args) {
        // 
    }
    
    private static class TreeNode{
        int value;
        TreeNode left;
        TreeNode right;
        public TreeNode(int value) {
            this.value = value;
        }
    }
    
    public TreeNode rebuildTree(int[] preorder, int preStart, int preEnd, 
            int[] inorder, int inStart, int inEnd) {
        // 前序位置，寻找左右子树的索引
        if (preStart > preEnd) {
            return null;
        }
        int rootVal = preorder[preStart];
        int index = 0;
        for (int i = inStart; i <= inEnd; i++) {
            if (inorder[i] == rootVal) {
                index = i;
                break;
            }
        }
        int leftSize = index - inStart;
        TreeNode root = new TreeNode(rootVal);

        // 递归构造左右子树
        root.left = this.rebuildTree(preorder, preStart + 1, preStart + leftSize,
                          inorder, inStart, index - 1);
        root.right = this.rebuildTree(preorder, preStart + leftSize + 1, preEnd,
                           inorder, index + 1, inEnd);
        return root;
    }
    
    // 求树中路径和最大的值 
    int maxRes = Integer.MIN_VALUE;
    public int maxPathOfTree(TreeNode root) {
        if(root == null) return 0;
        int left = Math.max(0, this.maxPathOfTree(root.left));  // 如果分支负数  不如 +0 
        int right = Math.max(0, this.maxPathOfTree(root.right));  // 如果需要所有到叶子的节点 这里需要最小值 Integer.min_value
        maxRes = Math.max(maxRes, left + right + root.value);
        return Math.max(left, right) + root.value;
    }
    
    // 求二插搜索树中第k小的元素 搜索树中序遍历就是按照大小顺序获取 
    int res = 0; int rank = 0;
    public void getMinKNum(TreeNode root, int k) {
        if(root == null) return ;
        this.getMinKNum(root.left, k);
        rank++;
        if(rank == k) {
            res = root.value;
            return;
        }
        this.getMinKNum(root.right, k);
    }
}













