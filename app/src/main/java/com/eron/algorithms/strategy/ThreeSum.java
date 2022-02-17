/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ERON_AMD
 *  这里作为部分算法的集合, 快速写出思路
 */
public class ThreeSum {
    
    private static Logger log = LoggerFactory.getLogger(ThreeSum.class);

    public static void main(String[] args) {
        int[] numbers = new int[]{-1,0,1,2,-1,-4};
        List<Integer[]> result = threeSum(numbers);
        for(Integer[] x : result){
            log.info("结果 : {}", arrToString(x));
        }
        
    }
    
    public static String arrToString(Integer[] arr){
        StringJoiner sj = new StringJoiner(",");
        for(int x : arr){
            sj.add(Integer.toString(x));
        }
        
        return sj.toString();
    }

    // 这里借鉴的两数之和的方法
    public static List<Integer[]> threeSum(int[] arr) {  // 有重复的, 比较难解决
        if(arr.length < 3){  // 前置条件， 必须由3个数
            return new ArrayList<>();
        }
        
        List<Integer[]> totalResult = new LinkedList<Integer[]>();

        for (int i = 0; i < arr.length; i++) {
            int target = 0 - arr[i];
            Map<Integer, Integer> record = new HashMap<>();
            for (int j = 0; j < arr.length; j++) {
                if (i == j) {
                    continue;
                }
                if (record.containsKey(target - arr[j])) {
                    totalResult.add(new Integer[]{i, j, record.get(target - arr[j])});
                    break;
                }

                record.put(arr[j], j);
            }
        }
        
        return totalResult;
    }
    
    /**
        *   借鉴两数之和， 套在里面
        * @param arr
        * @param sum
        * @param exceptIndex
        * @return  索引数组 , 其实最终的结果就是    返回的一个二维数组和exceptIndex 
        */
    public static Integer[] twoSum(int[] arr, int sum, int exceptIndex) {
        Integer[] result = new Integer[2];
        Map<Integer, Integer> record = new HashMap<Integer, Integer>();
        
        for(int i = 0; i < arr.length; i++){
            if(i == exceptIndex){
                continue;
            }
            
            if(record.containsKey(sum - arr[i])){
                result = new Integer[]{record.get(sum-arr[i]), i};
                break;
            }
            record.put(arr[i], i);
        }
        
        return result;
    }

}
