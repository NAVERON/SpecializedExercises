package com.eron.algorithms.strategy.smartquestion;


import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BFS 相关问题解决
 *
 * @author wangy
 */
public class BFSTravelResolve {
    private static final Logger LOGGER = LoggerFactory.getLogger(BFSTravelResolve.class);

    // 一般有2中遍历 深度优先 和广度优先  计算终结条件和方式 
    public static void main(String[] args) {
        // 查找包围区域 
        int[][] arr = new int[][]{
                {1, 1, 1, 1},
                {1, 0, 0, 1},
                {1, 1, 0, 1},
                {1, 0, 1, 1}
        };

        BFSTravelResolve.findAroundArea(arr);  // 最重要的逻辑 : 失败结束标志 --> 遇到边界; 成功 --> 队列空 且当前没有0邻居, 表示连接面积遍历完毕 
    }

    // 求未被环绕的'O' 广度优先 使用队列, 遍历终止条件, 相邻节点全部为'X' 已经在队列里面 已经遍历过, 表示队列中全部被包围, 否则全部不被包围 
    // 什么表示到了包围的尽头 ? 找到0联通区域, 如果包围 全部设置成 1, 否则, 不变化 
    private static void findAroundArea(int[][] arr) {
        for (int[] x : arr) {
            for (int t : x) {
                System.out.print(t + " ");
            }
            System.out.println("");
        }

        int m = arr.length, n = arr[0].length;

        Deque<Pos> pos = new ArrayDeque<>();  // 保存广度搜索 位置 x, y
        boolean[][] visited = new boolean[m][n];

        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[0].length; j++) {
                int cur = arr[i][j];
                if (cur == 1 || visited[i][j] == true) continue;
                visited[i][j] = true;  // 后置, 因为上面的条件有限制 

                LinkedList<Pos> around = new LinkedList<>();  // 记录当前可能的包围对象 
                pos.push(new Pos(i, j));
                around.add(new Pos(i, j));
                while (!pos.isEmpty()) {
                    LOGGER.info("进入遍历 --> {}", pos.peek());
                    Pos top = pos.pop();
                    visited[top.x][top.y] = true;

                    // 特殊处理的是 终结条件
                    List<Pos> neighbors = getNeighbor(top, m, n);
                    if (neighbors.size() < 4) {
                        // 有一个边超过了边界 表示不可能包围起来 
                        pos.forEach(p -> visited[p.x][p.y] = true);  // 进入队列表示同样的可达性 
                        pos.clear();  // 清空当前遍历的面积区域 
                        break;
                    }

                    List<Pos> validNeighbor = neighbors.stream()
                            .filter(p -> arr[p.x][p.y] == 0 && !visited[p.x][p.y])
                            .toList();
                    // 如果周围没有新增的 0 表示范围查找已经结束 
                    validNeighbor.forEach(p -> {
                        LOGGER.info("{} == 邻居 位置 --> {}", top, p);
                        pos.push(p);
                        around.push(p);
                    });

                    if (validNeighbor.isEmpty() && pos.isEmpty()) {  // 新增的邻居 都是 1 , 节点是最后一个边界
                        // 最后一个节点 没有其他0 进入, 表示环包 形成 around中所有设置为 1 
                        around.forEach(p -> {
                            arr[p.x][p.y] = 1;
                            visited[p.x][p.y] = true;
                        });
                    }
                }
            }
        }

        LOGGER.info("更新后的数组 --> ");
        for (int[] x : arr) {
            for (int t : x) {
                System.out.print(t + " ");
            }
            System.out.println("");
        }

    }

    /**
     * 尝试使用 java 最新的特性 record
     *
     * @param x x
     * @param y y
     */
    private record Pos(int x, int y) {}

    private static List<Pos> getNeighbor(Pos pos, int row, int col) {
        List<Pos> res = new LinkedList<>();
        // 上下左右判断 返回有效的邻居
        if (pos.x - 1 >= 0) res.add(new Pos(pos.x - 1, pos.y));

        int left_i = pos.x;
        int left_j = pos.y - 1;
        if (left_j >= 0) res.add(new Pos(left_i, left_j));

        int right_i = pos.x;
        int right_j = pos.y + 1;
        if (right_j <= col - 1) res.add(new Pos(right_i, right_j));

        int bottom_i = pos.x + 1;
        int bottom_j = pos.y;
        if (bottom_i <= row - 1) res.add(new Pos(bottom_i, bottom_j));

        res.forEach(p -> LOGGER.info("邻居 --> {}", p));
        return res;
    }

}









