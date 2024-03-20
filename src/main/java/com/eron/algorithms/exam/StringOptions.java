package com.eron.algorithms.exam;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringOptions {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringOptions.class);

    public static void main(String[] args) {
        StringOptions stringOptions = new StringOptions();
        LOGGER.info("两个list数字相加 --> {}",
            stringOptions.plusFromListNumber(List.of(1, 12, 3), List.of(12, 4)));
    }

    // kmp 字符串搜寻算法
    public void KMPAlgorithm() {

    }

    // 翻转字符串
    public void reverse(String s) {

    }

    // 字符串 计算相加
    public void plusNumber(String s1, String s2) {

    }

    // 类似上述问题，两个list相加
    public List<Integer> plusFromListNumber(List<Integer> n1, List<Integer> n2) {

        List<Integer> finalResult = new ArrayList<>();

        int sum = 0, add = 0; // add = carry
        Iterator<Integer> iter1 = n1.iterator();
        Iterator<Integer> iter2 = n2.iterator();

        while (iter1.hasNext() && iter2.hasNext()) {  // 两个都没有组到尽头
            int num1 = iter1.next();
            int num2 = iter2.next();

            sum = (num1 + num2 + add) % 10;
            add = (num1 + num2 + add) / 10;

            finalResult.add(sum);
        }

        while (iter1.hasNext()) {
            int num = iter1.next();
            sum = (num + add) % 10;
            add = (num + add) / 10;

            finalResult.add(sum);
        }

        while (iter2.hasNext()) {
            int num = iter2.next();
            sum = (num + add) % 10;
            add = (num + add) / 10;

            finalResult.add(sum);
        }

        if (add != 0) {
            finalResult.add(add);
        }

        return finalResult;
    }

}
