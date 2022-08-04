/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.strategy;

import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ERON_AMD
 */
public class PrintX {
    
    private static Logger log = LoggerFactory.getLogger(PrintX.class);
    
    public static void main(String[] args) {
        int[] arr = new int[]{1, 0, 9, 0, 8, 3};
        log.info("之前 : {}", arrToString(arr));
        moveZero(arr);
        log.info("之后 : {}", arrToString(arr));
        
        printer(7);
    }
    
    // 打印星花矩阵
    public static void printer(int n){
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                if(i==j || i+j == n-1){
                    System.out.print(" * ");
                }else{
                    System.out.print("  ");
                }
            }
            System.out.println();
        }
    }
    
    // 数组中的0元素全部移动到最后  如 [1, 2, 3, 0, 4] --> [1, 2, 3, 4, 0]  等等
    public static void moveZero(int[] arr){
        int i = 0, j = 0;
        
        while(arr[i] != 0 && j < arr.length){
            i++;
            j++;
        }
        
        j++;
        while(j < arr.length){
            if(arr[j] == 0){
                j++;
                continue;
            }
            swap(arr, i, j);
            i++;
            j++;
        }
    }
    
    public static void swap(int[] arr, int i, int j){
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    
    public static String arrToString(int[] arr){
        StringJoiner sj = new StringJoiner(";");
        for(int x : arr){
            sj.add(Integer.toString(x));
        }
        
        return sj.toString();
    }
    
}



