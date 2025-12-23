package com.eron.structures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义 单向指针链表
 * 实现原理类似 双向链表，这里不实现 --> java LinkedList 是双向链表
 *
 * @param <T> T
 */
public class CustomLinkedList<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomLinkedList.class);

    public static void main(String[] args) {
        // 测试 指针链表
        CustomLinkedList<Integer> singleList = CustomLinkedList.buildLinkedList(
            List.of(12, 45, 67, 11)
        );
        singleList.append(22);
        LOGGER.info("当前list --> {}, 大小 --> {}", singleList, singleList.getSize());
        LOGGER.info("查找 --> {}, {}", singleList.search(12), singleList.search(0));
        LOGGER.info("移除最后一个元素 --> {}, 移除后的list --> {}", singleList.removeLast(), singleList);
        singleList.append(89);

        singleList.reverseAllList();
        LOGGER.info("翻转链表 --> {}", singleList);
        singleList.reverseAllList2();
        LOGGER.info("再犯转一次，使用循环遍历方法 --> {}", singleList);

        // 部分翻转
        singleList.reversePartList(2);
        LOGGER.info("部分翻转list --> {}", singleList);
    }

    public static class CustomLinkedListNode<T> {
        public T value;
        public CustomLinkedListNode<T> next;

        public CustomLinkedListNode(T value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "CustomLinkedListNode{" +
                "value=" + value +
                '}';
        }
    }

    private CustomLinkedListNode<T> head, tail; // tail 也可以不要，插入的时候需要重新遍历插入

    public static <T> CustomLinkedList<T> buildLinkedList(List<T> values) {
        if (Objects.isNull(values)) {
            throw new IllegalArgumentException("values is null");
        }
        if (values.isEmpty()) {
            throw new IllegalArgumentException("values is empty");
        }

        CustomLinkedList<T> list = new CustomLinkedList<>();
        values.forEach(list::append);

        return list;
    }

    public CustomLinkedListNode<T> getHead() {
        if (Objects.isNull(this.head)) {
            throw new IllegalStateException("list is empty");
        }

        return this.head;
    }

    public CustomLinkedListNode<T> getTail() { // 快速获取 尾元素
        if (Objects.isNull(this.tail)) {
            throw new IllegalStateException("list is empty");
        }

        return this.tail;
    }

    // 新增 节点
    public void append(T value) {
        if (Objects.isNull(value)) {
            throw new IllegalArgumentException("value is null");
        }

        CustomLinkedListNode<T> node = new CustomLinkedListNode<>(value);
        this.append(node);
    }

    private void append(CustomLinkedListNode<T> node) {
        // 只能由内部方法调用，提前设定为不会为null
        if (this.head == null) {
            // 新建链表
            this.tail = this.head = node;
            return;
        }

        this.tail.next = node;
        this.tail = node;
    }

    // 获取链表长度
    public int getSize() {
        int i = 0;
        CustomLinkedListNode<T> cur = this.head;
        while (cur != null) {
            i++;
            cur = cur.next;
        }

        return i;
    }
    public CustomLinkedListNode<T> removeLast() {

        if (this.getHead() == this.getTail()) {
            CustomLinkedListNode<T> cur = this.tail;
            // 只有一个节点的情况
            this.head = this.tail = null;
            return cur;
        }

        CustomLinkedListNode<T> pre = this.getHead(), cur = this.getHead().next;
        while (cur != this.tail) { // 指针比较
            pre = pre.next;
            cur = cur.next;
        }

        pre.next = null;
        this.tail = pre;

        return cur;
    }

    public CustomLinkedListNode<T> search(T value) {
        // 查找list第一个匹配的
        CustomLinkedListNode<T> cur = this.getHead();
        while (cur != null) {
            if (value.equals(cur.value)) {
                return cur;
            }
            cur = cur.next;
        }

        return cur; // 没有找到 null值
    }

    // 遍历 list
    @Override
    public String toString() {
        List<T> travelRes = new ArrayList<>();
        if (this.head == null) {
            return "";
        }

        CustomLinkedListNode<T> cur = this.head;
        while (Objects.nonNull(cur)) {
            travelRes.add(cur.value);
            cur = cur.next;
        }

        return travelRes.stream().map(String::valueOf).collect(Collectors.joining(", "));
    }

    // 翻转整个链表
    public void reverseAllList() {
        CustomLinkedListNode<T> newHead = this.reverse(this.head);
        this.tail = this.head;
        this.head = newHead;
    }

    public void reverseAllList2() {
        CustomLinkedListNode<T> newHead = this.reverse2(this.head);
        this.tail = this.head;
        this.head = newHead;
    }

    // 翻转按照顺序第index之后的节点链表
    public void reversePartList(int index) {  // 只能翻转 2 之后的
        CustomLinkedListNode<T> cur = this.getHead();
        int i = 0;
        while (cur != null) { // 找到翻转的节点
            if (index == i) {
                break; // cur 之后节点翻转
            }
            cur = cur.next;
            i++;
        }

        if (Objects.isNull(cur)) {
            // 如果最后没有找到索引，表示无法翻转
            return;
        }

        this.tail = cur.next; // 翻转之后，next节点就是最后
        // 翻转cur之后的list
        cur.next = this.reverse(cur.next);
    }

    // 链表翻转
    private CustomLinkedListNode<T> reverse(CustomLinkedListNode<T> node) {
        if (node == null || node.next == null) {
            return node;
        }

        CustomLinkedListNode<T> leader = this.reverse(node.next);
        node.next.next = node;
        node.next = null;

        return leader;
    }

    // 使用循环 翻转链表
    public CustomLinkedListNode<T> reverse2(CustomLinkedListNode<T> node) {
        if (Objects.isNull(node)) {
            throw new IllegalArgumentException("node is null");
        }

        // 输入头节点, 返回反转后的头节点
        CustomLinkedListNode<T> a, b, c;

        a = node;
        b = a.next;

        if (b == null) { // 只有1个节点，不用翻转
            return node;
        }

        // 到这里表示至少有2个节点
        while (b != null) {
            c = b.next;
            b.next = a;

            a = b;
            b = c;
        }
        node.next = null;  // 第一个节点反转  ** 很重要,否则会出现末端死循环

        return a;
    }

}
