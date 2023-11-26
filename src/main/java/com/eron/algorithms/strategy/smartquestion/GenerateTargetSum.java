package com.eron.algorithms.strategy.smartquestion;

import java.util.Arrays;

/**
 * 随机生成一组和为给定数字的数组
 *
 * @author eron
 */
public class GenerateTargetSum {

    public static void main(String[] args) {
        int[] x = new int[]{1, 0, 2, 3, 0, 4, 5, 0};
        // 数组中的数字 遇到0 多写一个 dup 
        GenerateTargetSum generateTargetSum = new GenerateTargetSum();
        generateTargetSum.rewriteZero(x);  // 遍历2遍, 从后向前再过一遍, 遇到0直接多复制1位
        // 判断是否回文数字 
        generateTargetSum.reverseNumber(12021);  // 转换成字符串处理, ij索引相对而行
        // 按照数字处理, 求得最大位数, 取余和求商 对比比大小
        // 增序数组 删除重复值 
        int[] arr = new int[]{1, 1, 2};
        generateTargetSum.deleteDupNums(arr);  // 相等 j 向后一位， 不相等, 同时向后
        // 最大子数组和
        int[] arr2 = new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4};
        generateTargetSum.maxSubArray(arr2);

        generateTargetSum.getLastWord("hello hiudheh denini  ");
        // + 1算法
        int[] digits = new int[]{9};
        generateTargetSum.plusOne(digits);
    }

    public void rewriteZero(int[] arr) {
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            if (arr[i] == 0) {
                for (int j = n - 1; j > i + 1; j--) {
                    arr[j] = arr[j - 1];
                }
                arr[i + 1] = 0;
                i++;
            }
        }

        for (int a : arr) {
            System.out.print("==" + a);
        }
        System.out.println("");
    }

    // 回文数字
    public void reverseNumber(int x) {
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            System.out.println("负数 最后一位是0 不会是回文");
        }
        // 获取 x 有多少位 
        int length = 0;
        int num = x;
        while (num > 0) {
            length++;
            num /= 10;
        }
        System.out.println("x的长度=" + length);

        // 反转整个数字 
        num = x;
        int reversedNum = 0;
        while (num > 0) {
            reversedNum = reversedNum * 10 + num % 10;
            num /= 10;
        }
        System.out.println("反转后的数字 -->" + reversedNum);
        // 判断是否是回文数字 
        int reverseNumber = 0;
        num = x;
        while (num > reverseNumber) {
            reverseNumber = reverseNumber * 10 + num % 10;
            num /= 10;
        }
        boolean check1 = reverseNumber == num;
        boolean check2 = num == reverseNumber / 10;
        System.out.println("判断 == " + check1 + ", ===" + check2);
    }

    public void deleteDupNums(int[] arr) {
        int n = arr.length;
        System.out.println("数组长度 - " + n);
        if (n <= 1) System.out.println("数组长度 < 1 == " + n);
        ;
        int i = 0, j = 1;
        while (j < n) {
//            if(arr[i] == arr[j]) {
//                j++;
//                System.out.println("相等 + " + i + j);
//            }else {
//                arr[i+1] = arr[j];
//                System.out.println("输出当前变化的 - " + arr[i+1] + ", " + i + j);
//                i++;j++;
//            }

            // 上面的逻辑可以修改为 
            if (arr[i] != arr[j]) {
                arr[i + 1] = arr[j];
                i++;
            }
            j++;
        }
        System.out.println("遍历后i的索引 = " + i);
    }

    // 最大子数组和 
    public void maxSubArray(int[] nums) {  // 最长递增序列
        int n = nums.length;
        int pre = 0, max = 0;
        for (int x : nums) {
            pre = Math.max(pre + x, x);  // 连续子数组 
            max = Math.max(max, pre);
        }
        System.out.println("最大 - > " + max);
    }

    // 获取一个由空格分割的字符串的最后一个单词
    public void getLastWord(String s) { // 使用栈, 从后向前遍历
        String[] x = s.split("\s");
        Arrays.asList(x).forEach(ss -> System.out.println("分割结果-->" + ss));
        char[] chars = s.toCharArray();
        int n = chars.length;

        int i = 0, j = 0;
        int lastLen = 0;
        while (i < n && j < n) {
            if (chars[i] == ' ') {
                i++;
                j++;
                continue;
            }
            while (j < n && chars[j] != ' ') {
                j++;
            }
            lastLen = j - i;  // 更新最后的单词长度 
            // 使用substring 可以分割需要字符串 

            if (j < n) {
                i = j;
            }
        }
        System.out.println("输出最后的单词长度 -->" + lastLen);
    }

    // 给一个数字数组 +1  输出结果
    public void plusOne(int[] digits) { // 从后向前遍历, carry = 0, 直接返回原来数组 否则继续向前遍历
        int n = digits.length;
        int[] digits2 = new int[n + 1];  // 多出来一个保存最后的carry

        int carry = 1, base = 0;
        for (int i = n - 1; i >= 0; i--) {
            int sum = digits[i] + carry;
            System.out.println("sum = " + sum);
            base = sum % 10;
            carry = sum / 10;

            digits[i] = base;
            digits2[i + 1] = base;
        }

        if (carry > 0) {
            digits2[0] = carry;
            for (int d : digits2) {
                System.out.println("digits2 ==" + d);
            }
        } else {
            // 最后没有进位 
            for (int d : digits) {
                System.out.println("digits ==" + d);
            }
        }
    }
}











