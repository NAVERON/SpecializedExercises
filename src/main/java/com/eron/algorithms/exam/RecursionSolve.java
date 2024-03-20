package com.eron.algorithms.exam;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用递归的思想 方便解决一些问题
 */
public class RecursionSolve {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecursionSolve.class);

    public static void main(String[] args) {
        RecursionSolve recursionSolve = new RecursionSolve();

        // 汉诺塔问题
        recursionSolve.hanoiTower(10, "A", "B", "C");
        // 斐波那契 数列计算
        LOGGER.info("斐波那契数列计算结果 --> {}", recursionSolve.fibonacciSequence(10));

    }

    // 汉诺塔问题 把n个圆盘 从 'from' 上移到 'to'上
    private int hanoiTowerStepCount = 0;
    public void hanoiTower(int n, String from, String buffer, String to) {
        if (n == 1) {
            hanoiTowerStepCount++; // 记录步数
            // 最后一步
            LOGGER.info("把 {} 从 {} 移动到 {} 上, 第 {} 步", n, from, to, hanoiTowerStepCount);
            return;
        }

        this.hanoiTower(n-1, from, to, buffer);
        this.hanoiTower(1, from, buffer, to);
        this.hanoiTower(n-1, buffer, from, to);
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
    public void knapsackProblem(int N, int W, int[] weight, int[] val) {
        int[][] dp = new int[N + 1][W + 1];
        for (int i = 0; i < N; i++) {
            dp[i][0] = 0;
        }
        for (int i = 0; i < W; i++) {
            dp[0][i] = 0;
        }

        for (int i = 1; i < N + 1; i++) {
            for (int j = 1; j < W + 1; j++) {
                if (weight[i - 1] > j) {
                    // 包容量不够
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = Math.max(
                        // 装入
                        val[i - 1] + dp[i - 1][j - weight[i - 1]],
                        // 不装入
                        dp[i - 1][j]
                    );
                }
            }
        }

        for (int i = 0; i < N + 1; i++) {
            for (int j = 0; j < W + 1; j++) {
                System.out.print("  " + dp[i][j]);
            }
            System.out.println();
        }
        LOGGER.info("背包 N W = {}", dp[N][W]);
    }

    // 实现 幂乘 计算
    public double customePower(double base, int exponent) {
        if (base - 0 < 0.001) { // 特殊情况处理
            return base;
        }

        if (exponent < 0) { // 幂次 为 负数，需要颠倒下
            base = 1 / base;
            exponent = -1 * exponent;
        }

        return quickMul(base, exponent);
    }
    private double quickMul(double base, int exponent) { // 幂乘核心
        if (exponent == 0) {
            return 1.0D;
        }
        double result = quickMul(base, exponent >> 1); // exp/2 计算, 持续迭代，直到 = 0
        LOGGER.info("当前状态 : {}, {} -> {}", base, exponent, result);
        if ((exponent & 1) == 1) { // 奇数
            result = result * result * base;
        } else {
            result = result * result;
        }

        return result;
    }

    // 爬楼地问题 迭代，动态规划思路
    // 爬n层楼梯，每次最大m层，求可能情况
    public static int ladderCalCount(int ladder, int maxJump) {  // 可以使用dp以空间换实践
        int jump = 0;
        if (ladder == 0) {
            return 1;
        }
        if (ladder >= maxJump) {
            // 需要多步
            for (int i = 1; i <= maxJump; i++) {
                jump += ladderCalCount(ladder - i, maxJump);
            }
        } else {
            // 最小一部以内
            jump = ladderCalCount(ladder, ladder);
        }

        return jump;
    }

    // 有10级台阶，每次只能1-2步   动态规划的思路
    public static int ladderStepCount(int ladder, Map<Integer, Integer> map) {

        if (ladder < 1) {
            return 0;
        }
        if (ladder < 2) {
            return 1;
        }

        if (map.containsKey(ladder)) {
            return map.get(ladder);
        } else {
            int jump = ladderStepCount(ladder - 1, map) + ladderStepCount(ladder - 2, map);
            map.put(ladder, jump);
            return jump;
        }
    }

    // 输入可用零钱和总额，求解可搭配方案
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
