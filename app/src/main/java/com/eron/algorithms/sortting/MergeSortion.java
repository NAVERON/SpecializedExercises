/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.sortting;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MergeSortion {  // 归并排序
    
    private static final Logger log = LoggerFactory.getLogger(MergeSortion.class);

    public static void mergeSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }
        mergeSort(arr, 0, arr.length - 1);
    }

    public static void mergeSort(int[] arr, int l, int r) {
        if (l == r) {
            return;
        }
        int mid = l + ((r - l) >> 1);
        mergeSort(arr, l, mid);  // 对划分的左部分排序
        mergeSort(arr, mid + 1, r);  // 对右部分排序
        merge(arr, l, mid, r);
    }

    public static void merge(int[] arr, int l, int mid, int r) {  // 把每一个分组分到不能再分， 然后归并   治
        int[] help = new int[r - l + 1];
        int count = 0;
        int p1 = l;
        int p2 = mid + 1;
        while (p1 <= mid && p2 <= r) {  // 如果两个头指针都没有越界  则继续合并
            help[count++] = arr[p1] < arr[p2] ? arr[p1++] : arr[p2++];  // 找出2部分中较小的
        }
        while (p1 <= mid) {  // 如果左边的没有空， 则直接复制到后面
            help[count++] = arr[p1++];
        }
        while (p2 <= r) {  // 如果右边没有空  则复制到后面 不需要比较了，因为另一半已经合并完成了 
            help[count++] = arr[p2++];
        }
        
        for (int i = 0; i < help.length; i++) {  // 把所有排序好的  复制到原始array中 
            arr[l + i] = help[i];
        }
    }

    public static void printArray(int[] arr) {
        if (arr == null) {
            return;
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }

        log.info("\n");
    }

    public static int[] copyArray(int[] arr) {
        if (arr == null) {
            return null;
        }
        int[] res = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            res[i] = arr[i];
        }
        return res;
    }

    public static boolean isEqual(int[] arr1, int[] arr2) {
        if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
            return false;
        }
        if (arr1 == null && arr2 == null) {
            return true;
        }
        if (arr1.length != arr2.length) {  // 提前判断
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static int[] generateRandomArray(int maxSize, int maxValue) {
        int[] arr = new int[(int) ((maxSize + 1) * Math.random())];  // 随机大小的数组
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());  // 两个随机数运算
        }
        return arr;
    }

    public static void main(String[] args) {
        int testTime = 1;
        int maxSize = 100;
        int maxValue = 100;
        boolean succeed = true;
        for (int i = 0; i < testTime; i++) {
            int[] arr1 = generateRandomArray(maxSize, maxValue);
            int[] arr2 = copyArray(arr1);
            mergeSort(arr1);
            Arrays.sort(arr2);
            if (!isEqual(arr1, arr2)) {
                succeed = false;
                printArray(arr1);
                printArray(arr2);
                break;
            }
        }
        System.out.println(succeed ? "归并排序结果正确!" : "错误!");

        // 上面测试   自己做的和Arrays.sort是不是一样的算法
        int[] arr = generateRandomArray(maxSize, maxValue);
        printArray(arr);
        mergeSort(arr);
        printArray(arr);
    }
}
