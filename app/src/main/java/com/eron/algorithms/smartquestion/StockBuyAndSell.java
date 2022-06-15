
package com.eron.algorithms.smartquestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 股票买卖最大收益的问题 
 * @author eron 
 * 
 */
public class StockBuyAndSell {
    private static final Logger log = LoggerFactory.getLogger(StockBuyAndSell.class);
    public static void main(String[] args) {
        int[] arr = new int[] {
            1, 2, 4, 6, 8, 12, 4, 7, 2, 9, 18, 0, 13
        };
        
        StockBuyAndSell stockBuyAndSell = new StockBuyAndSell();
        stockBuyAndSell.stockProblems(arr);
        
    }
    // 自己尝试实现 
    public void stockProblems(int[] arr) {  // 传入股票一段时间的记录  arr 长度大于等于2 
        int i = 0; 
        int j = i + 1;
        int maxSell = arr[j] - arr[i];
        int maxi = i; int maxj = j;  // 记录最大收益的索引范围 
        // 保存一系列收益最大的瞬间 
        List<Integer> maxs = new ArrayList<>();
        // i 一定小于 j 因为先买再卖 
        while(j < arr.length - 1) {
            log.info("当前j -> {}", j);
            int curDiff = arr[j] - arr[i];
            if(arr[j] <= arr[j+1]) {
                if(arr[i] > arr[i+1]) {  // 这里简单特例处理 
                    i++;
                }
                j++;
                curDiff = arr[j] - arr[i];
                log.info("更新最大收益 -> {}, {}, {}, 之前的最大收益={}", i, j, curDiff, maxSell);
                maxs.add(curDiff);
            }else {
                i = j;
                j++;
                curDiff = arr[j] - arr[i];
            }
            if(maxSell <= curDiff) {
                maxSell = curDiff;
                maxi = i;
                maxj = j;
            }
            // maxSell = Math.max(maxSell, curDiff);
        }
        // 做个最大收益的时间段 
        // Collections.sort(maxs);
        maxs.forEach(t -> log.info("---{}", t));
        log.info("最大收益 --> {}, 索引位置 -> {}, {}", maxSell, maxi, maxj);
    }
    
    public void standardStockSellSolve(int[] arr) {
        
    }
    
}










