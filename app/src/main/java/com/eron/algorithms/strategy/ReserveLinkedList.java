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
        Node head = null;
        reverseFullList(head);
    }
    
    public static Node reverseFullList(Node head){
        if(head == null || head.next == null){
            return head;
        }
        
        Node leader = reverseFullList(head.next);
        head.next.next = head;
        head.next = null;
        
        return leader;
    }
    
    public class Node {
        public int val;
        public Node next = null;
    }
    
    // 也可以使用3指针 
    public Node reverseWithPointer(Node head) {
    	
    	// 输入头节点, 返回反转后的头节点 
    	Node a = null, b = null, c = null;
    	
    	a = head == null ? null : head;
    	b = a == null ? null : a.next;
    	
    	// 2个节点以上才能循环批量处理 
    	if(a == null || b == null) { // 传参数错误, 不能null 
    		return head;
    	}
    	
    	while(b != null) {
    		c = b.next;
    		// b != null c == null ? != null ? 
    		b.next = a;
    		a = b;
    		b = c;
    	}
    	return a;
    }
    
}






