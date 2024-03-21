package com.eron.algorithms.exam;

import java.lang.ProcessHandle.Info;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.SimpleTimeZone;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringOptions {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringOptions.class);

    public static void main(String[] args) {
        StringOptions stringOptions = new StringOptions();
        LOGGER.info("翻转字符串 --> {}", stringOptions.reverse("helloworld", 3));
        LOGGER.info("字符串转换为数字 --> {}", stringOptions.parseToNumber("123".toCharArray()));

        LOGGER.info("两个字符串数字相加 --> {}",
            stringOptions.plusNumber("12".toCharArray(), "34".toCharArray()));
        LOGGER.info("使用简单方法 两数相加 --> {}",
            stringOptions.parseToNumber("12".toCharArray()) + stringOptions.parseToNumber(
                "34".toCharArray()));

        LOGGER.info("两数相乘 --> {}",
            stringOptions.timesNumber("123".toCharArray(), "456".toCharArray()));
        LOGGER.info("验证两数相乘结果 --> {}",
            stringOptions.parseToNumber("123".toCharArray()) * stringOptions.parseToNumber(
                "456".toCharArray()));
    }

    // 字符串转换成正整数
    private long parseToNumber(char[] chars) {
        int base = 0;
        for (char c : chars) {
            base = base * 10 + (c - '0');
        }

        return base;
    }

    // 翻转字符串 设定一个位置，将其前后的字符串调换位置, 例子 : helloworld ---> loworldhel
    public String reverse(String s, int index) {
        if (Objects.isNull(s)) {
            throw new IllegalArgumentException("string is empty");
        }
        char[] chars = s.toCharArray();
        if (index > chars.length - 1) {
            throw new IllegalArgumentException("index out of range");
        }

        this.reverseChar(chars, 0, chars.length - 1 - index);
        this.reverseChar(chars, chars.length - 1 - index + 1, chars.length - 1);
        this.reverseChar(chars, 0, chars.length - 1);

        return Arrays.toString(chars);
    }
    private void reverseChar(char[] chars, int start, int end) { // 翻转字符位置
        while (start < end) {
            char temp = chars[start];
            chars[start] = chars[end];
            chars[end] = temp;

            start++;
            end--;
        }
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

    // kmp 字符串搜寻算法
    public void KMPAlgorithm(String text, String pattern) {

    }

}
