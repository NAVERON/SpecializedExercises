package com.eron.algorithms.smartquestion;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图 相关解决方案 
 * @author wangy
 * 
 */
public class GraphRelatedResolve {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphRelatedResolve.class);
    
    public static void main(String[] args) {
        int[][] course = new int[][] {
            {1, 0},
            {2, 0}, 
            {3, 1}, 
            {3, 2}
        };
        GraphRelatedResolve.courseOrder(course, 4);
    }
    
    // 课程表 
    // 给出一个二维数组 表示每门课之前需要上哪门课  
    private static void courseOrder(int[][] prerequired, int courseNum) {
        // 转换为map 
        HashMap<Integer, LinkedList<Integer>> info = new HashMap<>();
        Set<Integer> visited = new HashSet<Integer>();  // 访问过的节点 
        Deque<Integer> stack = new ArrayDeque<>();  // 最终的顺序 stack 
        
        for(int[] order : prerequired) {
            int from = order[0]; 
            int to = order[1];
            info.putIfAbsent(from, new LinkedList<>());
            info.get(from).add(to);  // 添加路径 
            
            LOGGER.info("打印当前路径 --> {} ==> {}", from, info.get(from).toString());
        }
        
        List<Integer> tmp = new LinkedList<>();
        // info.entrySet().forEach(x->{}); // 取出每个entry  遍历 
        info.forEach((key, value) -> {
            LOGGER.info("当前遍历 --> {} --> {}", key, value.toString());
        });
    }
    
    
}







