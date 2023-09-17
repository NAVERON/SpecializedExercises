package com.eron.algorithms.smartquestion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 贪婪算法的应用
 * 局部最优解 可以得到全局最优解时, 使用贪婪算法可行
 *
 * @author wangy
 */


public class GreedySolution {

    private static final Logger LOGGER = LoggerFactory.getLogger(GreedySolution.class);

    public static void main(String[] args) {

    }

    // 任务调度的实现
    private static void tackProcess() {
        // 最大并行任务数 n  组成任务矩阵, 每一行不能有重复任务执行 
        // (maxTimes - 1) * (n + 1) --> 间隔任务数  + 最后一行的数量 --> 任务数量与最大任务数一样, 表示最后还需要执行一次 
    }


    // 字典树的实现 之前做过, 这做类似的练习 
    // 可以反向插入单词, 用于匹配后缀 
}










