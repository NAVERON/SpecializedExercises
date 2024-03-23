package com.eron.algorithms.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 有关数字计算的策略问题
 */
public class NumberOptions {
    private static final Logger LOGGER = LoggerFactory.getLogger(NumberOptions.class);

    public static void main(String[] args) {
        NumberOptions numberOptions = new NumberOptions();
        // 数字翻转
        LOGGER.info("翻转数字 --> {}", numberOptions.reverseNumber(1234));
        LOGGER.info("是否为回文数字 --> {}", numberOptions.isSymmetryNumber(1221));
    }

    // 翻转数字 [正整数]， 1234 --> 4321
    public long reverseNumber(long number) {
        long reversedNumber = 0;
        long remainder;
        while (number > 0) {
            remainder = number % 10;
            number = number / 10;
            reversedNumber = reversedNumber * 10 + remainder;
        }

        return reversedNumber;
    }

    // 对称/回文数字判断 如 1221, 只能是 正整数
    // 可以先翻转，然后判断两个数字是否一样
    // 转换成字符串，使用 <StringOption> 方法判断是否为回文字符串
    public boolean isSymmetryNumber(long number) {
        long div = 1;
        while (number / div >= 10) {
            div *= 10;
        }
        LOGGER.info("div --> {}", div);

        while (number > 0) {
            long heigh = number / div;
            long low = number % div;
            if (heigh != low) {
                return false;
            }

            number = (number % div) / 10;
            div /= 100; // number前后各自删除了一个数字
        }

        return true;
    }


}
