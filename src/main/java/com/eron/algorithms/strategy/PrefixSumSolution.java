package com.eron.algorithms.strategy;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 前缀 提前处理 返回发解决
 *
 * @author wangy
 */
public class PrefixSumSolution {

    private static final Logger LOGGER = LoggerFactory.getLogger(PrefixSumSolution.class);

    public static void main(String[] args) {
        int[] arr2 = new int[]{1, 1, 1};
        PrefixSumSolution.subArrayOfSum(arr2, 2);

        int[] arr3 = new int[]{1, 1, 2, 1, 1};
        PrefixSumSolution.targetNumofOdd(arr3, 2);
    }

    private class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {
        }

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // 和为k的子数组  前缀和, 思路类似于 滑动窗口 
    private static void subArrayOfSum(int[] arr, int target) {
        int count = 0;  // 左右指针 合格数量 
        int n = arr.length;
        int[] preSum = new int[n];  // 前缀和 

        // 计算前缀和 双循环快速判断是否有条件形成 
        for (int x = 0; x < n; x++) {
            if (x == 0) {
                preSum[x] = arr[x];
                continue;
            }

            preSum[x] = preSum[x - 1] + arr[x];
        }

        LOGGER.info("打印前缀和 --> {}", printArray(preSum));

        // 数组本身等于 target 
        // 包含在逻辑中 
        for (int i = 0; i < n; i++) {
            if (preSum[i] == target) count++;
            for (int j = 0; j < i; j++) {
                int diff = preSum[i] - preSum[j];
                if (diff == target) count++;
            }
        }

        LOGGER.info("总数 --> {}", count);
    }

    // 前缀路径 树tree的实现 解决 
    // 给定一个树 找出树中 路径和为 target的 路径
    private static HashMap<Long, Integer> map = new HashMap<>();  // key 前缀和 value 出现的次数 
    private static int res = 0;  // 计数
    private static int targetSum = 20;  // 目标 

    private static void subTreeOfTargetSum(TreeNode root, int target) {
        // 整体思路 遍历树生成前缀和 数组,  再遍历数组 找出和满足的次数 
        map.put(0L, 1);
        getPrefixCount(root, 0L);
        LOGGER.info("计数结果 --> {}", res);
    }

    // 从根节点  到当前节点的  和, 不包括当前节点
    private static void getPrefixCount(TreeNode root, Long curSum) {
        if (root == null) {
            return;
        }

        curSum += root.val;
        res += map.getOrDefault(curSum - targetSum, 0);  // 更新存在多少可能满足条件 
        map.put(curSum, map.getOrDefault(curSum, 0) + 1);

        getPrefixCount(root.left, curSum);
        getPrefixCount(root.right, curSum);

        map.put(curSum, map.getOrDefault(curSum, 0) - 1);  // 还原
    }


    // 数组中  计数的数量 = 目标设定上数量
    // 优美子数组 子数组中的奇数个数 = k 
    private static void targetNumofOdd(int[] arr, int k) {
        int res = 0;
        // 计算数组 奇数偶数 
        int[] prefix = new int[arr.length];  // 前缀奇数和 

        for (int i = 0; i < arr.length; i++) {
            arr[i] = arr[i] % 2 == 0 ? 0 : 1;  // 设置奇偶性 数组变成是奇数还是偶数

            if (i == 0) {
                prefix[i] = arr[i] == 1 ? 1 : 0;
                continue;
            }
            prefix[i] = prefix[i - 1] + (arr[i] == 1 ? 1 : 0);
        }

        LOGGER.info("打印转换结果 --> {}", printArray(prefix));

        for (int i = 0; i < prefix.length; i++) {
            if (prefix[i] == k) res++;
            for (int j = 0; j < prefix.length; j++) {
                int diff = prefix[j] - prefix[i];
                if (diff == k) res++;
            }
        }

        LOGGER.info("最终计算结果 --> {}", res);
    }


    private static String printArray(int[] arr) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < arr.length; i++) {
            sb.append(String.valueOf(arr[i]) + ", ");
        }

        return sb.toString();
    }

}



