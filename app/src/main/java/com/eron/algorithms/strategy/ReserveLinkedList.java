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
    
    public class Node{
        public int val;
        public Node next = null;
    }
}






