package com.eron.algorithms.strategy;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 排序算法集合练习
 * 冒泡排序 + 插入排序 -- 思路类似
 * 快速排序 -- 二分思想
 * 堆排序 -- 大小堆思路 ...
 * 其他 ：shell排序，归并排序(fork-join结构)
 */
public class Sort {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sort.class);

    public static void main(String[] args) {
        int[] arr = new int[]{
            9, 8, 7, 6, 5, 4, 3, 2, 1, 0, -1, -2, -3, -4, -5, -6, -7, -8, -9
        };

        // 升序
        Sort sort = new Sort();

        sort.shuffleArr(arr);
        LOGGER.info("bubble sort --> {}", Arrays.stream(sort.bubbleSort(arr)).boxed().collect(
            Collectors.toList()));

        sort.shuffleArr(arr);
        LOGGER.info("insert sort --> {}", Arrays.stream(sort.insertSort(arr)).boxed().collect(
            Collectors.toList()));

        sort.shuffleArr(arr);
        LOGGER.info("quick sort --> {}", Arrays.stream(sort.quickSort(arr)).boxed().collect(
            Collectors.toList()));

        sort.shuffleArr(arr);
        LOGGER.info("heap sort --> {}",
            Arrays.stream(sort.heapSort(arr)).boxed().collect(Collectors.toList()));
    }

    // 交换数组两个位置的值
    private void swapArr(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // 打乱数组
    private void shuffleArr(int[] arr) {
        Random random = new Random();
        for (int i = 0; i < arr.length; i++) {
            int tempIndex = random.nextInt(i, arr.length);
            this.swapArr(arr, i, tempIndex);
        }

        LOGGER.info("shuffled arr --> {}", Arrays.stream(arr).boxed().collect(Collectors.toList()));
    }

    // 冒泡排序
    private int[] bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    this.swapArr(arr, i, j);
                }
            }
        }

        return arr;
    }

    // 插入排序
    private int[] insertSort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = i - 1; j >= 0; j--) {
                if (arr[j + 1] >= arr[j]) {
                    break;
                }

                this.swapArr(arr, j, j + 1);
            }
        }

        return arr;
    }

    // 快速排序
    private int[] quickSort(int[] arr) {
        // 快速排序/二分排序
        this.quickSortCore(arr, 0, arr.length - 1);
        return arr;
    }

    private void quickSortCore(int[] arr, int left, int right) {
        if (left < right) {
            int i = left;
            int j = right;
            int mid = arr[i];

            while (i < j) {
                while (i < j && arr[j] >= mid) {
                    j--;
                }
                while (i < j && arr[i] <= mid) {
                    i++;
                }

                if (i < j) {
                    this.swapArr(arr, i, j);
                }
            }

            arr[left] = arr[i];
            arr[i] = mid;
            quickSortCore(arr, left, i - 1);
            quickSortCore(arr, i + 1, right);
        }
    }

    // 堆排序
    private int[] heapSort(int[] arr) {
        // 堆排序，大/小堆的使用
        int n = arr.length - 1;
        this.buildHeap(arr, n); // 构建最大堆
        this.swapArr(arr, n, 0);

        for (int i = n - 1; i >= 0; i--) {
            this.heapify(arr, i, 0);
            this.swapArr(arr, i, 0);
        }

        return arr;
    }

    private void heapify(int[] arr, int n, int i) {
        if (i > n) {
            return;
        }

        // 对总长度为n的数组 tree，从i节点开始调整堆
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        int max = i;
        if (left <= n && arr[max] < arr[left]) {
            max = left;
        }
        if (right <= n && arr[max] < arr[right]) {
            max = right;
        }

        if (max != i) {
            // 如果parent不是最大的 交换
            this.swapArr(arr, max, i);
            this.heapify(arr, n, max);
        }
    }

    private void buildHeap(int[] arr, int n) {
        int parent = (n - 1) / 2;
        for (int i = parent; i >= 0; i--) {
            this.heapify(arr, n, i);
        }
    }

    // 使用堆的数据结构 实现优先级队列
    private static class HeapPriorityQueue {
        private Integer[] arr = new Integer[10];
        int size = 0;

        public HeapPriorityQueue(){}

        public HeapPriorityQueue(int initCapacity) {
            arr = new Integer[initCapacity];
        }

        public void insert(int data) {
            size++;

            int curIndex = size - 1;
            arr[curIndex] = data;
            this.swim(curIndex);
        }

        public int deleteMax() {
            int data = arr[0];

            int lastIndex = size - 1;
            this.swap(0, lastIndex);
            this.arr[lastIndex] = null; // 重置数据, 填空
            size--;

            this.sink(0);

            return data;
        }

        private void sink(int i) { // 同 heapify 方法，下沉
            while (i < size) {
                int c1 = 2 * i + 1;
                int c2 = 2 * i + 2;

                int max = i;
                if (c1 < size - 1 && arr[max] < arr[c1]) {
                    max = c1;
                }
                if (c2 < size - 1 && arr[max] < arr[c2]) {
                    max = c2;
                }

                if (max == i) {  // 如果没有变化，后面也不会变化
                    break;
                }

                this.swap(max, i);
                i = max;
            }
        }

        // 从 i 节点 上浮到堆顶
        private void swim(int i) {
            // 从i 计算parent节点，直到到堆顶
            while (i > 0) {
                int parent = (i - 1) / 2;
                if (arr[parent] >= arr[i]) {
                    break;
                }
                this.swap(parent, i);
                i = parent;
            }
        }

        private void swap(int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }

        @Override
        public String toString() {
            return Arrays.stream(arr)
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(", "));
        }
    }

}
