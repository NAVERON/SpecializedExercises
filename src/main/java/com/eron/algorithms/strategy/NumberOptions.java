package com.eron.algorithms.strategy;

import java.util.ArrayDeque;
import java.util.Deque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 有关数字计算的策略问题
 */
public class NumberOptions {
    private static final Logger LOGGER = LoggerFactory.getLogger(NumberOptions.class);

    public static void main(String[] args) {
        NumberOptions numberOptions = new NumberOptions();
        // 数字翻转
        LOGGER.info("翻转数字 --> {}", numberOptions.reverseNumber(1234));
        LOGGER.info("是否为回文数字 --> {}", numberOptions.isSymmetryNumber(1221));

        // 字符串 转换成 数字
        LOGGER.info("字符串转换为数字 --> {}", numberOptions.parseToNumber("123".toCharArray()));

        // 两个字符串数字相加
        LOGGER.info("两个字符串数字相加 --> {}",
            numberOptions.plusNumber("12".toCharArray(), "34".toCharArray()));
        LOGGER.info("使用简单方法 两数相加 --> {}",
            numberOptions.parseToNumber("12".toCharArray()) + numberOptions.parseToNumber(
                "34".toCharArray()));

        // 两个字符串相乘
        LOGGER.info("两数相乘 --> {}",
            numberOptions.timesNumber("123".toCharArray(), "456".toCharArray()));
        LOGGER.info("验证两数相乘结果 --> {}",
            numberOptions.parseToNumber("123".toCharArray()) * numberOptions.parseToNumber(
                "456".toCharArray()));
    }

    // 翻转数字 [正整数]， 1234 --> 4321
    public long reverseNumber(long number) {
        long reversedNumber = 0;
        long remainder;
        while (number > 0) {
            remainder = number % 10;
            number = number / 10;
            reversedNumber = reversedNumber * 10 + remainder;
        }

        return reversedNumber;
    }

    // 对称/回文数字判断 如 1221, 只能是 正整数
    // 可以先翻转，然后判断两个数字是否一样
    // 转换成字符串，使用 <StringOption> 方法判断是否为回文字符串
    public boolean isSymmetryNumber(long number) {
        long div = 1;
        while (number / div >= 10) {
            div *= 10;
        }
        LOGGER.info("div --> {}", div);

        while (number > 0) {
            long heigh = number / div;
            long low = number % div;
            if (heigh != low) {
                return false;
            }

            number = (number % div) / 10;
            div /= 100; // number前后各自删除了一个数字
        }

        return true;
    }

    // 字符串转换成正整数
    private long parseToNumber(char[] chars) {
        int base = 0;
        for (char c : chars) {
            base = base * 10 + (c - '0');
        }

        return base;
    }

    // 字符串 计算相加,只考虑正整数
    public long plusNumber(char[] c1, char[] c2) {
        // 简单的方法 将两个转换为数字，直接进行运算
        long base = 0, carry = 0;
        int m = c1.length - 1, n = c2.length - 1;
        Deque<Long> ans = new ArrayDeque<>();

        while (m >= 0 && n >= 0) {
            long num1 = c1[m] - '0';
            long num2 = c2[n] - '0';
            base = (num1 + num2 + carry) % 10;
            carry = (num1 + num2 + carry) / 10;

            ans.addFirst(base);
            m--;
            n--;
        }

        while (m >= 0) {
            long num1 = c1[m] - '0';
            base = (num1 + carry) % 10;
            carry = (num1 + carry) / 10;

            ans.addFirst(base);
            m--;
        }

        while (n >= 0) {
            long num2 = c2[n] - '0';
            base = (num2 + carry) % 10;
            carry = (num2 + carry) / 10;

            ans.addFirst(base);
            n--;
        }

        if (carry > 0) {
            ans.addFirst(carry);
        }

        // 队列中的数字 转换成真正的number
        long sum = 0;
        while (!ans.isEmpty()) {
            sum = sum * 10 + ans.pollFirst();
        }
        return sum;
    }

    // 两个字符串数字相乘
    public long timesNumber(char[] c1, char[] c2) {
        long sum = 0;
        int level = 1;
        int n = c1.length;

        for (int i = n - 1; i >= 0; i--) {
            char x = c1[i];
            sum += this.singleTimesNumber(x, c2) * level;

            level *= 10;
        }

        return sum;
    }

    // 提供底层实现 单个数字 乘以 一个多位数
    private long singleTimesNumber(char a, char[] chars) {
        int n = chars.length - 1;
        long base = 0, carry = 0;
        long num0 = a - '0'; // 乘数基数

        int level = 1; // 每次上一个层次，乘法计算
        long sum = 0; // 最终乘法结果

        while (n >= 0) {
            long num1 = chars[n] - '0';
            base = (num0 * num1 + carry) % 10;
            carry = (num0 * num1 + carry) / 10;

            sum = sum + base * level;

            level *= 10;
            n--;
        }

        if (carry > 0) {
            sum = sum + carry * level;
        }

        return sum;
    }

}
