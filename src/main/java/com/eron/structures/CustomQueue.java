package com.eron.structures;

import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;
import java.util.StringJoiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 普通实现，使用栈结构模拟队列操作
 * 底对底的栈结构 模拟队列实现
 * ------|  |--------------
 *     xx|  |xx
 * ------|  |-------------
 */
public class CustomQueue<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomQueue.class);

    public static void main(String[] args) {
        CustomQueue<Integer> queue = new CustomQueue<>();
        queue.add(12, 34, 56);
        queue.add(0);
        LOGGER.info("当前队列元素, volume: {}, size: {}, elements --> {}", queue.volume(),
            queue.size(), queue);
        LOGGER.info("队列查看队首元素 --> {}, 当前队列状态: {}", queue.peek(), queue);
        LOGGER.info("队列弹出队列首位元素 --> {}, 当前队列状态: {}", queue.poll(), queue);
        queue.add(11, 22, 33, 44, 55, 66, 77);
        LOGGER.info("当前队列状态 --> {}", queue);
    }

    private final Stack<T> c1 = new Stack<>();
    private final Stack<T> c2 = new Stack<>();
    private long volume = 10; // 队列容量默认值

    public CustomQueue(){}
    public CustomQueue(long volume) { // 初始化队列的最大容量
        this.volume = volume;
    }

    public void add(T... values) {
        Arrays.stream(values).forEach(this::add);
    }

    // 队列新增元素
    public void add(T value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("value is not allowed null");
        }

        // 右边的size 超过容量一半 --> 右边的迁移到左边，同时保证左边的也不能超过容量一半
        if (c2.size() >= volume / 2) {
            transfer();
        }

        if (c2.size() >= volume / 2) {
            // 如果迁移后仍然不够，表示容量不够了，抛出异常
            throw new IllegalStateException("queue is out of size");
        }

        c2.push(value);
    }

    // 队列的实际元素数量
    public long size() {
        transfer(); // 整理栈元素
        return c1.size() + c2.size();
    }

    // queue 容器最大可存储容量
    public long volume() {
        return this.volume;
    }

    // 当发现c2容量满时，进行元素转移
    private void transfer() {
        // 整理两个stack容器 c2 --> c1
        Stack<T> temp1 = new Stack<>();
        Stack<T> temp2 = new Stack<>();
        while (!c1.isEmpty()) {
            temp1.push(c1.pop());
        }
        while (!c2.isEmpty()) {
            temp2.push(c2.pop());
        }

        // 可以从此c2中移动多少个到c1
        long canMoveCount = volume / 2 - temp1.size();
        while (canMoveCount > 0) {
            if (!temp2.isEmpty()) {
                temp1.push(temp2.pop());
            }
            canMoveCount--;
        }

        // 把c1原有的放回原处
        while (!temp1.isEmpty()) {
            c1.push(temp1.pop());
        }

        // 把c2多余的内容再返回去
        while (!temp2.isEmpty()) {
            c2.push(temp2.pop());
        }
    }

    // 弹出队列第一个值
    public T poll() {
        if (c1.isEmpty()) {
            this.transfer();
        }

        if (c1.isEmpty()) {
            return null; // 队列中没有元素
        }

        return c1.pop();
    }

    // 查看队列第一个值
    public T peek() {
        if (c1.isEmpty()) {
            this.transfer();
        }

        if (c1.isEmpty()) {
            return null;
        }

        return c1.peek();
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ");
        // 打印队列的元素
        Stack<T> temp1 = new Stack<>();
        Stack<T> temp2 = new Stack<>();

        while (!c1.isEmpty()) {
            T value = c1.pop();
            sj.add(value.toString());
            temp1.push(value);
        }
        while (!c2.isEmpty()) { // c2 与c1不同，需要回栈的时候遍历
            T value = c2.pop();
            temp2.push(value);
        }

        // 还原现场
        while (!temp1.isEmpty()) {
            c1.push(temp1.pop());
        }
        while (!temp2.isEmpty()) {
            T value = temp2.pop();
            sj.add(value.toString());
            c2.push(value);
        }

        return sj.toString();
    }
}
