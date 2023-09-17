package com.eron.algorithms.strategy;

import java.util.ArrayList;
import java.util.List;

// 最长回文子序列 
// 多种算法  暴力， 中心扩展， 动态规划， manacher算法 
public class Manacher {

    public static void main(String[] args) {
        Manacher manacher = new Manacher();

        String s = "test31tses";

        String result = manacher.longestPalindrome(s);
        System.out.println("result : " + result);

        String result2 = manacher.longestPalindromeManacher(s);
        System.out.println("result 2 : " + result2);
    }

    // manacher 算法
    public String longestPalindromeManacher(String s) {
        int start = 0, end = -1;
        // 原始字符串插入 # 保证字符个数为奇数， 原因 s.length * 2 + 1 永远是奇数 
        StringBuffer t = new StringBuffer("#");
        for (int i = 0; i < s.length(); ++i) {  // 填充# 
            t.append(s.charAt(i)).append("#");
            // t.append('#');
        }
        // t.append('#');

        s = t.toString();

        List<Integer> arm_len = new ArrayList<Integer>();  // 回文半径 
        int right = -1, j = -1;

        for (int i = 0; i < s.length(); ++i) {  // 遍历 从开头到结尾的 
            int cur_arm_len;
            if (right >= i) {
                int i_sym = j * 2 - i;
                int min_arm_len = Math.min(arm_len.get(i_sym), right - i);
                cur_arm_len = expand(s, i - min_arm_len, i + min_arm_len);
            } else {
                cur_arm_len = expand(s, i, i);
            }
            arm_len.add(cur_arm_len);
            if (i + cur_arm_len > right) {
                j = i;
                right = i + cur_arm_len;
            }
            if (cur_arm_len * 2 + 1 > end - start) {
                start = i - cur_arm_len;
                end = i + cur_arm_len;
            }
        }

        StringBuffer ans = new StringBuffer();
        for (int i = start; i <= end; ++i) {
            if (s.charAt(i) != '#') {
                ans.append(s.charAt(i));
            }
        }
        return ans.toString();
    }

    /**
     * 中心扩散 中心 辅助方法
     *
     * @param s     原始字符串
     * @param left  判断回文的左中心
     * @param right 判断回文的右中心
     * @return ? 应该是臂长
     */
    public int expand(String s, int left, int right) {
        while (left >= 0 && right < s.length() && s.charAt(left) == s.charAt(right)) {
            --left;
            ++right;
        }
        return (right - left - 2) / 2;
    }

    // 马拉车算法  第二种写法， 参考链接 ： https://www.zhihu.com/question/37289584/answer/465656849 
    // 学习链接 ： https://segmentfault.com/a/1190000008484167 
    public String longestPalindrome2(String s) {
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
    public String preProcess(String s) {
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

    // 动态规划
    public String longestPalindrome(String s) {
        int len = s.length();
        if (len < 2) {
            return s;
        }

        int maxLen = 1;  // 最长回文 长度 
        int begin = 0;  // 最长回文序列 开始指针
        // dp[i][j] 表示 s[i..j] 是否是回文串
        boolean[][] dp = new boolean[len][len];
        // 初始化：所有长度为 1 的子串都是回文串
        for (int i = 0; i < len; i++) {
            dp[i][i] = true;
        }

        char[] charArray = s.toCharArray();
        // 递推开始
        // 先枚举子串长度
        for (int j = 1; j < len; j++) {
            for (int i = 0; i < j; i++) {
                if (charArray[i] != charArray[j]) {
                    dp[i][j] = false;
                } else {
                    if (j - i < 3) {
                        dp[i][j] = true;
                    } else {
                        dp[i][j] = dp[i + 1][j - 1];
                    }
                }

                // 如果 dp[i][j] 是回文， 计算起始位置和长度
                if (dp[i][j] && j - i + 1 > maxLen) {
                    maxLen = j - i + 1;
                    begin = i;
                }
            }
        }
        return s.substring(begin, begin + maxLen);
    }

    // 暴力解法 核心 是否是回文子序列
    public Boolean bruteForce(String originString, int i, int j) {
        // 检查 i j 之间是否是回文序列
        while (i < j) {
            if (originString.charAt(i) == originString.charAt(j)) {
                i++;
                j--;
            } else {
                return Boolean.FALSE;
            }
        }

        return Boolean.TRUE;
    }

    // 中心扩散法
    public Integer extendFromCenter(String originString, int i, int j) {
        // 从i j 向两个方向扩散, 直到不满足回文序列 或者到达 端点
        Integer maxLen = 0;

        while (i >= 0 && j < originString.length()) {
            if (originString.charAt(i) == originString.charAt(j)) {
                i--;
                j++;
            } else {
                break;
            }
        }

        maxLen = j - i - 1;

        return maxLen;
    }
}








