package com.eron.algorithms.strategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 参考资料 https://linux.thai.net/~thep/datrie/datrie.html 
 * https://zhuanlan.zhihu.com/p/80325757
 * 在trie的基础上 增加fail链接, 相当于kmp算法中的next指针  
 * Trie 树的实现 思路跟这里差不多  
 * 
 * @author eron  
 * @see com.eron.structures.TrieBuilder  
 */
public class ACautomaton {
    
    private static final Logger log = LoggerFactory.getLogger(ACautomaton.class);
    
    public static void main(String[] args) {
        ACautomaton auto = new ACautomaton();
        List<String> list = new ArrayList<>() {{
            add("she");
            add("shr");
            add("say");
            add("he");
            add("her");
        }};
        // 实现ac自动机 多模式匹配
        Trie trie = new Trie();
        auto.createGoto(trie, list);
        auto.createFail(trie);
        
        String text = "one day she say her has eaten many shrimps";
        List<String> res = auto.match(trie, text);
        
        log.info("匹配结果 --> {}", res);
    }
    private static class Node {
        String id;
        Map<Character, Node> children = new HashMap<>();
        String pattern = "";
        int endCount = 0;
        Node fail;  // 失败指针
        public Node(String id) {
            this.id = id;
        }
    }
    private static class Trie {
        Node root;
        int count = 0;
        public Trie() {
            this.root = new Node("root");
        }
        public void insert(String word) {
            Node cur = this.root;
            for(int i = 0; i < word.length(); i++) {
                char x = word.charAt(i);
                Node child = cur.children.get(x);
                if(child == null) {
                    child = new Node(Character.toString(x));
                    cur.children.put(x, child);
                    
                    this.count++;
                }
                
                cur = child;
            }
            cur.pattern = word;
            cur.endCount++;
        }
    }
    
    public void createGoto(Trie trie, List<String> patterns) {  // 生成 
        patterns.forEach(p -> {
            trie.insert(p);
        });
    }

    public void createFail(Trie trie) {  // 失败节点 
        // trie 广度优先遍历 
        Node root = trie.root;
        Queue<Node> queue = new ArrayBlockingQueue<>(trie.count);
        queue.add(root);
        
        while(!queue.isEmpty()) {
            Node node = queue.poll();
            if(node != null) {
                for(Character i : node.children.keySet()) {
                    Node child = node.children.get(i);
                    if(node == root) {  // 如果是根节点 失败链指向自己 
                        child.fail = root;
                    }else{
                        Node p = node.fail;
                        while(p != null) {
                            if(p.children.get(i) != null) {  // 存在相同的子 
                                child.fail = p.children.get(i);
                                break;
                            }
                            p = p.fail;
                        }
                        
                        if(p == null) {
                            child.fail = root;
                        }
                    }
                    
                    queue.add(child);
                }
            }
        }
    }
    
    public List<String> match(Trie trie, String text) {  // 匹配 
        List<String> res = new ArrayList<String>();
        Set<String> unique = new HashSet<String>();
        
        Node root = trie.root, p = root;
        for(int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            while(p.children.get(c) == null && p != root) {
                p = p.fail;
            }
            
            p = p.children.get(c);
            if(p == null) {
                p = root;  // 如果没有匹配的 
            }
            
            Node node = p;
            while(node != root) {
                if(node.endCount > 0) {
                    int pos = i - node.pattern.length() + 1;
                    log.info("模式匹配串 {} 其起始位置 {}", node.pattern, pos);
                    
                    if(!unique.contains(node.pattern)) {
                        unique.add(node.pattern);
                        
                        res.add(node.pattern);
                    }
                }
                
                node = node.fail;
            }
        }
        
        return res;
    }
}










