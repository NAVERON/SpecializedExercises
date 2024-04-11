package com.eron.structures;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;
import java.util.StringJoiner;
import java.util.concurrent.LinkedBlockingDeque;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomStack<T> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomStack.class);

    public static void main(String[] args) {
        CustomStack<Integer> stack = new CustomStack<>();
        stack.push(11, 22, 33);
        stack.push(44);
        LOGGER.info("当前栈的状态 --> volume: {}, size: {}, elements: {}, is empty ? {}",
            stack.volume(), stack.size(), stack, stack.isEmpty());

        stack.push(90, 67);
        LOGGER.info("获取栈顶元素 --> {}, 弹出站定元素 --> {}, 最后栈状态 --> {}", stack.peek(),
            stack.pop(), stack);

        // 验证栈序列 45321 正确
        boolean status = CustomStack.checkStackValidOrder(List.of(1, 2, 3, 4, 5),
            List.of(4, 3, 5, 1, 2));
        LOGGER.info("验证栈序列 --> {}", status);

        stack = new CustomStack<>(25);
        stack.push(90, 80, 70, 60);
        LOGGER.info("设定栈的容量 --> {}, {}, elements: {}", stack.volume(), stack.size(), stack);
    }

    // 2个queue实现一个stack
    private final Queue<T> c1 = new LinkedBlockingDeque<>();
    private long volume = 10; // 栈容量

    public CustomStack(){}
    public CustomStack(long volume) {
        this.volume = volume;
    }

    public void push(T... values) {
        Arrays.stream(values).forEach(this::push);
    }
    public void push(T value) {
        if (c1.size() >= this.volume) {
            throw new IllegalStateException("stack is out of size");
        }

        c1.add(value);
    }

    // 弹出栈顶
    public T pop() {
        for (int i = 0; i < c1.size() - 1; i++) {
            c1.add(c1.poll());
        }

        return c1.poll();
    }

    // 查看栈顶元素
    public T peek() {
        for (int i = 0; i < c1.size() - 1; i++) {
            c1.add(c1.poll());
        }

        T value = c1.peek();
        c1.add(c1.poll());

        return value;
    }

    public long volume() {
        return this.volume;
    }

    public long size() {
        return c1.size();
    }

    public boolean isEmpty() {
        return c1.isEmpty();
    }

    @Override
    public String toString() {
        // 从栈底到栈顶
        StringJoiner sj = new StringJoiner(", ");
        for (int i = 0; i < c1.size(); i++) {
            T value = c1.poll();
            sj.add(value.toString());
            c1.add(value);
        }

        return sj.toString();
    }

    // 根据已知的压栈判断是否可能出现某种出栈情况
    public static <T> boolean checkStackValidOrder(List<T> pushStack, List<T> popStack) {
        // 检查输入的两个参数合法性
        CustomStack<T> temp = new CustomStack<>();

        int index = 0;  // 待检测对象当前的位置
        for (T t : pushStack) {
            temp.push(t);

            while (!temp.isEmpty() && temp.peek().equals(popStack.get(index))) {
                temp.pop();
                index++;
            }
        }

        // 查看辅助stack是否全部match  如果全部match表示是正常的入栈出栈
        return temp.isEmpty();
    }

}
