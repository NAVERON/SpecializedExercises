package com.eron.structures;

import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomStack {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomStack.class);

    public static void main(String[] args) {
        // test
    }

    // 2个queue实现一个stack
    private Queue<Integer> q1 = new LinkedBlockingDeque<>();
    private Queue<Integer> q2 = new LinkedBlockingDeque<>();
    private Queue<Integer> curPointer = q1;  // 其实可以直接使用q1  q2 指针, 直接互换即可
    private Queue<Integer> toolPointer = q2;  // 但是这里这样写可以更清楚的表达思想

    public void push(Integer x) {
        curPointer.add(x);
    }

    public Integer pop() {
        if (curPointer.isEmpty()) {  // push的那个队列
            throw new IllegalAccessError("空了");
        }
        int size = curPointer.size();
        for (int i = 0; i < size - 1; i++) {
            toolPointer.add(curPointer.poll());
        }
        int res = curPointer.poll();  // 最后一个
        // 交换2个指针
        Queue<Integer> t = curPointer;
        curPointer = toolPointer;
        toolPointer = t;
        return res;
    }

    /**
     * 一个队列就可以实现
     *
     * @param x
     */
    Queue<Integer> q3 = new LinkedBlockingDeque<>();

    public void pushx(Integer x) {
        q3.add(x);
    }

    public Integer popx() {
        int size = q3.size();
        for (int i = 0; i < size - 1; i++) {
            q3.add(q3.poll());
        }
        return q3.poll();
    }

    public Integer peek() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    // 根据已知的压栈判断是否可能出现某种出栈情况
    public static boolean checkStackValidOrder(int[] pushStack, int[] popStack) {
        // 检查输入的两个参数合法性
        CustomStack assistantStack = new CustomStack();

        int indexOfPop = 0;  // 待检测对象当前的位置
        for (int i = 0; i < pushStack.length; i++) {
            assistantStack.push(pushStack[i]);

            while (!assistantStack.isEmpty() && assistantStack.peek().equals(popStack[indexOfPop])) {
                assistantStack.pop();
                indexOfPop++;
            }
        }

        // 查看辅助stack是否全部match  如果全部match表示是正常的入栈出栈
        return assistantStack.isEmpty();
    }

}
