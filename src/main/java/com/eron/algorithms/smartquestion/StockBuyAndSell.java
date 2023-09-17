
package com.eron.algorithms.smartquestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 股票买卖最大收益的问题
 *
 * @author eron
 */
public class StockBuyAndSell {
    private static final Logger log = LoggerFactory.getLogger(StockBuyAndSell.class);

    public static void main(String[] args) {
        int[] arr = new int[]{
                1, 2, 4, 6, 8, 12, 4, 7, 4, 9, 18, 0, 13
        };

        StockBuyAndSell stockBuyAndSell = new StockBuyAndSell();
        stockBuyAndSell.stockProblems(arr);

        // *** 标准实现 单个股票买卖
        stockBuyAndSell.standardStockSellSolve(arr);  // 标准全局股票问题 

    }

    // 自己尝试实现  这个收益只是表示一段内的,真正的实现需要所有全局的最大值最小值
    public void stockProblems(int[] arr) {  // 传入股票一段时间的记录  arr 长度大于等于2 
        int i = 0;
        int j = i + 1;
        int maxSell = arr[j] - arr[i];
        int maxi = i;
        int maxj = j;  // 记录最大收益的索引范围
        // 保存一系列收益最大的瞬间 
        List<Integer> maxs = new ArrayList<>();
        // i 一定小于 j 因为先买再卖 
        while (j < arr.length - 1) {
            log.info("当前j -> {}", j);
            int curDiff = arr[j] - arr[i];
            if (arr[j] <= arr[j + 1]) {
                if (arr[i] > arr[i + 1]) {  // 这里简单特例处理
                    i++;
                }
                j++;
                curDiff = arr[j] - arr[i];
                log.info("更新最大收益 -> {}, {}, {}, 之前的最大收益={}", i, j, curDiff, maxSell);
                maxs.add(curDiff);
            } else {
                i = j;
                j++;
                curDiff = arr[j] - arr[i];
            }
            if (maxSell <= curDiff) {
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
        // arr = new int[] {7,1,5,3,6,4}; 这种情况下可以 但是特殊情况下 
        // 找到全局的最大值和最小值即可 
        int min = Integer.MAX_VALUE, max = 0;  // min 最小值 max-> 最大收益 
        int lastMin = 0, minIndex = 0, maxIndex = 0;
        for (int i = 0; i < arr.length; i++) {
            if (min > arr[i]) {
                min = arr[i];
                lastMin = i;
            } else if (max < arr[i] - min) {
                max = arr[i] - min;
                minIndex = lastMin;
                maxIndex = i;
            }
        }
        log.info("股票最大 => {}, 以及对应的索引 -> {}, {}", max, minIndex, maxIndex);
    }


    // 多次买卖股票问题 
    // 贪心算法 每次交易都能够正向收益即可 
    private static void MultiSellStock(int[] prices) {
        int ans = 0;
        for (int i = 1; i < prices.length; i++) {
            int diff = prices[i] - prices[i - 1];
            if (diff > 0) {
                ans += diff;
            }
        }
    }


}










