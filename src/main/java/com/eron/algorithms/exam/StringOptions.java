package com.eron.algorithms.exam;

import java.lang.ProcessHandle.Info;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.SimpleTimeZone;
import java.util.logging.Level;
import java.util.stream.Collectors;
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
        LOGGER.info("kmp 搜索 --> {}", stringOptions.KMPSearch("hello world", "rl"));
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
    public int KMPSearch(String str, String pattern) {
        char[] strs = str.toCharArray();
        char[] patterns = pattern.toCharArray();

        int L = strs.length, N = patterns.length;
        int i = 0, j = 0; // i: str pointer, j: pattern pointer

        // 特殊情况处理
        if (N < 1) return 0;
        if (L < N) return -1;

        int[] lps = lps(pattern); // get the array that stores the longest subarray whose prefix is also its suffix
        while (i < L) {
            // 如果两个char相等 前进1位
            if (strs[i] == patterns[j]) { // same value found, move both str and pattern pointers to their right
                ++i;
                ++j;
                if (j == N) return i - N; // whole match found
            }
            // 判断回溯位置
            else if (j > 0) j = lps[j - 1]; // move pattern pointer to a previous safe location
                // 如果j在起始位置 表示第一个字符就不匹配
            else ++i; // restart searching at next str pointer
        }
        return -1;
    }

    // next指针数组生成 关键**
    private int[] lps(String pattern) {
        int j = 0, i = 1, L = pattern.length();  // 两个指针

        int[] res = new int[L];  // 全部初始化为 0

        char[] chars = pattern.toCharArray();

        while (i < L) {
            if (chars[i] == chars[j]) {
                res[i++] = ++j;  // j++; res[i] = j; i++;  // 直接跳到下一个匹配位
            } else {
                int temp = i - 1;  // 上一个可以判断公共前后缀的信息
                while (temp > 0) {
                    int prevLPS = res[temp];
                    if (chars[i] == chars[prevLPS]) {  // 表示前后缀匹配+1 i的位置就是新增相同char的索引
                        res[i++] = prevLPS + 1;
                        j = prevLPS;
                        break;
                    } else temp = prevLPS - 1;  // 如果不匹配 找前部分前缀的对称位置 res[temp - 1] 获取前面的位置, 因为对称性
                }
                if (temp <= 0) {
                    res[i++] = 0;
                    j = 0;
                }
            }
        }
        return res;
    }

    // 最长回文字符串匹配
    public String manacher(String s) {
        String T = preProcess(s);
        int n = T.length();

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
            while (T.charAt(i + 1 + P[i]) == T.charAt(i - 1 - P[i])) {  // 如果可以拓展就拓展，否则表示已经到边界
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
    public void longestCommonString(char[] chars1, char[] chars2) {
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
    }

    // 无重复字符 最长长度
    private static void longestUniqueChars(char[] str) {
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
    }

    // 字符转换最大长度  s --> t 转化字符, 限制最大消耗, 可以得到的最大长度
    private static void maxLengthOfLimitCost(String s, String t, int cost) {
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
    }

}
