package com.eron.algorithms.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.xml.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用递归的思想 方便解决一些问题
 */
public class RecursionSolve {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecursionSolve.class);

    public RecursionSolve() {
    }

    public static void main(String[] args) {
        RecursionSolve recursionSolve = new RecursionSolve();

        // 汉诺塔问题
        recursionSolve.hanoiTower(10, "A", "B", "C");
        // 斐波那契 数列计算
        LOGGER.info("斐波那契数列计算结果 --> {}", recursionSolve.fibonacciSequence(10));

        LOGGER.info("背包问题的 递归实现 --> {}",
            recursionSolve.recursionKnapSack(3, 50, new int[]{10, 20, 30},
                new int[]{60, 100, 120}));
        LOGGER.info("背包问题, 最大价值 --> {}",
            recursionSolve.knapsackOptimize(3, 50, new int[]{10, 20, 30}, new int[]{60, 100, 120}));
        LOGGER.info("数字的阶乘实现 --> {}", recursionSolve.customPower(2, 10));
        LOGGER.info("爬楼地，限制每次最大层数，求可能的情况 --> {}",
            recursionSolve.ladderCalCount(10, 5));
        LOGGER.info("同爬楼地问题，每次只能爬 1-2 层，求可能的情况数 --> {}",
            recursionSolve.ladderStepCount(12, new HashMap<>()));
        LOGGER.info("找零钱问题, 货币的种类，拼凑出需要的金额 --> {}",
            recursionSolve.coinChange(new int[]{1, 2, 5, 10}, 34));
        LOGGER.info("零钱兑换 2 一维数组解法 --> {}",
            recursionSolve.coinChange2(new int[]{1, 2, 5, 10}, 34));
    }

    // 汉诺塔问题 把n个圆盘 从 'from' 上移到 'to'上
    private final List<String> hanoiTowerSteps = new ArrayList<>();

    public void hanoiTower(int n, String from, String buffer, String to) {
        if (n == 1) {
            hanoiTowerSteps.add(String.format("把 %d 从 %s 移动到 %s 上, step --> %d", n, from, to,
                hanoiTowerSteps.size()));
            return;
        }

        this.hanoiTower(n - 1, from, to, buffer);
        this.hanoiTower(1, from, buffer, to);
        this.hanoiTower(n - 1, buffer, from, to);
    }

    // 斐波那契数列问题解决 0 1 1 2 3 5 ...
    public int fibonacciSequence(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n < 0 is not allowed ...");
        }

        if (n == 0) {
            return 0;
        }
        if (n == 1) {
            return 1;
        }

        return this.fibonacciSequence(n - 1) + this.fibonacciSequence(n - 2);
    }

    // 背包问题 N个物体 M总重量 weight每个物体的重量 val每个物体的价值
    // 使用回调方式实现 解决背包问题, 没有dp 记录之前的计算结果
    public int recursionKnapSack(int N, int W, int[] weight, int[] value) {
        if (N == 0 || W == 0) {
            return 0;
        }

        if (weight[N - 1] > W) {
            // 如果背包剩余可背重量 < 当前东西的重量, 不可背
            return this.recursionKnapSack(N - 1, W, weight, value);
        }

        return Math.max(
            value[N - 1] + this.recursionKnapSack(N - 1, W - weight[N - 1], weight, value),
            this.recursionKnapSack(N  -1, W, weight, value)
        );
    }
    // 使用 动态规划 优化递归的迭代问题
    public int knapsackOptimize(int N, int W, int[] weight, int[] value) {
        int[][] dp = new int[N + 1][W + 1];

        for (int i = 0; i < N + 1; i++) {
            for (int j = 0; j < W + 1; j++) {
                dp[i][j] = -1;
            }
        }

        return this.knapScakRec(N, W, weight, value, dp);
    }
    private int knapScakRec(int N, int W, int[] weight, int[] value, int[][] dp) {
        if (N == 0 || W == 0) {
            return 0;
        }

        if (dp[N][W] != -1) {
            return dp[N][W];
        }

        if (weight[N - 1] > W) {
            return dp[N][W] = this.knapScakRec(N - 1, W, weight, value, dp);
        }

        return dp[N][W] = Math.max(
            value[N - 1] + this.knapScakRec(N - 1, W - weight[N - 1], weight, value, dp),
            this.knapScakRec(N - 1, W, weight, value, dp)
        );
    }

    // 实现 幂乘 计算
    public double customPower(double base, int exponent) {
        if (base - 0 < 0.001) { // 特殊情况处理
            return base;
        }

        if (exponent < 0) { // 幂次 为 负数，需要颠倒下
            base = 1 / base;
            exponent = -1 * exponent;
        }

        return this.quickMul(base, exponent);
    }
    private double quickMul(double base, int exponent) { // 幂乘核心
        if (exponent == 0) {
            return 1.0D;
        }
        double result = quickMul(base, exponent >> 1); // exp/2 计算, 持续迭代，直到 = 0
        if ((exponent & 1) == 1) { // 奇数
            result = result * result * base;
        } else {
            result = result * result;
        }

        return result;
    }

    // 爬楼地问题 迭代，动态规划思路
    // 爬n层楼梯，每次最大m层，求可能情况
    public int ladderCalCount(int ladder, int maxJump) {
        int jump = 0;
        if (ladder == 0) {
            return 1;
        }
        if (ladder >= maxJump) {
            // 需要多步
            for (int i = 1; i <= maxJump; i++) {
                jump += this.ladderCalCount(ladder - i, maxJump);
            }
        } else {
            // 最小一部以内
            jump = this.ladderCalCount(ladder, ladder);
        }

        return jump;
    }

    // 有10级台阶，每次只能1-2步   动态规划的思路
    public int ladderStepCount(int ladder, Map<Integer, Integer> map) {
        if (ladder < 1) {
            return 0;
        }
        if (ladder < 2) {
            return 1;
        }

        if (map.containsKey(ladder)) {
            return map.get(ladder);
        } else {
            int jump =
                this.ladderStepCount(ladder - 1, map) + this.ladderStepCount(ladder - 2, map);
            map.put(ladder, jump);
            return jump;
        }
    }

    // LeetCode第322题：零钱兑换 --> 类似于背包问题
    public int coinChange(int[] coins, int amount) {
        // arr 表示当前有几种钱币额数, 如 [1, 2, 5, 10 ...]
        // amount 表示 可以用钱币拼出的目标金额
        // 问题是求出可以使用限定的钱币 最少的数量给出 目标金额
        int N = coins.length; // 硬币的种类数量
        // 初始化dp
        int[][] dp = new int[N][amount+1]; // 使用几种硬币 表示 amount 最小硬币数
        Arrays.fill(dp[0], -1);
        for (int i = 0; i * coins[0] <= amount; i++) {
            dp[0][i * coins[0]] = i;
        }

        for (int i = 1; i < coins.length; i++) {
            for (int j = 0; j < amount + 1; j++) {
                if (j >= coins[i]) { // 总金额 > 当前使用的钱币, 否则无法拼出
                    // 所有的情况：使用i钱币，不使用i钱币; 总金额减少 ...
                    if (dp[i][j - coins[i]] == -1) { // 当前钱币 无法拼出目标金额
                        dp[i][j] = dp[i - 1][j];
                    } else if (dp[i - 1][j] == -1) {
                        dp[i][j] = dp[i][j - coins[i]] + 1;
                    } else {
                        dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - coins[i]] + 1);
                    }
                } else {
                    dp[i][j] = dp[i - 1][j]; // 只能使用 前面的硬币种类
                }
            }
        }

        return dp[coins.length  -1][amount];
    }

    // 零钱兑换 问题，单数组实现 --> 将dp二位数组 压平计算
    public int coinChange2(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, -1);
        dp[0] = 0;
        for(int i = 0; i < coins.length; i++) {
            for (int j = coins[i]; j < amount + 1; j++)  {
                if (dp[j - coins[i]] != -1) {
                    if (dp[j] == -1) {
                        dp[j] = dp[j - coins[i]] + 1;
                    } else {
                        dp[j] = Math.min(dp[j], dp[j - coins[i]] + 1);
                    }
                }
            }
        }

        return dp[amount];
    }
}
