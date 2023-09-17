package com.eron.algorithms.strategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 给定一个正整数list  如 [1, 43, 5, 8]
 * 返回这些数字可以组成的最大数 85431
 *
 * @author eron
 * 思路 ： 比较list中前后顺序 无非是大的放在前面, 主要是二位数与一位数的比较
 * 两两比对， 交换2个数字的前后顺序并比较大小， 组成后的数字大的放在前面, 否则放在后面
 */
public class BigestOfNumbers {

    private static final Logger log = LoggerFactory.getLogger(BigestOfNumbers.class);

    public static void main(String[] args) {
        BigestOfNumbers base = new BigestOfNumbers();

        @SuppressWarnings("serial")
        List<Integer> numbers = new ArrayList<Integer>() {{
            add(5);
            add(3);
            add(31);
            add(2);
        }};

        Long result = base.getLargestNumber(numbers);
        log.info("最大值 => {}", result);
    }

    public Long getLargestNumber(List<Integer> numbers) {

        Comparator<Integer> comparator = new Comparator<Integer>() {  // 降序
            @Override
            public int compare(Integer o1, Integer o2) {
                String a = o1.toString();
                String b = o2.toString();

                // 如果o1排在前面大 返回正数 否则返回负数, 实现降序
                return Integer.parseInt(b + a) - Integer.parseInt(a + b);
            }
        };
        numbers.sort(comparator);

        StringBuilder numBuilder = new StringBuilder();
        for (Integer x : numbers) {
            numBuilder.append(x.toString());
        }

        return Long.parseLong(numBuilder.toString());
    }

}










