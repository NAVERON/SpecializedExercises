package com.eron.algorithms.sortting;

import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 冒泡排序算法
 *
 * @author ERON_AMD
 */
public class BubbleSort {

    private static final Logger log = LoggerFactory.getLogger(BubbleSort.class);

    public static void main(String[] args) {
        // 冒泡排序
        int[] arr = {10, 9, 8, 7, 6, 5, 4, 3, 2, 2, 1, 11, 1, 0, -4};
        //bubbleProcess(arr);
        String a = "";
        int[] arr_two = {10, 9, 8, 0, 5, 6, 8, 90, 6, 5, 4, 3, 2, 2, 1, 11, 1, 0, -4};
        // customeBubbleProcess(arr_two);
//        sort2(arr);
//        sort2(arr_two);

        // 打印排序结果
        log.info("第一個排序結果 : {}", arrToString(arr));

        log.info("第二個排序結果 : {}", arrToString(arr_two));

        insertSort(arr_two);
        log.info("插入排序 : {}", arrToString(arr_two));
    }

    public static void bubbleProcess(int[] arr) {
        // 冒泡排序法
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j + 1]) {  // 相邻两个交换   如果第一个索引固定不变， 不能保证第一层索引的有序性  最大的数一直往下沉
                    int tmp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = tmp;
                }
            }
        }
    }

    public static void customeBubbleProcess(int[] arr) {  // 这种比较好理解  
        // 依次抽出一个，与其他的比较，发现小的就方上面  这样每个数都与其他数作了比较
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {  // 从大到小排序
                    int tmp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = tmp;
                }
            }
        }
    }

    public static void sort2(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) { // xxoxxxx  o和o之后的x比较 
                if (arr[j] < arr[j + 1]) {
                    swap(arr, j, j + 1);
                }
            }
        }
    }

    public static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static String arrToString(int[] arr) {
        StringJoiner sj = new StringJoiner(";");

        for (int x : arr) {
            sj.add(Integer.toString(x));
        }

        return sj.toString();
    }

    public static void insertSort(int[] arr) {
        // 插入排序 与冒泡排序的思路类似 一次选择一张，与已经排序过的比较, 指导最后所有的都走过一遍
        for (int i = 0; i < arr.length; i++) {
            int temp = arr[i];
            for (int j = i - 1; j >= 0; j--) {  // 从前往后拿一张  后，与已经拍过序的比较，找到位置
                // 从最后的位置开始(先跟最大的)比较
                if (temp > arr[j]) { // 比最大的还大，放在最后就行，接着往后
                    break;
                }
                if (temp <= arr[j]) {
                    swap(arr, j + 1, j);
                }
            }
        }
    }
}
