package com.eron.algorithms.strategy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 两数之和 给两个表示数字的链表, 相加， 从左向右依次是高位
 *
 * @author eron
 */
public class TwoLinkNumberSum {

    public static void main(String[] args) {

        List<Integer> list1 = new ArrayList<>();
        list1.add(5);
        list1.add(4);
        list1.add(7);

        List<Integer> list2 = new ArrayList<>();
        list2.add(5);
        list2.add(6);
        list2.add(4);

        TwoLinkNumberSum twoLinkNumberSum = new TwoLinkNumberSum();
        List<Integer> result = twoLinkNumberSum.makeSumOfLinkedList(list1, list2);

        System.out.println("result : " + result.toString());

    }

    public List<Integer> makeSumOfLinkedList(List<Integer> list1, List<Integer> list2) {
        List<Integer> finalResult = new ArrayList<>();

        int sum = 0, add = 0; // add = carry
        Iterator<Integer> iter1 = list1.iterator();
        Iterator<Integer> iter2 = list2.iterator();

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








