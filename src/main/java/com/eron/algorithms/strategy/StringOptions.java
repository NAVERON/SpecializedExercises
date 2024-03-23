package com.eron.algorithms.strategy;

import com.eron.designpattern.JdkDynamicProxy;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.lang.model.util.ElementScanner6;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringOptions {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringOptions.class);

    public static void main(String[] args) {
        StringOptions stringOptions = new StringOptions();
        // 字符串翻转
        LOGGER.info("翻转字符串 --> {}", stringOptions.reverse("helloworld", 3));
        // 字符串 转换成 数字
        LOGGER.info("字符串转换为数字 --> {}", stringOptions.parseToNumber("123".toCharArray()));

        // 两个字符串数字相加
        LOGGER.info("两个字符串数字相加 --> {}",
            stringOptions.plusNumber("12".toCharArray(), "34".toCharArray()));
        LOGGER.info("使用简单方法 两数相加 --> {}",
            stringOptions.parseToNumber("12".toCharArray()) + stringOptions.parseToNumber(
                "34".toCharArray()));

        // 两个字符串相乘
        LOGGER.info("两数相乘 --> {}",
            stringOptions.timesNumber("123".toCharArray(), "456".toCharArray()));
        LOGGER.info("验证两数相乘结果 --> {}",
            stringOptions.parseToNumber("123".toCharArray()) * stringOptions.parseToNumber(
                "456".toCharArray()));

        // KMP 字符串搜索算法
        LOGGER.info("kmp 搜索 --> {}", stringOptions.KMPSearch("rl", "hello world"));
        LOGGER.info("最长回文字串 --> {}", stringOptions.manacher("nduiebhufrvkd"));
        // 最长公共子序列
        LOGGER.info("最长公共字串 --> {}",
            stringOptions.longestCommonString("xxx".toCharArray(), "ccc".toCharArray()));
        LOGGER.info("最长无重复子串 --> {}",
            stringOptions.longestUniqueChars("heloji".toCharArray()));
        LOGGER.info("把一个字符串转换成另一个 限制转换次数 --> {}",
            stringOptions.maxLengthOfLimitCost("hello", "world", 12));
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
    // 参考资料
    // (1) https://en.wikipedia.org/wiki/Knuth%E2%80%93Morris%E2%80%93Pratt_algorithm
    // (2) jakeboxer.com/blog/2009/12/13/the-knuth-morris-pratt-algorithm-in-my-own-words/
    public int KMPSearch(String pattern, String txt) {
        int M = pattern.length();
        int N = txt.length();

        int i = 0, j = 0;
        int[] lps = this.computeLPSArray(pattern);
        while ((N - i) >= (M - j)) { // 需要比对的字符串长度 > 匹配字符串长度
            if (txt.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
            }

            if ( j == M) {
                // 如果匹配到了匹配字符串最后 表示找到了目标
                LOGGER.info("找到匹配位置 --> {}, {}", i, j);
                // j = lps[j - 1];
                return i - j;
            } else if (i < N && txt.charAt(i) != pattern.charAt(j)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return -1;
    }

    // next指针数组生成 关键**, 找出匹配字符串的前缀相同的影子状态
    // longest proper prefix
    private int[] computeLPSArray(String pattern) {
        int[] lps = new int[pattern.length()];
        lps[0] = 0;

        // length of the previous longest prefix suffix
        int len = 0;
        int i = 1;
        while (i < pattern.length()) {
            if (pattern.charAt(len) == pattern.charAt(i)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = len;
                    i++;
                }
            }
        }

        return lps;
    }

    // 最长回文字符串匹配
    public String manacher(String s) {
        String s1 = preProcess(s);
        int n = s1.length();

        int[] P = new int[n];  //记录每个索引中心的 回文半径
        int C = 0, R = 0;  // C 回文中心index  回文半径

        for (int i = 1; i < n - 1; i++) {
            int i_mirror = 2 * C - i;  // i 关于C的镜像位置索引
            if (R > i) {  // 当前点在回文序列的半径范围   R 是半径的索引
                P[i] = Math.min(R - i, P[i_mirror]);// 防止超出 R
            } else {
                P[i] = 0;// 等于 R 的情况, i 超出了 C的回文半径， 需要中心拓展找出回文序列半径
            }

            // 碰到之前讲的三种情况时候，需要利用中心扩展法
            // 如果可以拓展就拓展，否则表示已经到边界
            while (s1.charAt(i + 1 + P[i]) == s1.charAt(i - 1 - P[i])) {
                P[i]++;
            }

            // 判断是否需要更新 R
            if (i + P[i] > R) {  // 如果当前 i的回文半径大于之前的， 就指向当前的回文中心 参数
                C = i;
                R = i + P[i];  // 所以R 是半径的索引， 不是半径 p[i]是半径
            }
        }

        // 找出 P 的最大值
        int maxLen = 0;
        int centerIndex = 0;
        for (int i = 1; i < n - 1; i++) {
            if (P[i] > maxLen) {
                maxLen = P[i];
                centerIndex = i;
            }
        }
        int start = (centerIndex - maxLen) / 2; //最开始讲的求原字符串下标
        LOGGER.info("最长回文 下标 {}, 长度 : {}", start, maxLen);
        return s.substring(start, start + maxLen);
    }

    // 一些方法会在字符串前后增加 ^$ 表示开头和结尾， 不需要 再判断字符串边界了
    private String preProcess(String s) {
        int n = s.length();
        if (n == 0) {
            return "^$";
        }
        StringBuilder ret = new StringBuilder("^");
        for (int i = 0; i < n; i++)
            ret.append("#").append(s.charAt(i));
        ret.append("#$");
        return ret.toString();
    }

    // 最长公共子序列
    public int longestCommonString(char[] chars1, char[] chars2) {
        // 最长公共子序列
        int n1 = chars1.length, n2 = chars2.length;
        int[][] dp = new int[n1 + 1][n2 + 1];

        for (int i = 0; i < n1 + 1; i++) {
            dp[i][0] = 0;
        }
        for (int i = 0; i < n2 + 1; i++) {
            dp[0][i] = 0;
        }

        for (int i = 1; i < n1 + 1; i++) {
            for (int j = 1; j < n2 + 1; j++) {
                if (chars1[i - 1] == chars2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }

        // 打印出整个数组
        // 最大值
        int maxLength = 0;
        for (int i = 0; i < n1 + 1; i++) {
            for (int j = 0; j < n2 + 1; j++) {
                System.err.print(dp[i][j] + "  ");
                maxLength = Math.max(maxLength, dp[i][j]);
            }
            System.err.println("\n");
        }

        LOGGER.info("最长子序列 : {}", maxLength);
        return maxLength;
    }

    // 滑动窗口思想
    // 无重复字符 最长长度
    private int longestUniqueChars(char[] str) {
        int n = str.length;
        int i = 0, j = 0;
        Set<Character> sets = new HashSet<>();
        int maxNum = 0;  // 最多不重复字符

        while (j < n) {  //这里的set  用于判断重复字符 是否已经存在, 可以其他替代
            if (!sets.contains(str[j])) {
                sets.add(str[j]);
                j++;
                maxNum = Math.max(maxNum, sets.size());
            } else {
                sets.remove(str[i]);
                i++;
            }
        }

        LOGGER.info("最长不重复字符 --> {}", maxNum);
        return maxNum;
    }

    // 字符转换最大长度  s --> t 转化字符, 限制最大消耗, 可以得到的最大长度
    public int maxLengthOfLimitCost(String s, String t, int cost) {
        int n = s.toCharArray().length;
        int[] diff = new int[n];
        for (int i = 0; i < n; i++) {
            diff[i] = Math.abs(s.charAt(i) - t.charAt(i));
        }

        int start = 0, end = 0;
        int maxLength = 0;
        int curCost = 0;

        while (end < n && start <= end) {
            curCost += diff[end];
            while (start <= end && curCost > cost) {
                curCost -= diff[end];
                start++;
            }

            end++;
            maxLength = Math.max(maxLength, end - start);
        }

        LOGGER.info("最长 --> {}", maxLength);
        return maxLength;
    }

}
