package com.eron.algorithms.strategy.smartquestion;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 有2个水晶球 100层楼
 * 水晶球可能在某一层破裂，求破裂的最小楼层
 *
 * @author eron
 * <p>
 * 我的思考 ：
 * 第一个小球破碎后，第二个只能根据第一个小球得到的区间 从低到高测试，破裂的那一层就是答案
 * 二分法 但是代价太大, 如果第一次直接破碎，导致只能从第一层开始向上
 * 所以需要求的最合适的间距
 * 设楼层高度 n = 100 ， 破碎楼层 x，间距 m ，q 表示间距跳的次数, 可以得到一些计算公式
 * qm - x = m - x%m
 * 如果刚好在间距的整数倍， 则(x+m-x%(m+1))/m;  否则需要从上一个继续步进 (x+m-x%(m+1))/m + x%m;
 * <p>
 * 设定楼层 n不变，破碎答案 x = [1, 100]
 * 可以根据x 和m 的变化计算而为矩阵, 也就是x和m确定时，可以计算出步数
 */
public class FloorLevel {
    private static final Logger log = LoggerFactory.getLogger(FloorLevel.class);

    public static void main(String[] args) {
        FloorLevel floorLevel = new FloorLevel();
        // floorLevel.getMinCount();

        floorLevel.ballDrop(2, 100);
    }

    public void getMinCount() {  // 如果有 w 个球，k层楼呢？
        int n = 100;
        int[][] arr = new int[n][n];
        // arr[0][0] = 0; arr[0][1] = 0; arr[1][0] = 0;
        for (int m = 0; m < n; m++) {
            for (int x = 0; x < n; x++) {
                arr[x][m] = this.calcute(n, m, x);
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(arr[i][j] + " ");
            }
            System.out.println("\n");
        }
    }

    // n 楼层高度 m 跳跃间隔 x 最终水球破碎楼层  (x+m-x%m)/m + x%m
    private int calcute(int n, int m, int x) {
        int result = 0;
        if (m == 0 || x == 0) {
            return result;
        }
        result = (x + m - x % (m)) / m + x % (m);  // 因为刚好是整数倍 也只是指导在一个区间 需要步进的测试
        return result;
    }

    // 动态规划算法 解决 k个鸡蛋 n层楼最少几次可以获取最大不碎的楼层 
    AtomicInteger x = new AtomicInteger(0);
    private static Map<String, Integer> memo = new HashMap<>();

    public void ballDrop(int K, int N) {  // K 个鸡蛋 N层楼
        log.info("进入循环体, 最终需要计算的条件 = {}, {}", K, N);
        int res = this.dp(K, N);
        log.info("res = {}, 迭代次数 = {}", res, x.get());
    }

    public Integer dp(Integer K, Integer N) {  // K 鸡蛋数 N 楼层  返回步数
        x.incrementAndGet();
        if (K == 1) return N;
        if (N == 0) return 0;

        String genKey = K + ":" + N;
        if (memo.containsKey(genKey)) {
            return memo.get(genKey);
        }

        int res = Integer.MAX_VALUE;
        for (int i = 1; i < N + 1; i++) {  // 从1 到 N [1, N]
            res = Math.min(res,
                    Math.max(
                            dp(K - 1, i - 1), // 碎了
                            dp(K, N - i)  // 没碎
                    ) + 1  // 当前步数 + 1
            );
        }
        memo.put(genKey, res);

        return res;
    }

}










