package com.eron.algorithms.strategy;


/**
 * 判断一个数组可以且分成多少个字数组 
 * 要求 ：切分后各个部分排序 整体依然是增序排列 
 * @author eron
 * 关键点 : 切分的条件是 切分点前面的最大值不能大于后面的最小值 
 */
public class ArrayCanSplitParts {

    public static void main(String[] args) {
        int[] arr = new int[] {1, 2, 5, 6, 10};
        int res = new ArrayCanSplitParts().canSplitNums(arr);
        System.out.println("当前答案 --> " + res);
    }
    
    public int canSplitNums(int[] arr) {
        int n = arr.length;
        int res = 0;
        int[] leftMax = new int[n]; leftMax[0] = arr[0];
        int[] rightMin = new int[n]; rightMin[n - 1] = arr[n - 1];
        
        int max = arr[0];
        for(int i = 1; i <= n; i++) {
            if(arr[i - 1] > max) {
                max = arr[i - 1];
            }
            leftMax[i - 1] = max;
        }
        
        int min = arr[n - 1];
        for(int i = n - 2; i >= -1; i--) {
            if(arr[i + 1] < min) {
                min = arr[i + 1];
            }
            rightMin[i + 1] = min;
        }
        printArray(leftMax);
        printArray(rightMin);
        // 从1 - n 可以切 
        for(int i = 1; i < n; i++) {
            if(leftMax[i] <= rightMin[i]) {
                res++;
            }
        }
        
        return res;
    }
    
    public void printArray(int[] arr) {
        System.out.println("输出arr ----> " + arr.length);
        for(int i = 0; i < arr.length; i++) {
            System.out.print(" " + arr[i]);
        }
        System.out.println();
    }
    
    
}





