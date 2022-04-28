/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  反转链表 反转链表一部分    反转整个链表    每k个反转一次
 * @author ERON_AMD
 */
public class ReserveLinkedList {
    
    private static Logger log = LoggerFactory.getLogger(ReserveLinkedList.class);
    
    public static void main(String[] args) {
    	// 创建一个链表 
        Node head = LinkBuilder.createBuilder().append(new Node(12)).append(new Node(22)).build();
        
        // Node result = reverseFullList(head);
        Node result = reverseWithPointer(head);
        
        // 关于头节点 可以根据需要去除,这里不做过多的处理
        while(result != null) {
        	log.info("转换后 : {}", result.val);
        	
        	result = result.next;
        }
    }
    
    public static Node reverseFullList(Node head){
        if(head == null || head.next == null){
            return head;
        }
        
        Node leader = reverseFullList(head.next);
        head.next.next = head;
        head.next = null;  // 第一个节点指针设置 
        
        return leader;
    }
    
    public static class Node {
        public int val;
        public Node next = null;
        
        public Node(int value) {
        	this.val = value;
        }
    }
    
    private static class LinkBuilder {
    	
    	public Node head = new Node(-1);
    	public Node curPoint = this.head;
    	
    	public static LinkBuilder createBuilder() {
    		return new LinkBuilder();
    	}
    	
    	public LinkBuilder append(Node newNode) {
    		this.curPoint.next = newNode;
    		this.curPoint = this.curPoint.next;
    		
    		return this;
    	}
    	
    	public Node build() {
    		// 遍历list输出节点 
    		this.curPoint = head;
    		
    		while (this.curPoint != null) {
				log.info("输出节点 : {}", this.curPoint.val);
				this.curPoint = this.curPoint.next;
			}
    		
    		return this.head;
    	}
    }
    
    // 也可以使用3指针 
    public static Node reverseWithPointer(Node head) {
    	
    	// 输入头节点, 返回反转后的头节点 
    	Node a = null, b = null, c = null;
    	
    	a = head == null ? null : head;
    	b = a == null ? null : a.next;
    	
    	// 2个节点以上才能循环批量处理 
    	if(a == null || b == null) { // 传参数错误, 不能null 
    		return head;
    	}
    	
    	// 到这里表示至少有2个节点 
    	while(b != null) {
    		c = b.next;
    		b.next = a;
    		
    		a = b;
    		b = c;
    	}
    	head.next = null;  // 第一个节点反转   ** 很重要,否则会出现末端死循环 
    	
    	return a;
    }
    
}






