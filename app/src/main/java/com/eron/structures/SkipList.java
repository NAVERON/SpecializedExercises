/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Spliterator;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ERON_AMD
 */
public class SkipList<K extends Comparable<K>, V> implements Iterable<K> {

    private static final Logger log = LoggerFactory.getLogger(SkipList.class);

    /**
    * | 4 | --------------> |___| --------------------------------------> | | |
    * 3 | --------------> |___| --------------------------------------> | | | 2
    * | --------------> |___| --------------> |____|----------------> | | | 1 |
    * --------------> |___| --------------> |____|----------------> | | | 0 |
    * ---> | 3 | ---> | 5 | ---> | 7 | ---> | 12 | ---> | 13 | ---> | |
    *
    * 跳表中一个头链接指向第一个节点，层级表示
    */
    protected static final Random randomGenerator = new Random();
    protected static final double DEFAULT_PROBABILITY = 0.5;  // 分层系数 
    private Node<K, V> head;
    private double probability;
    private int size;
    
    public SkipList(){
        this(SkipList.DEFAULT_PROBABILITY);
    }
    
    public SkipList(double probability){
        this.head = new Node<K, V>(null, null, 0);
        this.probability = probability;
        this.size = 0;
    }
    
    /**
     * 主要4个方法, 获取特定的节点 | 添加节点  | 查找最近的节点[完全相同或直接邻接] | 移除某个节点 
     * @param key
     * @return
     */
    public V get(K key){
        checkKeyValidity(key);
        Node<K, V> node = findNode(key);  // 找到的node是邻接目标值的最大值
        if(node.getKey().compareTo(key) == 0){  // 两个key相同  表示找到了准确值, 因为findNode找的是偏大的那个值(临近真实值的那个) 
            return node.getValue();
        }else{
            return null;
        }
    }
    
    public void add(K key, V value){
        checkKeyValidity(key);
        Node<K, V> node = this.findNode(key);  // 找到与所给key最邻近的最大值
        if(node.getKey() != null && node.getKey().compareTo(key) == 0){  // 如果存在一个key节点，则替换值， 不新增节点
            node.setValue(value);
            return;
        }
        
        // 不存在key相同节点，需要新增节点
        Node<K, V> newNode = new Node<K, V>(key, value, node.getLevel());
        this.horizontalFrontInsert(node, newNode);  // 将新的节点插入到找到的临近节点后面 ==  后面还需要插入上一层节点
        // Decide level according to the probability function
        int currentLevel = node.getLevel();
        int headLevel = this.head.getLevel();
        while(isBuildLevel()){  // 随机确定是否需要扩容增加一层 索引
            // building a new level 
            if(currentLevel >= headLevel){  // 如果新层的层数大于头节点， 头节点需要升高一级
                Node<K, V> newHead = new Node<K, V>(null, null, headLevel + 1);
                this.verticalLink(newHead, this.head);
                this.head = newHead;
                headLevel = this.head.getLevel();
            }
            // copy node and newNode to the upper level
            while(node.getUp() == null){
                node = node.getPrevious();
            }
            node = node.getUp();  // 往上升一层级
            
            Node<K, V> tmp = new Node<K, V>(key, value, node.getLevel());  // 将刚才获取到的node值插入上一层
            this.horizontalBehindInsert(node, newNode);
            this.verticalLink(tmp, newNode);
            newNode = tmp;  // 根据概率决定是否再上一层创建索引
            currentLevel++;
        }
        
        this.size++;
    }
    
    /**
        * 找到与所给的key最邻近的节点
        * @param key
        * @return 
        */
    protected Node<K, V> findNode(K key){
        Node<K, V> node = this.head;  //  左上角第一个极为头节点
        Node<K, V> next = null, down = null;
        K nodeKey = null;
        while(true){
            next = node.getNext();
            while(next != null && lessThanOrEqual(next.getKey(), key)){
                node = next;
                next = node.getNext();  // 两个指针向后移动一位
            }
            nodeKey = node.getKey();
            if(nodeKey != null && nodeKey.compareTo(key) == 0){  // 找到了
                break;  // 当前指针指向节点就是我们要找的值， 则直接返回
            }
            down = node.getDown();
            if(down != null){
                node = down;  // 如果向下的指向节点不为空， 则进入下一层循环， 向右查找
            }else{
                break;
            }
        }
        
        return node;
    }
    
    
    public void remove(K key){
        checkKeyValidity(key);
        Node<K, V> node = this.findNode(key);
        if(node == null || node.getKey().compareTo(key) != 0){
            throw new NoSuchElementException("The Key si not exists !");
        }
        
        while(node.getDown() != null){
            node = node.getDown();
        }
        // because node is on the lowest level so we need remove by down-top
        Node<K, V> prev = null;
        Node<K, V> next = null;
        for(; node != null; node = node.getUp()){  // 去除竖链上的所有元数据和索引节点
            prev = node.getPrevious();
            next = node.getNext();
            if(prev != null){
                prev.setNext(next);
            }
            if(next != null){
                next.setPrevious(prev);
            }
        }
        
        // Adjust head  处理头节点
        while(head.getNext() == null && head.getDown() != null){  // 如果出现头节点层次没有一个节点情况，则头节点转向下层头节点
            head = head.getDown();
            head.setUp(null);  // 释放指向上层的指针，GC会自动回收悬空节点
        }
        this.size--;
    }
    
