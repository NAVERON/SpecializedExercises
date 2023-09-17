package com.eron.algorithms.strategy;

import java.util.ArrayList;
import java.util.List;

public class ZCharConvert {

    // 将给定的字符串转变为Z形排列

    public static void main(String[] args) {
        ZCharConvert zShapeCharConvert = new ZCharConvert();

        String origin = "hello world";
        List<StringBuilder> result = zShapeCharConvert.toZString(origin, 4);
        System.out.println("最终结果list : " + result.toString());

        String result2 = zShapeCharConvert.toZStringMethod2(origin, 4);
        System.out.println("方法2结果 : " + result2);
    }

    public List<StringBuilder> toZString(String origin, int rowNum) {

        List<StringBuilder> result = new ArrayList<>();
        for (int i = 0; i < rowNum; i++) {
            result.add(new StringBuilder());
        }

        if (rowNum <= 1) {  // 如果只需要1行
            result.get(0).append(origin);
            return result;
        }

        int rowIndex = 0, flag = -1;  // 这里注意 flag初始化  -1, 因为第一次向下走

        for (Character x : origin.toCharArray()) {
            result.get(rowIndex).append(x);
            if (rowIndex == 0 || rowIndex == rowNum - 1) {
                // 如果在边界， 就需要反转方向增加
                flag = -flag;
            }
            rowIndex += flag;
        }

        return result;
    }

    /**
     * 方法2  矩阵算法
     * 创建一个2维矩阵
     * r = rowNum 行数
     * 循环周期 t = r + r - 2 = 2r - 2
     * 每个周期行数 = r，  列数 = 1 + r - 2 = r - 1
     * 周期数 = charLen/t , 总列数 = charLen/t * (r - 1)
     */
    public String toZStringMethod2(String origin, int rowNum) {
        StringBuilder result = new StringBuilder();

        int n = origin.length(), r = rowNum;
        if (r == 1 || n <= rowNum) {
            result.append(origin);
            return result.toString();
        }

        int t = r * 2 - 2; //循环周期， 一个周期的步长
        int c = (n + t - 1) / t * (r - 1);  // 列数   + t - 1是保证列数正确，'/' 整除会去掉余数 ...
        char[][] mat = new char[r][c];

        for (int i = 0, x = 0, y = 0; i < n; ++i) {// i origin char索引， x y 是矩阵的坐标 
            mat[x][y] = origin.charAt(i);
            if (i % t < r - 1) {
                ++x; // 向下移动
            } else {
                --x;
                ++y; // 向右上移动
            }
        }
        for (char[] row : mat) {  // 汇总2维矩阵 
            for (char ch : row) {
                if (ch != 0) {
                    result.append(ch);
                }
            }
        }

        return result.toString();
    }


}












