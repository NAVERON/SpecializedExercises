package com.eron.algorithms.sortting;

import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ERON_AMD 堆排序 Java实现
 *
 * 如果使用一个数组表示 一个树, index = i, 父节点为 (i-1)/2, 左子节点为 2*i +1, 右子节点为 2*i + 2
 */
public class HeapSort {

    private static final Logger log = LoggerFactory.getLogger(HeapSort.class);

    public static void main(String[] args) {
        int[] arr = {4, 10, 3, 5, 1, 2, 89, 90, 10, 23, 21, 1};  // 原始待排序数组
        log.info("origin arr : {}", arrToString(arr));

        // buildHeap(arr, arr.length);
        // heapSort(arr, arr.length);  // 排序之前  需要创建一个heap
        PriorityQueue pq = new PriorityQueue(20);
        for(int x : arr){
            pq.insert(x);
        }
        log.info("输出大堆 : {}", pq.toString());
        int realSize = pq.size;
        for(int i = 1; i <= realSize; i++){
            int data = pq.delMax();
            log.info("堆一次输出最大值 : {}", data);
        }

        log.info("===========================");
        log.info("sorted : {}", arrToString(arr));
    }

    public static void heapSort(int tree[], int n) {

        buildHeap(tree, n);  // 一个有序的堆，最大值在根节点
        for (int i = n - 1; i > 0; i--) {  // 逐步将最后一个切除，最后一个是最大的，因为已经进行了heapify重建
            swap(tree, i, 0);  // 上面堆排序完成根节点是最大的
            heapify(tree, i, 0); // 重新建最大堆，数组长度为i，从跟点0开始   i 排序长度 0 需要重排的根节点 
        }

    }

    /**
     * 倒着将一个数组按照树结构排序
     *
     * @param tree 待排序的数组
     * @param n 数组长度，需要重排序的堆长度，因为后面会依次减少最后一个排序
     */
    public static void buildHeap(int[] tree, int n) {  // 根据输入的乱序数组重新排列 
        int lastNode = n - 1;
        int parent = lastNode / 2 - 1;  // 最后一个节点的父节点
        for (int i = parent; i >= 0; i--) {
            heapify(tree, n, i);
        }
    }

    public static void swap(int[] tree, int i, int j) {
        //将数组中i, j位置作交换
        int tmp = tree[i];
        tree[i] = tree[j];
        tree[j] = tmp;
    }

    /**
     * 树结构用数组表达  功能：i节点为根的子树  堆化
     *
     * @param tree 树 数组
     * @param n 树的最大长度，数组长度
     * @param i 根节点index
     */
    public static void heapify(int[] tree, int n, int i) {  // 顺序化 i节点为根的子数序列 
        if (i >= n) {
            return;
        }

        // 假设i索引处为父节点，求取三个中最大值  节点i的子节点 
        int child1 = 2 * i + 1;
        int child2 = 2 * i + 2;
        int max = i;

        if (child1 < n && tree[child1] > tree[max]) {
            max = child1;
        }
        if (child2 < n && tree[child2] > tree[max]) {
            max = child2;
        }

        // 使得最大值往根节点方向跑
        if (max != i) {  // 如果最大值发生变更，子节点上也需要进行重新排序
            // 如果最大值不是根节点，将根节点和子节点调换
            swap(tree, max, i);
            heapify(tree, n, max);  // 对于之前已经排过序的重新计算排序  max根节点heapify 
        }
    }
    
    public static String arrToString(int[] arr){
        StringJoiner sj = new StringJoiner(",");
        for(int x : arr){
            sj.add(Integer.toString(x));
        }
        
        return sj.toString();
    }

    // 堆排序,  优先级队列   所有的索引从1 开始 0忽略
    public static class PriorityQueue {

        int[] arr;
        int size;

        public PriorityQueue(int initCapcity) {
            arr = new int[initCapcity];
            size = 0;
        }
        
        public void insert(int data){ // 往最后插入  需要上浮 
            size++;
            arr[size] = data;
            swim(size);
        }

        public int delMax() {
            int maxNum = arr[1];  // 去除

            swap(1, size);
            arr[size] = 0;  // 根节点到作后取出来了  重置为 0
            size--;
            sink(1);

            return maxNum;
        }

        public void swim(int k) {  // 下标 从1 开始，方便计算 所以这里计算parent的公式 变为 parent = i/2 
            // 上浮 
            while (k > 1) {
                // 只要k大于1, 表示没有到root根
                if (arr[k / 2] < arr[k]) {  // 如果新加入的 大, 交换   否则不用动
                    swap(k / 2, k);
                    k = k / 2;
                } else {
                    break;
                }
            }
        }

        public void sink(int k) {  // 去除了根节点  需要从根节点向下heapify 
        	// 两个子节点  2i and 2i+1 
            while (2*k <= size) {
                int maxIndex = 2 * k;
                if (2 * k + 1 <= size && arr[2 * k] < arr[2 * k + 1]) {  // 两个子节点比较 
                    maxIndex = 2 * k + 1;
                }
                if (arr[maxIndex] <= arr[k]) {  // 较大的子节点和根节点比较
                    return;
                }
                swap(maxIndex, k);
                k = maxIndex;
            }
        }

        public void swap(int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        
        public String toString(){
            StringJoiner sj = new StringJoiner(",");
            for(int x : arr){
                sj.add(Integer.toString(x));
            }

            return sj.toString();
        }
    }

}
