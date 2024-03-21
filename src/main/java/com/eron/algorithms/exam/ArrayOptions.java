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
        // 求两个有序数组中的 中位数
        arrayOptions.midOfArray(arr, arr);
        // 求数组中子数组 和 为target的集合
        arrayOptions.subArraySumOfTarget(arr, 0);
        // 重写0 数组中遇到0，则辅之以个顺后多协议个0
        arr = new int[]{1, 0, 2, 3, 0, 4, 5, 0};
        arrayOptions.rewriteZero(arr);
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
    public float midOfArray(int[] arr1, int[] arr2) {
        // Integer[] arr1 = {1, 4, 6, 7, 9, 25};
        // Integer[] arr2 = {1, 3, 6, 8, 14, 22, 32, 43};
        // 二分法思路， 每次比较切分点两边的大小

        System.out.println("==============================");
        float result = 0F;
        // 二分法 查找中位数
//		奇数：
//		median = max(maxLeftA, maxLeftB)
//		偶数：
//		median = (max(maxLeftA, maxLeftB) + min(minRightA, minRightB)) / 2

        /**
         * 过程
         * 1 短数组放前面， 长数组放后面
         * 2 遍历短数组， i++, 长数组指针 bIndex = (m+n+1)/2 - aIndex
         * 3 找到邻近的4个值, 判断
         * 4 左右边界 变化，最终缩小到临界
         */

        Integer len1 = arr1.length;
        Integer len2 = arr2.length;

        boolean isEven = (len1 + len2) % 2 == 0 ? true : false;  // 偶数还是奇数

        if (len1 > len2) { // arr1 始终选择短的那个， 减少长短判断
            // MedianOfTwoArray.reverseArray(arr1, arr2);
        }

        Integer low = 0;
        Integer high = len1;

        while (low <= high) {
            System.out.println("当前 [low, high] : " + low + ", " + high);

            int partation1 = (high - low) / 2 + low;
            int partation2 = (len1 + len2 + 1) / 2 - partation1;  // 这样计算就可以保证 i + j = 中间的数量

            int maxLeftA = partation1 == 0 ? Integer.MIN_VALUE : arr1[partation1 - 1];
            int minRightA = partation1 == len1 ? Integer.MAX_VALUE : arr1[partation1];
            int maxLeftB = partation2 == 0 ? Integer.MIN_VALUE : arr2[partation2 - 1];
            int minRightB = partation2 == len2 ? Integer.MAX_VALUE : arr2[partation2];

            if (maxLeftA <= minRightB && minRightA >= maxLeftB) {
                // 如果找到临界
                // 如果是偶数
                System.out.println("找到时 分界线附近的变量 : [maxLeft1, minRight1, maxLeft2, minRight2] "
                    + maxLeftA + ", " + minRightA
                    + ", " + maxLeftB + ", " + minRightB);
                if (isEven) {
                    result = Float.sum(Math.max(maxLeftA, maxLeftB), Math.min(minRightA, minRightB)) / 2;
                    System.out.println("结果 " + result);
                } else {
                    result = Float.max(maxLeftA, maxLeftB);
                    System.out.println("结果 " + result);
                }
                break;
            } else if (maxLeftA > minRightB) {
                // partation1 靠右了/太大了， 中位数在左边的地方
                high = partation1 - 1;  // 缩小查找范围
            } else {
                low = partation1 + 1;
            }
        }

        return result;
    }

    // 求一个数组中 有哪些连续子数组的和为给定的值
    // 思路 ：滑动窗空 -> 提前计算 + 遍历
    public void subArraySumOfTarget(int[] arr, int target) {
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

        LOGGER.info("打印前缀和 --> {}", Arrays.stream(preSum).boxed().collect(Collectors.toList()));

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

    public void rewriteZero(int[] arr) {
        // 预先计算位置 然后正常逻辑填充
        int index = 0;
        int cur = 0;
        for (; cur < arr.length && index < arr.length; cur++) {
            if (arr[cur] == 0) {
                index++;
            }
            index++;
        }
        cur--; // 索引，向前退一步

        // 从后向前复原操作
        int last = arr.length - 1;
        for (; cur >= 0 && last >= 0; cur--) {
            if (arr[cur] == 0) {
                arr[last--] = 0;
                arr[last--] = 0;
            } else {
                arr[last--] = arr[cur];
            }
        }

        LOGGER.info("重写0后的数组 --> {}", Arrays.stream(arr).boxed().collect(Collectors.toList()));
    }

}
