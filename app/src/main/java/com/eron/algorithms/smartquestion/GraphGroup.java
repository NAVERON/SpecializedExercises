package com.eron.algorithms.smartquestion;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图中有多少个分组 之间没有连线为一个分组 
 * @author eron 
 * 解法 遍历图 获取可达性对象 
 * 先考虑通常情况，再根据测试补充参数校验 
 */
public class GraphGroup {

    private static final Logger log = LoggerFactory.getLogger(GraphGroup.class);
    public static void main(String[] args) {
        List<String> graph = new LinkedList<>() {{
            add("1010");
            add("1000");
            add("0010");
            add("0001");
        }};
        GraphGroup graphGroup = new GraphGroup();
        int result = graphGroup.countGroup2(graph);
        log.info("分组--> {}", result);
        
    }
    
    // 或者广度优先 深度优先遍历图， 或者使用父标记 
    public int countGroup(List<String> graph) {
        int group = 0;
        List<List<Boolean>> graphArray = this.formatGraph(graph);
        
        // 创建新组的条件 
        LinkedList<HashSet<Integer>> groupInfo = new LinkedList<>();
        for(int i = 0; i < graphArray.size(); i++) {
            // 检查现有的组是否已经包含 没有则新加  需要获取当前可以插入第几个分组 
            HashSet<Integer> curSet = null;
            for(int k = 0; k < groupInfo.size(); k++) {
                if(groupInfo.get(k).contains(i)) {
                    curSet = groupInfo.get(k); // 当前需要归到k分组中 
                    break;
                }
            }
            if(curSet == null) { // 表示没有可以归入的分组 
                curSet = new HashSet<Integer>();
                curSet.add(i);  // 当前就是分组第一个 
                groupInfo.add(curSet);
            }
            
            List<Boolean> row = graphArray.get(i);  // 这里是true的都是与i有直接连线的 
            for(int j = 0; j < row.size(); j++) {
                if(i == j || !row.get(j)) {  // 没有连通 
                    continue;
                }
                curSet.add(j);
            }
            
            // 判断当前set是否与其他集合有交集，有则合并 
            for(int k = 0; k < groupInfo.size(); k++) {
                if(curSet == groupInfo.get(k)) {
                    // 是同一个对象 
                    continue;
                }
                HashSet<Integer> res = new HashSet<>();
                res.addAll(curSet);
                res.retainAll(groupInfo.get(k));
                if(!res.isEmpty()) {
                    groupInfo.get(k).addAll(curSet);
                    groupInfo.remove(curSet);
                }
            }
        }
        
        return groupInfo.size();
    }
    
    public int countGroup2(List<String> graph) {
        int n = 0;
        List<List<Boolean>> graphArray = this.formatGraph(graph);
        LinkedList<HashSet<Integer>> groupinfo = new LinkedList<>();
        
        for(int i = 0; i < graph.size(); i++) {
            // 当前节点在那一组 
            HashSet<Integer> cur = null;
            for(HashSet<Integer> set : groupinfo) {
                if(set.contains(i)) {
                    cur = set;
                    break;
                }
            }
            if(cur == null) {
                cur = new HashSet<>();
                cur.add(i);
                groupinfo.add(cur);
            }
            
            List<Boolean> row = graphArray.get(i);
            for(int j = 0; j < row.size(); j++) {
                if(i == j) {
                    continue;
                }
                if(row.get(j) || graphArray.get(j).get(i)) {  // 双向检查 这样不用检查集合的交集 
                    cur.add(j);
                }
            }
        }
        
        return groupinfo.size();
    }
    
    public List<List<Boolean>> formatGraph(List<String> graph){
        List<List<Boolean>> graphArray = new ArrayList<List<Boolean>>();
        
        graph.forEach(row -> {
            byte[] letters = row.getBytes();
            List<Boolean> rowStatus = new ArrayList<>(letters.length);
            for(byte s : letters) {
                Boolean status = (s - '0') > 0 ? true : false;
                rowStatus.add(status);
            }
            graphArray.add(rowStatus);
            log.info("{}", rowStatus);  // 打印当前的格式转换结果 
        });
        
        return graphArray;
    }
}



