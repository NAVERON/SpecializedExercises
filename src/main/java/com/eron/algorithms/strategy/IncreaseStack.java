package com.eron.algorithms.strategy;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 单调栈 解决最值问题
 *
 * @author wangy
 */
public class IncreaseStack {

    private static final Logger LOGGER = LoggerFactory.getLogger(IncreaseStack.class);

    public static void main(String[] args) {

        // 柱形图 最大矩形面积 
        int[] arr1 = new int[]{2, 1, 5, 6, 2, 3};
        IncreaseStack.maxRectangleArea(arr1);


    }

    /**
     * 计算柱形图 可以围成的最大矩形面积
     *
     * @return 最大面积值
     */
    private static int maxRectangleArea(int[] arr) {

        int n = arr.length;
        Deque<Integer> stack = new ArrayDeque<>();  // 双端队列  保存索引, 不是实际的值 
        int ans = 0;

        for (int i = 0; i < n; i++) {
            if (stack.isEmpty() || arr[stack.peek()] <= arr[i]) {
                stack.push(i);
                continue;
            }

            while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
                ans = Math.max(ans, (i - stack.peek()) * arr[stack.peek()]);
                stack.pop();  // 移除 
            }

        }
        LOGGER.info("最大矩形面积 : {}", ans);

        return ans;
    }

    // 二维矩阵面积的计算  每一层按照柱状图 计算面积即可 
    private static int matrixArrayRectangle(int[][] arr2) {
        int row = arr2.length;
        int col = arr2[0].length;  // 行 列
        // 二维矩阵 中最大的矩形面积 
        int[][] heights = new int[row][col];  // 每层的 柱状图 高度

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (i == 0) {  // 第一行特殊处理
                    heights[i][j] = arr2[i][j];
                } else {
                    heights[i][j] = arr2[i][j] == 0 ? 0 : heights[i - 1][j] + 1;
                }
            }
        }

        int maxArea = 0;
        for (int i = 0; i < heights.length; i++) {
            maxArea = Math.max(maxArea, IncreaseStack.maxRectangleArea(heights[i]));
        }

        return maxArea;
    }


    // 下一个更高温度 
    private static int[] nextHeigherTem(int[] arr3) {
        int n = arr3.length;
        int[] ans = new int[n];  // 数值 默认为 0 
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && arr3[i] > arr3[stack.peek()]) {
                int preIndex = stack.pop();
                ans[preIndex] = i - preIndex;
            }

            stack.push(i);
        }

        return ans;
    }

    // 循环数组 下一个更大元素 
    private static void nextBiggerNum(int[] arr4) {
        // 数组 最后一个数组 下一个元素是第一个, 循环 
        int n = arr4.length;
        int[] ans = new int[n];
        Deque<Integer> stack = new ArrayDeque<>();
        boolean next = true;

        for (int i = 0; i < 2 * n - 1; i++) {
            if (!next) {
                break;
            }
            int index = i % n;
            while (!stack.isEmpty() && arr4[index] > arr4[stack.peek()]) {
                int preIndex = stack.pop();
                ans[preIndex] = arr4[index];
                if (preIndex >= n - 1) {  // 最后一个答案
                    next = false;
                }
            }

            stack.push(i);
        }
    }

}




















