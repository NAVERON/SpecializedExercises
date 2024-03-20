package com.eron.structures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 定义图的数据结构
 *
 * @author eron
 */
public class SimpleGraph {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleGraph.class);

    public static void main(String[] args) {
        SimpleGraph.simpleGraphCases();  // 测试普通图结构

        int a[] = {6, 6, 0, 1, 4, 3, 3, 4, 0};

        int n = a.length;

        // Function call
        Graph_to_tree(a, n);
    }

    // 定义普通图结构 这样类分开, 用于不同种类的图的生成
    // 1. 有向图 需要增加双向边才能表示2个方向
    // 2. 不允许存在单个没有边的节点 -> 没有表示接口
    // 3. 需要设置图的入口

    private static class GraphNode {  // 描述节点
        String id;
        Integer weight;

        public GraphNode(String id, Integer weight) {
            this.id = id;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "GraphNode [id=" + id + ", weight=" + weight + "]";
        }
    }

    private static class GraphEdge {  // 描述边

        String from;
        String to;
        Integer weight;

        public GraphEdge(String from, String to, Integer weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "GraphEdge [from=" + from + ", to=" + to + ", weight=" + weight + "]";
        }
    }

    // 保存graph的所有关系
    private Map<String, List<GraphNode>> nodesRelation = new HashMap<>();

    public SimpleGraph(List<GraphEdge> edges) {  // 根据边关系构建graph 从2-2关系到1-多关系
        edges.forEach(edge -> {
            String from = edge.from;
            String to = edge.to;
            Integer weight = edge.weight;

            List<GraphNode> neighbors = nodesRelation.get(from);
            if (neighbors == null) {  // 如果没有这个节点 需要增加
                nodesRelation.put(from, new ArrayList<>());
            }
            // 创建node 添加
            GraphNode node = new GraphNode(to, weight);
            nodesRelation.get(from).add(node);
        });
    }

    public void printGraph() {
        nodesRelation.entrySet().forEach(relation -> {
            String from = relation.getKey();
            List<GraphNode> tos = relation.getValue();
            LOGGER.info("{} = < {} >", from, tos);
        });
    }

    // 图的遍历  DFS BFS 省略
    public void dfsTravelHelper(String id) {  // 从哪个节点开始
        List<String> visited = new LinkedList<>();  // 已经遍历过的
        this.dfsRecursion(id, visited);
    }

    public void dfsRecursion(String id, List<String> visited) {  // 当前遍历节点id 已经遍历过的节点
        visited.add(id);
        LOGGER.info("当前深度遍历节点id = {}", id);

        if (nodesRelation.get(id) != null) {
            nodesRelation.get(id).forEach(x -> {
                if (!visited.contains(x.id)) {
                    this.dfsRecursion(x.id, visited);
                }
            });
        }
    }

    public void dfsCirculate() {
        List<String> visited = new LinkedList<>();  // 已经遍历过的
        LinkedList<String> stack = new LinkedList<>();  // 遍历顺序
        stack.addLast("A");  // 第一个root节点 需要提前设置

        while (!stack.isEmpty()) {
            String curId = stack.removeLast();
            visited.add(curId);
            LOGGER.info("当前循环遍历节点 --> {}", curId);

            List<GraphNode> nodes = nodesRelation.get(curId);
            if (nodes != null) {
                nodes.forEach(x -> {
                    if (!visited.contains(x.id)) {
                        stack.addLast(x.id);
                    }
                });
            }
        }
    }

    public static void simpleGraphCases() {
        List<GraphEdge> edges = Arrays.asList(
                new GraphEdge("A", "B", 1),
                new GraphEdge("A", "C", 1),
                new GraphEdge("A", "D", 1),
                new GraphEdge("A", "E", 1),
                new GraphEdge("A", "F", 1),
                new GraphEdge("C", "G", 1),
                new GraphEdge("D", "G", 1),
                new GraphEdge("E", "H", 1),
                new GraphEdge("F", "H", 1),
                new GraphEdge("B", "I", 1),
                new GraphEdge("G", "I", 1),
                new GraphEdge("H", "I", 1)
        );

        SimpleGraph simpleGraph = new SimpleGraph(edges);
        simpleGraph.printGraph();

        // 深度优先遍历
        simpleGraph.dfsTravelHelper("A"); // 从id = A节点开始
        // 循环方式深度遍历
        simpleGraph.dfsCirculate();
    }

    private static int find(int x, int a[],
        int vis[], int root[]) {
        if (vis[x] == 1) {
            return root[x];
        }

        vis[x] = 1;
        root[x] = x;
        root[x] = find(a[x], a, vis, root);
        return root[x];
    }

    // Function to convert directed graph into tree
    private static void Graph_to_tree(int a[], int n) {
        // Vis array to check if an index is
        // visited or not root[] array is to
        // store parent of each vertex
        int[] vis = new int[n];
        int[] root = new int[n];

        // Find parent of each parent
        for (int i = 0; i < n; i++) {
            find(a[i], a, vis, root);
        }

        // par stores the root of the resulting tree
        int par = -1;
        for (int i = 0; i < n; i++) {
            if (i == a[i]) {
                par = i;
            }
        }

        // If no self loop exists
        if (par == -1) {
            for (int i = 0; i < n; i++) {

                // Make vertex in a cycle as root of the tree
                if (i == find(a[i], a, vis, root)) {
                    par = i;
                    a[i] = i;
                    break;
                }
            }
        }

        // Remove all cycles
        for (int i = 0; i < n; i++) {
            if (i == find(a[i], a, vis, root)) {
                a[i] = par;
            }
        }

        // Print the resulting array
        for (int i = 0; i < n; i++) {
            System.out.print(a[i] + " ");
        }
    }

}









