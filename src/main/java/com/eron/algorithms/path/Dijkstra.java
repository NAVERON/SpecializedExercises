package com.eron.algorithms.path;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 经典的最短路径规划算法
 *
 * @author mburst/dijkstras-algorithm
 * <a href="https://github.com/mburst/dijkstras-algorithm/blob/master/Dijkstras.java"> resource </a>
 */
public class Dijkstra {
    private static final Logger log = LoggerFactory.getLogger(Dijkstra.class);

    public static void main(String[] args) {
        Graph g = new Graph();
        g.addVertex('A', Arrays.asList(new Vertex('B', 7), new Vertex('C', 8)));
        g.addVertex('B', Arrays.asList(new Vertex('A', 7), new Vertex('F', 2)));
        g.addVertex('C', Arrays.asList(new Vertex('A', 8), new Vertex('F', 6), new Vertex('G', 4)));
        g.addVertex('D', Arrays.asList(new Vertex('F', 8)));
        g.addVertex('E', Arrays.asList(new Vertex('H', 1)));
        g.addVertex('F', Arrays.asList(new Vertex('B', 2), new Vertex('C', 6), new Vertex('D', 8), new Vertex('G', 9),
                new Vertex('H', 3)));
        g.addVertex('G', Arrays.asList(new Vertex('C', 4), new Vertex('F', 9)));
        g.addVertex('H', Arrays.asList(new Vertex('E', 1), new Vertex('F', 3)));

        log.info("shortest path : {}", g.getShortestPath('A', 'H'));
    }

    public static class Vertex implements Comparable<Vertex> { // 表示图的边

        private Character id; // 字符表示的id 顶点
        private Integer distance; // 权重 距离

        public Vertex(Character id, Integer distance) {
            super();
            this.id = id;
            this.distance = distance;
        }

        public Character getId() {
            return id;
        }

        public Integer getDistance() {
            return distance;
        }

        public void setId(Character id) {
            this.id = id;
        }

        public void setDistance(Integer distance) {
            this.distance = distance;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((distance == null) ? 0 : distance.hashCode());
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Vertex other = (Vertex) obj;
            if (distance == null) {
                if (other.distance != null)
                    return false;
            } else if (!distance.equals(other.distance))
                return false;
            if (id == null) {
                return other.id == null;
            } else
                return id.equals(other.id);
        }

        @Override
        public String toString() {
            return "Vertex [id=" + id + ", distance=" + distance + "]";
        }

        @Override
        public int compareTo(Vertex o) {  // 升序 排序
            if (this.distance < o.distance)
                return -1;
            else if (this.distance > o.distance)
                return 1;
            else
                return this.getId().compareTo(o.getId());
        }

    }

    public enum Eage {
        A, B, C, D, E, F, G, H;
    }

    public static class Graph {

        private final Map<Character, List<Vertex>> vertices;  // 存储有向图 map

        public Graph() {
            this.vertices = new HashMap<>();
        }

        public void addVertex(Character character, List<Vertex> vertex) {
            this.vertices.put(character, vertex);
        }

        // 最后的结果 路径, 路径反向添加
        private final LinkedList<Character> path = new LinkedList<>();

        // 求 最短路径计算方法, 从 start到 finish
        public List<Character> getShortestPath(Character start, Character finish) {
            final Map<Character, Integer> distances = new HashMap<>();  // 保存从起始点到其他点的距离
            final Map<Character, Vertex> previous = new HashMap<>();  // 记录节点的最短路径上一个节点
            PriorityQueue<Vertex> nodes = new PriorityQueue<>();  // 计算得到最短路径的点 不断迭代更新最短距离 // 升序

            for (Character vertex : vertices.keySet()) {  // 计算起始点到其他点的距离 初始化
                if (vertex == start) {
                    distances.put(vertex, 0);
                    nodes.add(new Vertex(vertex, 0));
                } else {
                    distances.put(vertex, Integer.MAX_VALUE);  // 全部初始化为
                    nodes.add(new Vertex(vertex, Integer.MAX_VALUE));
                }
                previous.put(vertex, null);  // key节点的上一个节点
            }

            while (!nodes.isEmpty()) {
                Vertex smallest = nodes.poll();  // 这里会直接移除队列中的对象,所以后面不需要删除

                if (smallest.getId() == finish) {  // 如果找到了重点立即计算最短路径
                    while (previous.get(smallest.getId()) != null) {
                        path.addFirst(smallest.getId());
                        smallest = previous.get(smallest.getId());
                    }
                    path.addFirst(start);  // 补充起始点
                    return path;
                }

                if (distances.get(smallest.getId()) == Integer.MAX_VALUE) {  // 如果最短路径都是无穷大, 表示没有可达的路径
                    break;
                }

                for (Vertex neighbor : vertices.get(smallest.getId())) {  // 获取当前节点的邻居, 可以直接到达的对象顶点
                    Integer alt = distances.get(smallest.getId()) + neighbor.getDistance();
                    if (alt < distances.get(neighbor.getId())) {  // 更新最短距离
                        distances.put(neighbor.getId(), alt);
                        previous.put(neighbor.getId(), smallest);

                        // 优化下面的语句  与下面的意思一样
                        nodes.forEach(n -> {
                            if (n.getId() == neighbor.getId()) {  // n 已经找到了最短路径  更新nodes的路径距离
                                n.setDistance(alt);
                            }
                        });
                    }
                }
            }

            return new LinkedList<>(distances.keySet());  // 没有找到终点节点 返回所有的可达节点 list
        }
    }
}













