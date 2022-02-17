package com.eron.basic;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.LinkedList;
import java.util.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicThink {

    private static final Logger log = LoggerFactory.getLogger(BasicThink.class);

    public static void main(String[] args) {
        log.info("基础类");

        Vector<String> vector = new Vector<>();

        vector.add("hello");

        log.info("hello : {}", vector.toString());

        CustomStack<String> testStack = new CustomStack<String>();
        testStack.push("wangyulong ...");
        testStack.push("hello world");
        testStack.push("miss mingduo");
        log.info("teststack status : {}", testStack.isEmpty());
        for (; !testStack.isEmpty();) {
            log.info("输出stack数据 : {}", testStack.pop());
        }

        ThreadMXBean threadMX = ManagementFactory.getThreadMXBean();

        ThreadInfo[] threadinfos = threadMX.dumpAllThreads(false, false);

        for (ThreadInfo info : threadinfos) {
            log.info("threadid : {}, thread name : {}", info.getThreadId(), info.getThreadName());
        }

        long[] ids = threadMX.findMonitorDeadlockedThreads();
        if (ids == null) {
            log.info("inds is null");
            return;
        }
        for (long id : ids) {
            log.info("id : {}", id);
        }
    }

    // 自己实现栈结构
    public static class CustomStack<T> {

        private final LinkedList<T> container = new LinkedList<>();

        public void push(T date) {
            container.addFirst(date);
        }

        public T pop() {
            return container.removeFirst();
        }

        public int clear() {
            int removeDataSize = container.size();
            container.clear();
            return removeDataSize;
        }

        public boolean isEmpty() {
            return container.isEmpty();
        }

        public int getSize() {
            return container.size();
        }

    }

}
