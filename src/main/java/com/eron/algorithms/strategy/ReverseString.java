/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.strategy;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 以一个位置为中线 前后交换
 * @author ERON_AMD
 */
public class ReverseString {
    
    private static Logger log = LoggerFactory.getLogger(ReverseString.class);
    
    public static void main(String[] args) {
        char[] test = "abcdefg".toCharArray();
        rotateString(test, 4);
        log.info("result : {}", charToString(test));
        
        ReverseString.plusNumber("123", "33");  // 两数相加 
        
        // 乘法 
        ReverseString.numberTimes("20", "20");
    }
    
    public static String charToString(char[] arr){
        StringBuilder sb = new StringBuilder();
        for(char x : arr){
            sb.append(x);
        }
        
        return sb.toString();
    }
    
    public static void rotateString(char[] arr, int k){
        // 反转字符串
    	k = k % arr.length;
        // k = arr.length - (k % arr.length);  // 是否以0为开始下标
        
        reverse(arr, 0, arr.length - 1 - k);
        reverse(arr, arr.length - 1 - k + 1, arr.length - 1);
        reverse(arr, 0, arr.length - 1);
    }
    
    public static void reverse(char[] arr, int start, int end){
        while(start < end){
            char tmp = arr[start];
            arr[start] = arr[end];
            arr[end] = tmp;
            start++;
            end--;
        }
    }
    
    // 字符串相乘 实现 两个数字字符串相乘计算结果 
    private static void numberTimes(String x, String y) {
        
        char[] a = x.toCharArray(); int a1 = a.length;
        char[] b = y.toCharArray(); int b1 = b.length;
        int times = 1;  // 每次多余相乘是的倍数 
        int base = 0, carry = 0;
        
        int[] sums = new int[a1];
        
        for(int i = a1 - 1; i >= 0; i--) {
            int k = a[i] - '0';
            sums[i] = singleTimeString(k, b) * times;
            
            times *= 10;
        }
        
        // 最终 逐层加法 
        log.info("最终计算结果 --> {}", Arrays.stream(sums).sum());
    }
    
    private static int singleTimeString(int a, char[] b) {
//        log.info("传入参数 --> {}", a);
//        Arrays.asList(b).forEach(x -> log.info("传入参数 char[] --> {}", x));
        
        Deque<Integer> queue = new ArrayDeque<>();
        
        int base = 0, carry = 0; 
        int times = 1;
        for(int i = b.length - 1; i >= 0; i--) {
            int cur = b[i] - '0';
            base = (cur * a + carry) % 10 * times;
            carry = (cur * a + carry) / 10;
            
            queue.addFirst(base);
            times *= 10;
        }
        if(carry > 0) queue.addFirst(carry);
//        queue.stream().forEach(val -> log.info("queue 状态 --> {}", val));
        
        int ans = 0; // times = 1;
        while(!queue.isEmpty()) {  // 求和即可, 不要倍数  上面已经合并 
            int num = queue.pollLast();  // 获取并移除 
            ans += num;// * times;
            
            //times *= 10;
        }
        
        log.info("单次乘法结果 --> {}", ans);
        
        return ans;
    }
    
    // 两数相加 
    private static void plusNumber(String x, String y) {
        
        Deque<Integer> ans = new ArrayDeque<>();
        
        // 忽略 字符串数字校验 比如开头不能 0
        char[] a = x.toCharArray();
        char[] b = y.toCharArray();
        int m = a.length;
        int n = b.length;
        
        int base = 0, carry = 0;  // 基数和进位 
        int i = m - 1, j = n - 1;
        
        while( i >= 0 && j >= 0) {
            int first = a[i] - '0';
            int second = b[j] - '0';
            int sum = first + second + carry;
            base = sum % 10;
            carry = sum / 10;
            
            ans.addFirst(base);
            i--; j--;
        }
        
        // 长度较长的数字 
        while(i >= 0) {
            int first = a[i] - '0';
            int sum = first + carry;
            base = sum % 10;
            carry = sum / 10;
            
            ans.addFirst(base);
            i--;
        }
        while(j >= 0) {
            int second = b[j] - '0';
            int sum = second + carry;
            base = sum % 10;
            carry = sum / 10;
            
            ans.addFirst(base);
            j--;
        }
        
        // 最后一个进位判断 
        if(carry > 0) ans.addFirst(carry);
        
        log.info("最终结果 --> {}", ans.toString());
    }
    
    
    // 给出一段字符串 且分出满足 ip地址范围的情况
    // 输入：s = "25525511135"
    // 输出：["255.255.11.135","255.255.111.35"] 
    // 输入：s = "101023"
    // 输出：["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]
    
    // 使用回溯法   具体的实现省略, 只做思路练习 
    private static List<List<String>> ans = new LinkedList<>();
    private static void restoreIpAddress(String nums) {
        // 第一层选择可能的情况  进入递归 下一层, 出来后移除进行下一个情况 
        // dfs  deep first search 
        
    }
    private static void dfs(String subNums, List<String> curIp) {
        
        if(curIp.size() >= 4) {
            ans.add(curIp);  // 找到满足条件的 
            return;
        }
        
        for(int i = 0; i < subNums.length(); i++) {
            // 切分一种情况 
            // 进入下一层 
            // 还原 
        }
    }
    
}










