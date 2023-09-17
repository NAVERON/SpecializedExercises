/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.sortting;

import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ERON_AMD
 * 希尔排序
 */
public class ShellSort {

    private static Logger log = LoggerFactory.getLogger(ShellSort.class);

    public static void main(String[] args) {
        int[] arr = new int[]{4, 2, 3, 1};
        sort(arr);
        log.info("final : {}", arrToString(arr));
    }

    public static String arrToString(int[] arr) {
        StringJoiner sj = new StringJoiner(",");
        for (int x : arr) {
            sj.add(Integer.toString(x));
        }

        return sj.toString();
    }

    public static void sort(int[] arr) {
        int gap = 1;
        while (gap < arr.length) {  // 分组公式一般   3x + 1 // 样本量比较少的时候 使用2x
            gap = gap * 3 + 1;
        }
        while (gap > 0) {
            for (int i = gap; i < arr.length; i++) { // 同gap的每个分组
                int tmp = arr[i];
                int j = i - gap;
                while (j >= 0 && arr[j] > tmp) {  // 没有越界  且前面的大, 交换   找到i应该放在哪里 ==>  插入排序
                    arr[j + gap] = arr[j];  // 同组的  i之前的全部排序
                    j -= gap;
                }
                arr[j + gap] = tmp;
            }
            gap = gap / 3;  // 缩小组间距
        }
    }

}



