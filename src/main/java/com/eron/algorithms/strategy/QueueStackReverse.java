package com.eron.algorithms.strategy;

import java.util.Iterator;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * queue 和 stack 的相互实现
 *
 * @author eron
 */
public class QueueStackReverse {
    private static final Logger log = LoggerFactory.getLogger(QueueStackReverse.class);

    public static void main(String[] args) {
        // 验证正确性 
        CustomeStack stack = new CustomeStack();
        stack.push(222);
        stack.push(123);
        stack.push(34);
        log.info("stack双队列出 --> {}, {}, {}", stack.pop(), stack.pop(), stack.pop());
        stack.pushx(235);
        stack.pushx(67);
        stack.pushx(89);
        stack.pushx(909);
        log.info("单队列实现stack --> {}, {}, {}, {}", stack.popx(), stack.popx(), stack.popx(), stack.popx());

        // 使用栈实现队列 
        CustomeQueue queue = new CustomeQueue();
        queue.push(123);
        queue.push(345);
        queue.push(456);
        queue.push(222);
        log.info("双stack实现, 队列出队 --> {}, {}, {}, {}", queue.poll(), queue.poll(), queue.poll(), queue.poll());
        queue.poll();  // 主动抛出异常 
    }

    private static class CustomeStack {
        // 2个queue实现一个stack 
        private Queue<Integer> q1 = new LinkedBlockingDeque<Integer>();
        private Queue<Integer> q2 = new LinkedBlockingDeque<Integer>();
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
    }

    private static class CustomeQueue {
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

}





