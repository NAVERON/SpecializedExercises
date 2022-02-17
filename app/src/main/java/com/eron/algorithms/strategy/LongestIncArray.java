/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 求解一个数组序列中最长递增子序列
 *
 * @author ERON_AMD 动态规划 LIS问题
 */
public class LongestIncArray {
    
    private static Logger log = LoggerFactory.getLogger(LongestIncArray.class);

    public static void main(String[] args) {
        //  递归  动态规划  最长子序列
        int[] test1 = {1, 23, 5, 8, 24, 20, 56, 20};
        int incLength = getLIS(test1);
        log.info("结果 : {}", incLength);
        
        char[] a1 = "HelloWorld".toCharArray();
        char[] a2 = "loop".toCharArray();
        longestCommonSeq(a1, a2);
    }

    /**
        * 求取最长递增子序列
        * @param arr
        * @return
        */
    public static int getLIS(int[] arr) {
        int[] dp = new int[arr.length];  // 自动全部初始化为0，每一个索引位作为最后一个 前面可有的最长递增子序列
        dp[0] = 1;

        for (int i = 1; i < arr.length; i++) {
            int maxLength = 0;
            for (int j = 0; j < i; j++) {
                if (arr[j] < arr[i]) {
                    maxLength = Math.max(maxLength, dp[j]);  // 从0 至j的最大递增
                    // 如果需要输出数组, 这里添加
                }
            }

            dp[i] = maxLength + 1;
            log.info("第{}个循环, 当前maxLength = {}, 输出当前dp : {}", i, maxLength, arrayToString(dp));
        }

        int maxResult = 0;
        for (int i = 0; i < dp.length; i++) {
            maxResult = maxResult >= dp[i] ? maxResult : dp[i];
        }

        return maxResult;
    }
    
    public static String arrayToString (int[] arr){
        StringBuilder sb = new StringBuilder();
        sb.append("当前数组 : ");
        for(int a : arr){
            sb.append(a).append(",");
        }
        
        return sb.toString();
    }
    
    /**
        * 最长公共子序列
        * @param arr1 
        * @param arr2 
        */
    public static void longestCommonSeq(char[] arr1, char[] arr2){
        // 最长公共子序列
        int n1 = arr1.length, n2 = arr2.length;
        int[][] dp = new int[n1+1][n2+1];
        
        for(int i = 0; i < n1+1; i++){
            dp[i][0] = 0;
        }
        for(int i = 0; i < n2+1; i++){
            dp[0][i] = 0;
        }
        
        for(int i = 1; i < n1+1; i++){
            for(int j = 1; j < n2+1; j++){
                if(arr1[i-1] == arr2[j-1]){
                    dp[i][j] = dp[i-1][j-1] + 1;
                }else{
                    dp[i][j] = Math.max(dp[i-1][j], dp[i][j-1]);
                }
            }
        }
        
        // 打印出整个数组
        // 最大值
        int maxLength = 0;
        for(int i = 0; i < n1+1; i++){
            for(int j = 0; j < n2+1; j++){
                System.err.print(dp[i][j] + "  ");
                maxLength = maxLength > dp[i][j] ? maxLength : dp[i][j];
            }
            System.err.println("\n");
        }
        
        log.info("最长子序列 : {}", maxLength);
    }
    

}
