/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ERON_AMD
 */
public class ReverseString {
    
    private static Logger log = LoggerFactory.getLogger(ReverseString.class);
    
    public static void main(String[] args) {
        char[] test = "abcdefg".toCharArray();
        rotateString(test, 3);
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
