package com.eron.algorithms.sortting;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ERON_AMD 快速排序 选中一个基准值，比较厚前后交换
 */
public class QuickSort {

    private static final Logger log = LoggerFactory.getLogger(QuickSort.class);

    public static void main(String[] args) {

        int[] originArr = {4, 4, 3, 3, 8, 32, 1, 5, 8, 8, 45, 32};
        for (int num : originArr) {
            log.info("origin arr : {}", num);
        }

        sortProcess(originArr, 0, originArr.length - 1);

        log.info("排序后==========================");
        for (int num : originArr) {
            log.info("sortting after : {}", num);
        }
        
        method2Sort();

    }
    
    public static String arrayToString(int[] arr){
        // 公共方法, 输出数组为字符串数值
        StringBuilder sb = new StringBuilder();
        
        for(int a : arr){
            sb.append(a).append(",");
        }
        
        return sb.toString();
    }

    public static void sortProcess(int[] arr, int left, int right) { // 最左快排

        if (left >= right) {  // 需要限制边界
            return;
        }

        int i = left, j = right;
        int midNum = arr[i];

        while (i < j) {
            // 当i、j未相遇，继续比较和交换
            log.info("当前i j 索引 : {}, {}", i, j);

            while (arr[j] >= midNum && i < j) {
                j--;
            }

            while (arr[i] <= midNum && i < j) {
                i++;
            }

            if (i < j) {  // 杜绝两个索引到同一位置的问题
                arr[i] = arr[i] ^ arr[j];
                arr[j] = arr[i] ^ arr[j];
                arr[i] = arr[i] ^ arr[j];
            }
        }

        arr[left] = arr[i];
        arr[i] = midNum;

        sortProcess(arr, left, j - 1);
        sortProcess(arr, j + 1, right);
    }
    
    public static void method2Sort(){
        int[] aa = {1, 23, 45, 6, 7, 8, 10, 1, -1, -2};
        log.info("之前 : {}", arrayToString(aa));
        
        rightQuickSort(aa, 0, aa.length - 1);
        
        log.info("排序之后 : {}", arrayToString(aa));
    }
    
    public static void rightQuickSort(int[] arr, int start, int end){
        int partitionIndex = partition(arr, start, end);
        
        if(partitionIndex - 1 > start){
            rightQuickSort(arr, start, partitionIndex - 1);
        }
        if(partitionIndex + 1 < end){
            rightQuickSort(arr, partitionIndex + 1, end);
        }
    }
    
    public static int partition(int[] arr, int start, int end){
        
        int pivot = arr[end]; // 使用最后一个数做标准 
        
        for(int i = start; i < end; i++){  // 没有到最后的标准end位置, 所以后面需要调换两个, 将标准换到中间
            if(arr[i] < pivot){
                int temp = arr[i];
                arr[i] = arr[start];
                arr[start] = temp;
                
                start++;
            }
        }
        
        int temp = arr[start];
        arr[start] = arr[end];
        arr[end] = temp;
        
        return start;
    }

}