    /**
        * 检查key是否为null
        * @param key 
        */
    protected void checkKeyValidity(K key){
        if(key == null){
            throw new IllegalArgumentException("Key Must be Not NULL !!");
        }
    }
    
    public boolean contains(K key){  // 判断当前跳表中是否存在key
        return get(key) != null;
    }
    
    public int size(){
        return this.size;
    }
    
    public boolean empty(){
        return this.size == 0;
    }
    
    protected boolean lessThanOrEqual(K a, K b){  // a < b   true
        return a.compareTo(b) <= 0;
    }
    
    protected boolean isBuildLevel(){
        return randomGenerator.nextDouble() < this.probability;
    }
    
    /**
     * 将y节点插入x节点的前面 
     * @param x
     * @param y
     */
    protected void horizontalFrontInsert(Node<K, V> x, Node<K, V> y){
    	y.setNext(x);
    	y.setPrevious(x.getPrevious());
    	if(x.getPrevious() != null) {
    		x.getPrevious().setNext(y);
    	}
    	x.setPrevious(y);
    }
    // y插入x的后面 
    protected void horizontalBehindInsert(Node<K, V> x, Node<K, V> y) {
        y.setPrevious(x);
        y.setNext(x.getNext());
        if(x.getNext() != null){
            x.getNext().setPrevious(y);
        }
        x.setNext(y);
	}
    
    // 插入新的高层x节点, y节点为其直接下临节点 
    protected void verticalLink(Node<K, V> x, Node<K, V> y){  // x 上层节点
        x.setDown(y);
        y.setUp(x);
    }
    
    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String toString() {
        StringBuilder sb =new StringBuilder();
        Node<K, V> node = this.head;
        
        // move into the lower left bottom 
        while(node.getDown() != null){
            node = node.getDown();
        }
        while(node.getPrevious() != null){
            node = node.getPrevious();
        }
        
        // head node with each level the key is null  so need to move into next node
        if(node.getNext() != null){
            node = node.getNext();
        }
        
        while(node != null){
            sb.append(node.toString()).append("\n");
            node = node.getNext();
        }
        return sb.toString();
    }

    @Override
    public Spliterator<K> spliterator() {
        return Iterable.super.spliterator(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void forEach(Consumer<? super K> action) {
        Iterable.super.forEach(action); //To change body of generated methods, choose Tools | Templates.
    }
    
//    @Override 
//    public Iterator<K> iterator(){
//        return new SkipListIterator<K, V>(this.head);
//    }
    
    protected static class SkipListIterator<K extends Comparable<K>, V> implements Iterator<K>{

        private Node<K, V> node;
        
        public SkipListIterator(Node<K, V> node){
            while(node.getDown() != null){
                node = node.getDown();
            }
            while(node.getPrevious() != null){
                node = node.getPrevious();
            }
            
            if(node.getNext() != null){
                node = node.getNext();
            }
            
            this.node = node;
        }
        
        @Override
        public boolean hasNext() {
            return this.node != null;
        }

        @Override
        public K next() {
            K resultKey = node.getKey();
            node = node.getNext();
            return resultKey;
        }

        @Override
        public void remove() {
            Iterator.super.remove(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void forEachRemaining(Consumer<? super K> action) {
            Iterator.super.forEachRemaining(action); //To change body of generated methods, choose Tools | Templates.
        }
        
    }

    protected static class Node<K extends Comparable<K>, V> {

        private K key;  // 需要实现Comparable   comparaTo() 大于返回+, 小于返回-, 等于返回0
        private V value;
        private int level;
        private Node<K, V> up, down, next, previous;

        public Node(K key, V value, int level) {
            this.key = key;
            this.value = value;
            this.level = level;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public Node<K, V> getUp() {
            return up;
        }

        public void setUp(Node<K, V> up) {
            this.up = up;
        }

        public Node<K, V> getDown() {
            return down;
        }

        public void setDown(Node<K, V> down) {
            this.down = down;
        }

        public Node<K, V> getNext() {
            return next;
        }

        public void setNext(Node<K, V> next) {
            this.next = next;
        }

        public Node<K, V> getPrevious() {
            return previous;
        }

        public void setPrevious(Node<K, V> previous) {
            this.previous = previous;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Node[").append("key:");
            if (this.key == null) {
                sb.append("None");
            } else {
                sb.append(this.key.toString());
            }
            sb.append(" value:");
            if (this.value == null) {
                sb.append("None");
            } else {
                sb.append(this.value.toString());
            }

            sb.append("]");

            return sb.toString();
        }

    }

    public static void main(String[] args) {
        SkipList<Integer, String> skipList = new SkipList();
        
        for(int i = 0; i < 10; i++){
            skipList.add(i, String.valueOf(i));
        }
        
        assert skipList.size != 10;
        assert skipList.empty();
        assert skipList.contains(5);
        assert skipList.get(6).equals("6");
        
        int count = 0;
        // 遍历没有办法 可能11的api修改了
        
        skipList.remove(8);
        assert skipList.size == 9;
        assert skipList.get(8) == null;
        
        skipList.remove(6);
        skipList.remove(1);
        
        assert skipList.size == 3;
        log.info("hello : {}", skipList.size == 7);
    }
    
    
}










