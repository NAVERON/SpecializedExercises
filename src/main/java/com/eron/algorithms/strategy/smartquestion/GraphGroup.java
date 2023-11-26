package com.eron.algorithms.strategy.smartquestion;


import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图中有多少个分组 之间没有连线为一个分组
 *
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

        //拓扑排序 相关 
        int[][] course = new int[][]{
                {1, 0},
                {2, 0},
                {3, 1},
                {3, 2}
        };
        GraphGroup.courseOrder(course, 4);

    }

    // 或者广度优先 深度优先遍历图， 或者使用父标记 
    public int countGroup(List<String> graph) {
        int group = 0;
        List<List<Boolean>> graphArray = this.formatGraph(graph);

        // 创建新组的条件 
        LinkedList<HashSet<Integer>> groupInfo = new LinkedList<>();
        for (int i = 0; i < graphArray.size(); i++) {
            // 检查现有的组是否已经包含 没有则新加  需要获取当前可以插入第几个分组 
            HashSet<Integer> curSet = null;
            for (int k = 0; k < groupInfo.size(); k++) {
                if (groupInfo.get(k).contains(i)) {
                    curSet = groupInfo.get(k); // 当前需要归到k分组中 
                    break;
                }
            }
            if (curSet == null) { // 表示没有可以归入的分组
                curSet = new HashSet<Integer>();
                curSet.add(i);  // 当前就是分组第一个 
                groupInfo.add(curSet);
            }

            List<Boolean> row = graphArray.get(i);  // 这里是true的都是与i有直接连线的 
            for (int j = 0; j < row.size(); j++) {
                if (i == j || !row.get(j)) {  // 没有连通
                    continue;
                }
                curSet.add(j);
            }

            // 判断当前set是否与其他集合有交集，有则合并 
            for (int k = 0; k < groupInfo.size(); k++) {
                if (curSet == groupInfo.get(k)) {
                    // 是同一个对象 
                    continue;
                }
                HashSet<Integer> res = new HashSet<>();
                res.addAll(curSet);
                res.retainAll(groupInfo.get(k));
                if (!res.isEmpty()) {
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

        for (int i = 0; i < graph.size(); i++) {
            // 当前节点在那一组 
            HashSet<Integer> cur = null;
            for (HashSet<Integer> set : groupinfo) {
                if (set.contains(i)) {
                    cur = set;
                    break;
                }
            }
            if (cur == null) {
                cur = new HashSet<>();
                cur.add(i);
                groupinfo.add(cur);
            }

            List<Boolean> row = graphArray.get(i);
            for (int j = 0; j < row.size(); j++) {
                if (i == j) {
                    continue;
                }
                if (row.get(j) || graphArray.get(j).get(i)) {  // 双向检查 这样不用检查集合的交集
                    cur.add(j);
                }
            }
        }

        return groupinfo.size();
    }

    public List<List<Boolean>> formatGraph(List<String> graph) {
        List<List<Boolean>> graphArray = new ArrayList<List<Boolean>>();

        graph.forEach(row -> {
            byte[] letters = row.getBytes();
            List<Boolean> rowStatus = new ArrayList<>(letters.length);
            for (byte s : letters) {
                Boolean status = (s - '0') > 0 ? true : false;
                rowStatus.add(status);
            }
            graphArray.add(rowStatus);
            log.info("{}", rowStatus);  // 打印当前的格式转换结果 
        });

        return graphArray;
    }


    // 课程表 
    // 给出一个二维数组 表示每门课之前需要上哪门课  
    // DFS  深度优先遍历  结束的标志是 没有出度或者出度已经在结果集中 
    private static void courseOrder(int[][] prerequired, int courseNum) {

        HashMap<Integer, LinkedList<Integer>> info = new HashMap<>();
        Set<Integer> visited = new HashSet<Integer>();  // 访问过的节点 
        Deque<Integer> res = new ArrayDeque<>();  // 最终的顺序 stack 

        // 数组全部转化为方便使用的map结构 
        for (int[] order : prerequired) {
            int from = order[0];
            int to = order[1];
            info.putIfAbsent(from, new LinkedList<>());
            info.get(from).add(to);  // 添加路径 

            // log.info("打印当前路径 --> {} ==> {}", from, info.get(from).toString());
        }

        Deque<Integer> stack = new ArrayDeque<>();
        // info.entrySet().forEach(x->{}); // 取出每个entry  遍历 
        info.forEach((key, value) -> {
            log.info("当前遍历 --> {} --> {}", key, value.toString());

            if (visited.contains(key)) return;
            stack.push(key);
            visited.add(key);

            while (!stack.isEmpty()) {
                int top = stack.peek();
                LinkedList<Integer> edges = info.getOrDefault(top, new LinkedList<>());
                if (edges.isEmpty() || res.containsAll(edges)) {  // 符合条件
                    // 如果当前节点 出度为 0 , 表示它没有向下的指向节点 | 或者指向都已经入结果队列 
                    int x = stack.pop();
                    log.info("出现复合条件的 --> {}, {}, {}", top, x, edges);
                    res.push(x);
                    continue;
                }

                edges.stream().forEach(nearby -> {
                    if (visited.contains(nearby)) return;  // 如果

                    log.info("新加入的节点 --> {}", nearby);
                    stack.push(nearby);
                    visited.add(nearby);
                });
            }

        });

        log.info("最终res  结果 --> {}", res.toString());
    }
}














