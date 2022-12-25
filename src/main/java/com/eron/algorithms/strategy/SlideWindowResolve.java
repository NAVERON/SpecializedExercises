package com.eron.algorithms.strategy;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 滑动窗口 方案 系列问题 
 * @author wangy
 *
 */

public class SlideWindowResolve {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlideWindowResolve.class);
    
    public static void main(String[] args) {
        char[] arr = "qwertyuiopasdfghjklzxcvbnm".toCharArray();
        SlideWindowResolve.maxLengthUniqChar(arr);
        
        String a = "abcd", b = "bcdf";
        int cost = 3;
        SlideWindowResolve.maxLengthOfLimitCost(a, b, cost);
        
    }
    

    
    // 数组中找到连续和 大于target的 
    private static void targetSumArray(int[] arr, int target) {
        int n = arr.length;
        int i = 0, j = 0;
        int sum = 0;
        
        int ans = 0;
        
        while(j < n) {
            sum += arr[j];
            while(sum >= target) {
                ans = Math.min(ans, j - i + 1);
                sum -= arr[i];
                i++;
            }
            
            j++;
        }
        LOGGER.info("最小数组长度 --> {}", ans);
    }
    
    // 无重复字符 最长长度 
    private static void maxLengthUniqChar(char[] str) {
        int n = str.length;
        int i = 0, j = 0;
        Set<Character> sets = new HashSet<>();
        int maxNum = 0;  // 最多不重复字符 
        
        while(j < n) {  //这里的set  用于判断重复字符 是否已经存在, 可以其他替代 
            if(!sets.contains(str[j])) {
                sets.add(str[j]);
                j++;
                maxNum = Math.max(maxNum, sets.size());
            }else {
                sets.remove(str[i]);
                i++;
            }
        }
        
        LOGGER.info("最长不重复字符 --> {}", maxNum);
    }
    
    private static void longestOnes(int[] A, int K) {
        int N = A.length;
        int res = 0;  // 最长的1 数量 
        int left = 0, right = 0;  // 两个指针 
        int zeros = 0;  // 当前窗口有几个 0
        
        while (right < N) {
            if (A[right] == 0)
                zeros ++;
            while (zeros > K) {
                if (A[left++] == 0) 
                    zeros--;
            }
            res = Math.max(res, right - left + 1);
            right ++;
        }
        
        LOGGER.info("最长 1 --> {}", res);
    }
    
    // 字符转换最大长度  s --> t 转化字符, 限制最大消耗, 可以得到的最大长度 
    private static void maxLengthOfLimitCost(String s, String t, int cost) {
        int n = s.toCharArray().length;
        int[] diff = new int[n];
        for(int i = 0; i < n; i++) {
            diff[i] = Math.abs(s.charAt(i) - t.charAt(i));
        }
        
        int start = 0, end = 0;
        int maxLength = 0;
        int curCost = 0;
        
        while(end < n && start <= end) {
            curCost += diff[end];
            while(start <= end && curCost > cost) {
                curCost -= diff[end];
                start++;
            }
            
            end++;
            maxLength = Math.max(maxLength, end - start);
        }
        
        LOGGER.info("最长 --> {}", maxLength);
    }
    
}











