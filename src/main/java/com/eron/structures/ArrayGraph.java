package com.eron.structures;

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
 * 使用数组方式表示图的结构
 * 广度优先，深度优先，孤岛问题等
 */
public class ArrayGraph {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayGraph.class);

    public static void main(String[] args) {
        int[][] arr = new int[][]{
            {1, 1, 1, 1},
            {1, 0, 0, 1},
            {1, 1, 0, 1},
            {1, 0, 1, 1}
        };
        ArrayGraph arrayGraph = new ArrayGraph();
        arrayGraph.dfs(0, 0);
        arrayGraph.bfs(0, 0);

        // 临时 ===================================
        List<String> graph = new LinkedList<>() {{
            add("1010");
            add("1000");
            add("0010");
            add("0001");
        }};
        int result = arrayGraph.countGroup2(graph);
        LOGGER.info("分组--> {}", result);

        //拓扑排序 相关
        int[][] course = new int[][]{
            {1, 0},
            {2, 0},
            {3, 1},
            {3, 2}
        };
        courseOrder(course, 4);
    }

    // 深度优先遍历 从i/j位置开始
    public void dfs(int i, int j) {

    }

    // 广度优先遍历 i/j位置开始
    public void bfs(int i, int j) {

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
            LOGGER.info("{}", rowStatus);  // 打印当前的格式转换结果
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
            LOGGER.info("当前遍历 --> {} --> {}", key, value.toString());

            if (visited.contains(key)) return;
            stack.push(key);
            visited.add(key);

            while (!stack.isEmpty()) {
                int top = stack.peek();
                LinkedList<Integer> edges = info.getOrDefault(top, new LinkedList<>());
                if (edges.isEmpty() || res.containsAll(edges)) {  // 符合条件
                    // 如果当前节点 出度为 0 , 表示它没有向下的指向节点 | 或者指向都已经入结果队列
                    int x = stack.pop();
                    LOGGER.info("出现复合条件的 --> {}, {}, {}", top, x, edges);
                    res.push(x);
                    continue;
                }

                edges.stream().forEach(nearby -> {
                    if (visited.contains(nearby)) return;  // 如果

                    LOGGER.info("新加入的节点 --> {}", nearby);
                    stack.push(nearby);
                    visited.add(nearby);
                });
            }

        });

        LOGGER.info("最终res  结果 --> {}", res);
    }

}
