package com.eron.structures;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 基础集合类型的使用
 *
 * @author eron
 */
public class BasicJdkStructureUsage {

    private static final Logger log = LoggerFactory.getLogger(BasicJdkStructureUsage.class);

    public static void main(String[] args) {
        // 测试jdk 集合类
        BasicJdkStructureUsage usages = new BasicJdkStructureUsage();

        usages.listUsage();  // 容器 list

        usages.mapUsage();  // 容器 map

        usages.treeUsage(); // 容器 树结构

        usages.numberUsage();
    }

    public static class DelayedItem implements Delayed {  // 延迟队列容器中对象需要实现Delayed接口
        private String name;
        private Long availableTime;

        public DelayedItem(String name, Long delayTime) {
            this.name = name;
            this.availableTime = System.currentTimeMillis() + delayTime;
        }

        @Override
        public int compareTo(Delayed o) {
            if (!(o instanceof DelayedItem canComparedObj)) {
                throw new IllegalArgumentException();
            }
            int result = this.availableTime.compareTo(canComparedObj.availableTime);
            return result;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            long timeOff = this.availableTime - System.currentTimeMillis();
            return unit.convert(timeOff, TimeUnit.MILLISECONDS);
        }

        @Override
        public String toString() {
            return "DelayedItem [name=" + name + ", availableTime=" + availableTime + "]";
        }
    }

    public void listUsage() {
        // LinkedList ArrayList
        Integer[] simpleArr = new Integer[]{1, 3, 5, 7};

        List<Integer> link = new LinkedList<>(); // 指针链表
        List<String> arrs = new ArrayList<>(); // 数组链表

        List<Float> vector = new Vector<>();  // 同arraylist 方法添加synchronized关键字

        Queue<Double> priorityQueue = new PriorityQueue<>(); // 优先级队列  数组实现的binary堆  二叉堆
        Queue<Integer> arrQueue = new ArrayBlockingQueue<>(10); // 数组队列  数组 + 双指针
        Queue<String> arrDequeue = new ArrayDeque<>();  // 基于数组和双指针
        Queue<DelayedItem> delayQueue = new DelayQueue<>();  // 延迟队列

        Collections.sort(link);
        Collections.binarySearch(link, 5);  // 返回搜索结果索引

        // list转数组
        String[] arr = arrs.toArray(new String[0]);
        // arr  转 List
        List.of(simpleArr);  // 数组转变成List Java 9+

        CopyOnWriteArrayList<String> concurrentArrayList = new CopyOnWriteArrayList<>();  // 写时复制 List
        List.of(1, 2, 3);
    }

    public void mapUsage() {
        // HashMap ConcurrentHashMap  LinkedHashMap = List + HashMap

        Map<String, Integer> simple = new HashMap<String, Integer>();  // 数组 < 64, 链表 < 8, 否则转换成红黑树 treeMap
        Map<Integer, String> table = new Hashtable<>(); // key value 不能为null
        Map<String, Double> concurrentMap = new ConcurrentHashMap<>();  // node CAS和synchornized 锁的粒度小, 并发度高
        Map<Integer, String> lru = new LinkedHashMap<>();  // 带有链表顺序的hashMap

    }

    public void treeUsage() {
        // TreeMap 红黑树 TreeSet  HashSet = HashMap.keys()
        Set<Integer> orderedSet = new LinkedHashSet<>();  // extends HashSet , 内部通过LinkeHashMap 实现

        Set<String> treeset = new TreeSet<>(); // 基于treeMap 实现

        Collections.synchronizedSet(orderedSet);  // 普通的集合转变为支持并发的集合, 效率低, 建议直接使用concurrent 包并发库中的集合

        CopyOnWriteArraySet<Integer> copyOnWriteArraySet = new CopyOnWriteArraySet<>();
    }

    // JDK Lock Class的使用
    public void lockLikeUsage() {
        // RetrentLock  ReadLock  WriteLock

        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.tryLock();  // 尝试获取锁
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    }

    public void numberUsage() {
        BigDecimal number = new BigDecimal("984930921");
        log.info("bigdecimal number : {}", number);

    }


}









