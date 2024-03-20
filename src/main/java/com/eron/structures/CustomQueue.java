package com.eron.structures;

import java.util.Stack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomQueue.class);

    public static void main(String[] args) {
        // test
    }

    private Stack<Integer> s1 = new Stack<>();
    private Stack<Integer> s2 = new Stack<>();
    Stack<Integer> cur = s1;  // 这里要不要指针都可以 因为不能调换指针实现
    Stack<Integer> transfer = s2;

    public void push(Integer x) {
        cur.push(x);
    }

    public Integer poll() {
        // 如果transfer 不是空的, 直接pop transfer
        // 如果 transfer 空的, 需要按照普通逻辑
        if (transfer.isEmpty() && cur.isEmpty()) {
            throw new IllegalAccessError("队列为空");
        }
        if (transfer.isEmpty() && !cur.isEmpty()) {
            while (!cur.isEmpty()) {
                transfer.push(cur.pop());
            }
        }
        int res = transfer.pop();
        return res;
    }
}
