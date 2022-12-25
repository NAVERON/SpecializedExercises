

package com.eron.algorithms.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *
 * @author ERON_AMD
 */
public class SplitCoin {

    private static final Logger log = LoggerFactory.getLogger(SplitCoin.class);

    public static void main(String[] args) {
        //  找零钱问题   递归
        int[] conis = {1, 3, 5, 10};
        int amount = 100;
        
    }

    public static int getWay(int[] arr, int aim) {
        if (aim <= 0 || arr.length <= 0 || arr == null) {
            return 0;
        }

        return simpleCoins(arr, 0, aim);
    }

    // 暴力搜索法
    public static int simpleCoins(int[] coins, int index, int amount) {
        int res = 0;
        if (index >= coins.length) {
            res = 0 == amount ? 1 : 0;
            return res;
        }

        for (int i = 0; i * coins[index] <= amount; i++) {
            res += simpleCoins(coins, index++, amount - i * coins[index]);
        }

        return res;
    }

    /**
        * 输入可用零钱和总额，求解可搭配方案
        *
        * @param coins 可找零钱
        * @param amount 总额
        */
    public static void coinChange(int[] coins, int amount) {
        //
    }

    
    //  ========================================================
    // 非暴力解法
    public static int countWays(int[] arr, int aim) {
        if (aim <= 0 || arr.length <= 0 || arr == null) {
            return 0;
        }
        /**
                * 创建一个全局的数组dp[][]用来存放计算的中间结果
                * 之所以长度为arr.length+1是因为当index=arr.length时，后续还会执行dp[index]的赋值
                * 如果不定义长度为length+1会出现下标越界
                */
        int[][] dp = new int[arr.length + 1][aim + 1];
        return process(arr, 0, aim, dp);
    }

    public static int process(int[] arr, int index, int aim, int[][] dp) {
        int res = 0;
        //定义边界条件
        if (index == arr.length) {
            res = aim == 0 ? 1 : 0;
            return res;
        } else {
            int mapValue = 0;//用来表示已经记忆的元素
            //对出现的元素进行枚举,即使用了i=0时表示使用0张1元的情况，即枚举res1 ,res2,res3...
            for (int i = 0; i * arr[index] <= aim; i++) {
                mapValue = dp[index + 1][aim - i * arr[index]];
                if (mapValue != 0) {
                    res += mapValue == -1 ? 0 : mapValue;
                } else {
                    res += process(arr, index + 1, aim - i * arr[index], dp);
                }
            }
        }
        dp[index][aim] = res == 0 ? -1 : res;  // dp问题都需要保存中间状态值，加速效果，key为   钱index和剩余钱index
        return res;
    }
    
}



