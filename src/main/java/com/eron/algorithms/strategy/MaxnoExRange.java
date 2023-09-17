/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.strategy;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * max no exchange range
 * 线段树系列问题   最长没有交集的时间段
 *
 * @author ERON_AMD
 */
public class MaxnoExRange {  // 求时间区间最大交集

    private static Logger log = LoggerFactory.getLogger(MaxnoExRange.class);

    public static void main(String[] args) {
        // 测试最大不重合区间

        int[][] schedules = {{3, 4}, {1, 5}, {4, 6}, {7, 9}, {8, 10}, {13, 22}, {23, 34}};
        intervalSchedule(schedules);
    }

    public static int intervalSchedule(int[][] schedules) {
        if (schedules.length <= 0) {
            return 0;
        }

        quickSortProcess(schedules, 0, schedules.length - 1);

        for (int[] x : schedules) {
            log.info("xxx : {}", arrayToString(x));
        }

        List<int[]> range = new LinkedList();
        range.add(schedules[0]);
        if (schedules.length <= 1) {  // 如果只有一个区间
            return 1;
        }

        return 0;
    }

    public static void quickSortProcess(int[][] schedules, int left, int right) {
        if (left >= right) {
            return;
        }

        int i = left, j = right;
        int mid = i;
        while (i < j) {
            while (schedules[j][1] > schedules[mid][1] && i < j) {
                j--;
            }
            while (schedules[i][1] < schedules[mid][1] && i < j) {
                i++;
            }
            if (i < j) {
                int[] tmp = schedules[i];
                schedules[i] = schedules[j];
                schedules[j] = tmp;
            }

        }

        schedules[left] = schedules[i];
        schedules[i] = schedules[mid];

        quickSortProcess(schedules, left, j--);
        quickSortProcess(schedules, j, right);
    }

    public static String arrayToString(int[] range) {
        StringBuilder sb = new StringBuilder();
        for (int x : range) {
            sb.append(x).append(",");
        }

        return sb.toString();
    }

}
