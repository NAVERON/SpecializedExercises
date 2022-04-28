/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.strategy;

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
    
}
