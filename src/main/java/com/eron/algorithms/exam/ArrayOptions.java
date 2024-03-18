package com.eron.algorithms.exam;

import java.util.Arrays;
import java.util.Stack;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 各种小算法题目的实现
 */
public class ArrayOptions {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayOptions.class);

    public static void main(String[] args) {
        ArrayOptions arrayOptions = new ArrayOptions();
        int[] arr = new int[]{1, 2, 5, 6, 10};
        // 数组可以被分割成多少个递增子序列
        arrayOptions.arrCanSplits(arr);
        // 数组中数字 可组合成的最大数字
        arrayOptions.bigestNumber(arr);
        // 数组中每个位置 最临近的最大值
        arrayOptions.nextBiggerNumber(arr);
    }

    /**
     * 一个数组 切分递增的字数组，可以被切分成多少组
     * ---
     * 理解 ：对于每一个切分的部分，其中切分点左边最大值小于右边最小值
     */
    private void arrCanSplits(int[] arr) {
        int n = arr.length;
        int[] leftMax = new int[n]; leftMax[0] = arr[0];
        int[] rightMin = new int[n]; rightMin[n-1] = arr[n-1];

        int max = arr[0];
        // 遍历左边最大值
        for (int i = 1; i < n; i++) {
            if (arr[i - 1] > max) {
                max = arr[i-1];
            }
            leftMax[i] = max;
        }

        int min = arr[n-1];
        for (int i = n-2; i >= 0; i--) {
            if (arr[i+1] < min) {
                min = arr[i+1];
            }
            rightMin[i] = min;
        }

        int res = 0;
        for (int i = 0; i < n; i++) {
            if (leftMax[i] < rightMin[i]) {
                res++;
            }
        }

        LOGGER.info("数组可以被分成 {} 组", res);
    }

    /**
     * 给出一个数组，找出数组能够拼接的最大数字
     * ---
     * 思路：数字大的放在高位 将数字的组合按照大小排列
     * 两两比对，交换2个数字的前后顺序并比较大小， 组成后的数字大的放在前面, 否则放在后面
     */
    private void bigestNumber(int[] arr) {
        String res = Arrays.stream(arr).boxed().sorted((n1, n2) -> {
            String n1String = Integer.toString(n1);
            String n2String = Integer.toString(n2);
            return Integer.parseInt(n2String + n1String) - Integer.parseInt(n1String + n2String);
        }).map(String::valueOf).collect(Collectors.joining());

        LOGGER.info("数组可拼接的最大数字是 {}", res);
    }

    /**
     * 数组中 下一个比自己大的数字位置
     * ---
     * 思路: 单调栈 递增栈；如果后一个比栈顶的大，更新栈顶位置的 最临近最大值
     * 还有一些问题，需要倒着对比: 单调递减栈
     */
    public void nextBiggerNumber(int[] arr) {
        int n = arr.length;
        Stack<Integer> stack = new Stack<>();
        int[] ans = new int[n]; // 最大的一个没有比它更大的， 默认值为0
        boolean needNext = true;

        for (int i = 0; i < 2 * n - 1; i++) {
            if (!needNext) {
                break;
            }

            while (!stack.isEmpty() && arr[stack.peek() % n] < arr[i % n]) {
                int preIndex = stack.pop();
                if (preIndex > n - 1) {
                    needNext = false;
                    break;
                }
                ans[preIndex] = arr[i % n];
            }
            stack.push(i);
        }

        // 结果
        LOGGER.info("单调栈 最临近最大值 计算结果 --> {}", Arrays.stream(ans).boxed().collect(
            Collectors.toList()));
    }

    // 线段树问题 二位数组：排序的实现
    public void periodExchange() {

    }

    // 两个排序好的数组，求数组的中位数 --> 简单办法：合并数组，计算中位数
    // 巧妙方法，夹逼两个数组，得到中间的值 或 中间2个值的平均
    public void midOfArray(int[] arr1, int[] arr2) {

    }
}
