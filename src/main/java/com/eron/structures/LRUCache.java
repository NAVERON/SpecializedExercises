/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.structures;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * LRU 算法的实现 hashmap + linkedlist
 * 实现的功能同jdk中  linkedhashmap {@link LinkedHashMap}
 *
 * @author ERON_AMD
 */
public class LRUCache {

    private static Logger log = LoggerFactory.getLogger(LRUCache.class);

    public static class Node {

        public int key, value;
        public Node nextNode, preNode;

        public Node() {
        }

        public Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * 带头节点的双链表 JDK自己实现的是 LinkedHashMap
     */
    public static class DoublyLink {
        private int linkSize = 0;
        private Node head = null;
        private Node tail = null;

        public DoublyLink(int maxValocity) {
            head = new Node(0, 0);
            tail = new Node(0, 0);

            head.nextNode = tail;
            tail.preNode = head;
            linkSize = 0;
        }

        public int size() {
            return linkSize;
        }

        public void addFirst(Node x) {
            x.preNode = head;
            x.nextNode = head.nextNode;

            head.nextNode.preNode = x;
            head.nextNode = x;
            linkSize++;
        }

//        public Node removeFirst(){    // 这里移除最新节点的方法用不上 
//            if(head.nextNode == tail){
//                return null;
//            }
//            
//            Node tmp = head.nextNode;
//            remove(tmp);
//            
//            return tmp;
//        }

        public void addLast(Node x) {
            x.preNode = tail.preNode;
            x.nextNode = tail;

            tail.preNode.nextNode = x;
            tail.preNode = x;

            linkSize++;
        }

        public Node removeLast() {
            if (head.nextNode == tail) {
                return null; // 没有节点可以删除
            }

            Node tmp = tail.preNode;
            remove(tmp);

            return tmp;
        }

        // 这个节点一定存在 , 需要在上层调用时判断
        public void remove(Node x) {
            x.preNode.nextNode = x.nextNode;
            x.nextNode.preNode = x.preNode;

            x.preNode = null;
            x.nextNode = null;
            linkSize--;
        }

        @Override
        public String toString() {
            // 将双链表字符串化
            StringBuilder sb = new StringBuilder();
            Node tmp = head.nextNode;
            while (tmp != tail) {
                sb.append("{").append(tmp.key).append(",").append(tmp.value).append("}");
                tmp = tmp.nextNode;
            }

            return sb.toString();
        }

    }

    private Map<Integer, Node> quickSearch = null;
    private DoublyLink cache = null;
    private int maxCapacity = 0;

    public LRUCache(int initialMaxCapcity) {
        this.maxCapacity = initialMaxCapcity;

        cache = new DoublyLink(maxCapacity);
        quickSearch = new HashMap<Integer, Node>();
    }

    // 使特定的key Node为最新 这个key一定存在, 在最外层判断所有的异常情况
    public void makeRecently(int key) {
        Node x = (Node) quickSearch.get(key); // map不需要更新, 因为只是移动node, 不需要更换map的指针
        cache.remove(x);
        cache.addFirst(x);
    }

    // 移除队列最后 上层需要判定一定存在元素
    public void removeLastRecently() {
        Node last = cache.removeLast();
        quickSearch.remove(last.key);
    }

    // 删除一个特定的node 一定存在这个key
    public void delete(int key) {

        Node x = (Node) quickSearch.get(key);
        cache.remove(x);
        quickSearch.remove(key);
    }

    /**
     * 两个主要方法
     *
     * @param key
     * @return
     */
    public int get(int key) {
        if (!quickSearch.containsKey(key)) {
            return -1;
        }
        Node x = (Node) quickSearch.get(key);
        makeRecently(key);
        return x.value;
    }

    public void put(int key, int value) {
        if (quickSearch.containsKey(key)) {
            Node deletedX = (Node) quickSearch.get(key);
            cache.remove(deletedX);
            quickSearch.remove(key);
        }

        if (cache.linkSize >= maxCapacity) {
            // 如果空间满了, 移除最后的, 添加最新的
            Node last = cache.removeLast();
            quickSearch.remove(last.key);
        }

        // 这一步可以写成一个公共方法, addRecnetly
        Node newX = new Node(key, value);
        cache.addFirst(newX);
        quickSearch.put(key, newX);
    }

    @Override
    public String toString() {
        return "LRUCache[" + cache.toString() + ']';
    }

    public static void main(String[] args) {
        log.info("测试LRUCache");
        LRUCache lru = new LRUCache(4);

        lru.put(1, 2);
        log.info(lru.toString());

        lru.put(3, 5);
        log.info(lru.toString());

        log.info("lru get 4 :  {}", lru.get(4));
        log.info("lru get exists 3 : {}", lru.get(3));

        lru.put(3, 6);
        log.info(lru.toString());

        lru.put(5, 8);
        log.info(lru.toString());

        lru.put(11, 11);
        log.info(lru.toString());

        lru.put(34, 33);
        log.info(lru.toString());

        lru.put(0, 0);
        log.info(lru.toString());

        log.info("lru get 0 : {}", lru.get(0));

        // 直接使用LinkedHashMap  有序hashmap
        LinkedHashMap<Integer, Integer> jdkLRU = new LinkedHashMap<Integer, Integer>(3, 0.7F, true);

        jdkLRU.put(1, 3);
        jdkLRU.put(3, 4);
        log.info("当前lru情况 : {}, first -> {}", jdkLRU.toString(), jdkLRU.entrySet().iterator().next());
        jdkLRU.put(6, 8);
        log.info("cur => {}", jdkLRU.toString());
        jdkLRU.put(12, 33);
        log.info("cur => {}", jdkLRU.toString());

    }

}
