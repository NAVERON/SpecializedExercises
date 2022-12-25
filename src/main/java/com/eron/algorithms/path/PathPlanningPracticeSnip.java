package com.eron.algorithms.path;

/**
 * Created by xukaifang on 16/9/9.
 */
import java.util.*;

/**
 * 应该是路径规划相关的snips代码片段
 * 练习和参考学习
 * @author xukaifang
 *
 */
public class PathPlanningPracticeSnip {

	private static int[][] map = null;
	private static List<Node> openList = new ArrayList<Node>();// 开启列表  没有遍历过的 
	private static List<Node> closeList = new ArrayList<Node>();// 关闭列表  遍历过的 
	private static List<Node> resultList = new ArrayList<Node>();// 结果列表  

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		String lines = sc.nextLine();
		String[] linesStr = lines.split(" ");
		int length = linesStr.length;
		int[][] map1 = new int[length][length];
		for (int i = 0; i < length; i++) {
			map1[0][i] = Integer.parseInt(linesStr[i]);
		}
		for (int i = 1; i < length; i++) {
			String tmpLines = sc.nextLine();
			String[] tmpLinesStr = tmpLines.split(" ");
			for (int j = 0; j < length; j++) {
				map1[i][j] = Integer.parseInt(tmpLinesStr[j]);
			}
		}
		// 以上 从外界获取一个正方形的矩阵   只能输入1 或者  0  表示通/不通 

		// 1代表通路、0代表障碍-----矩阵的邻接表方式记录图

		map = new int[][] {
		    { 0, 1, 0, 0}, 
		    { 0, 0, 0, 1}, 
		    { 1, 0, 1, 0}, 
		    { 0, 0, 0, 0} 
		};
		// 地图数组 { 0, 1, 0, 0}, { 0, 0, 0, 1}, { 1, 0, 1, 0}, { 0, 0, 0, 0}};

		// map=map1;

		int Max_row = map.length;
		int MAX_col = map[0].length;
		// 设置起点 终点  
		Node startPoint = new PathPlanningPracticeSnip().new Node(0, 0, null);
		Node endPoint = new PathPlanningPracticeSnip().new Node(Max_row - 1, MAX_col - 1, null);

		seachWay(map, startPoint, endPoint);
	}

	/**
	 * 搜寻最短路径
	 *
	 * @param arr  map 
	 * @param startPoint
	 * @param endPoint
	 */
	private static boolean seachWay(int[][] map, Node startPoint, Node endPoint) {
	    int row = map.length; 
	    int col = map[0].length;  // 地图边界 
	    
		final int CONST_HENG = 10;  // 垂直方向或水平方向移动的路径评分
		final int CONST_XIE = Integer.MAX_VALUE / 2;  // 斜方向移动的路径评分
		
		Node curNode = startPoint;
		if (startPoint.x < 0 || startPoint.y > col || endPoint.x < 0 || endPoint.y > col
				|| map[startPoint.x][startPoint.y] == 1 || map[endPoint.x][endPoint.y] == 1) {
			throw new IllegalArgumentException("坐标参数错误！！");
		}

		openList.add(startPoint);
		while (!openList.isEmpty() && !openList.contains(endPoint)) {  // 路径节点不为空 且 路径中不包含 终点 
			curNode = minList(openList);  // 这里可以优化 直接使用优先级队列存储  
			if ( (curNode.x == endPoint.x && curNode.y == endPoint.y ) || openList.contains(endPoint) ) {  // 如果找到了最终点 
				// System.out.println("找到最短路径");
				int count = 0;
				while (!(curNode.x == startPoint.x && curNode.y == startPoint.y)) {

					resultList.add(curNode);
					count++;
					if (curNode.parentNode != null) {
						curNode = curNode.parentNode;
					}
				}
				resultList.add(startPoint);
				Collections.reverse(resultList);

				for (Node node : resultList) {
					System.out.println(node.x + "," + node.y);
				}

				System.out.println(count);

				return true;
			}
			
			// 4 个方向探索 cost 
			// 上
			if (curNode.y - 1 >= 0) {
				checkPath(curNode.x, curNode.y - 1, curNode, endPoint, CONST_HENG);
			}
			// 下
			if (curNode.y + 1 < col) {
				checkPath(curNode.x, curNode.y + 1, curNode, endPoint, CONST_HENG);
			}
			// 左
			if (curNode.x - 1 >= 0) {
				checkPath(curNode.x - 1, curNode.y, curNode, endPoint, CONST_HENG);
			}
			// 右
			if (curNode.x + 1 < row) {
				checkPath(curNode.x + 1, curNode.y, curNode, endPoint, CONST_HENG);
			}

			openList.remove(curNode);  // 遍历过的  没遍历过的 
			closeList.add(curNode);
		}
		
		System.out.println(-1);
		System.out.print("nopath");
		return false;
		
	}

	// 核心算法---检测节点是否通路 
	private static boolean checkPath(int x, int y, Node preNode, Node endPoint, int c) {
		Node node = new PathPlanningPracticeSnip().new Node(x, y, preNode);
		// 查找地图中是否能通过
		if (map[x][y] == 1) {
			closeList.add(node);
			return false;
		}
		// 查找关闭列表中是否存在
		if (isListContains(closeList, x, y) != -1) {// 存在
			return false;
		}
		// 查找开启列表中是否存在
		int index = -1;
		if ((index = isListContains(openList, x, y)) != -1) {// 没遍历过的节点 存在
			// G值是否更小，即是否更新G，F值
			if ((preNode.g + c) < openList.get(index).g) {  // 起点  到当前节点更远 
				countG(node, endPoint, c);
				countF(node);
				openList.set(index, node);  // 更新node  
			}
		} else {
			// 不存在，添加到开启列表中
			node.setParentNode(preNode);
			count(node, endPoint, c);
			openList.add(node);
		}
		return true;
	}

	// 计算G,H,F值
	private static void count(Node node, Node eNode, int cost) {
		countG(node, eNode, cost);
		countH(node, eNode);
		countF(node);
	}

	// 计算G值
	private static void countG(Node node, Node eNode, int cost) {
		if (node.getParentNode() == null) {
			node.setG(cost);
		} else {
			node.setG(node.getParentNode().getG() + cost);
		}
	}

	// 计算H值  eNode --> 终点node坐标位置 
	private static void countH(Node node, Node eNode) {
		node.setH((Math.abs(node.getX() - eNode.getX()) + Math.abs(node.getY() - eNode.getY())) * 10);
	}

	// 计算F值
	private static void countF(Node node) {
		node.setF(node.getG() + node.getH());
	}

	// 集合中是否包含某个元素(-1：没有找到，否则返回所在的索引)
	private static int isListContains(List<Node> list, int x, int y) {
		for (int i = 0; i < list.size(); i++) {
			Node node = list.get(i);
			if (node.getX() == x && node.getY() == y) {
				return i;
			}
		}
		return -1;
	}

	// 找最小值
	private static Node minList(List<Node> list) {
		Iterator<Node> i = list.iterator();
		Node candidate = i.next();

		while (i.hasNext()) {
			Node next = i.next();
			if (next.compareTo(candidate) < 0)
				candidate = next;
		}
		return candidate;
	}

	// 节点类
	private class Node implements Comparator<Node>{
		private int x;// X坐标
		private int y;// Y坐标
		private Node parentNode;// 父类节点
		private int g;// 当前点到起点的移动耗费
		private int h;// 当前点到终点的移动耗费，即曼哈顿距离|x1-x2|+|y1-y2|(忽略障碍物)
		private int f;// f=g+h

		/**
		 * 
		 * @param x Node 坐标x 
		 * @param y Node 坐标y 
		 * @param parentNode 上一个最短路径 Node 
		 */
		public Node(int x, int y, Node parentNode) {
			this.x = x;
			this.y = y;
			this.parentNode = parentNode;
		}

		public int compareTo(Node candidate) {
			return this.getF() - candidate.getF();
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		public Node getParentNode() {
			return parentNode;
		}

		public void setParentNode(Node parentNode) {
			this.parentNode = parentNode;
		}

		public int getG() {
			return g;
		}

		public void setG(int g) {
			this.g = g;
		}

		public int getH() {
			return h;
		}

		public void setH(int h) {
			this.h = h;
		}

		public int getF() {
			return f;
		}

		public void setF(int f) {
			this.f = f;
		}

		public String toString() {
			return "(" + x + "," + y + "," + f + ")";
		}

		public void countF() {
		    this.f = this.g + this.h;
		}
		
		public Boolean positionEquals(int x, int y) {
		    return this.x == x && this.y == y;
		}
        @Override
        public int compare(Node o1, Node o2) {
            return o1.getF()- - o2.getF();
        }
	}

	// 节点比较类
//	class NodeFComparator implements Comparator<Node> {
//		@Override
//		public int compare(Node o1, Node o2) {
//			return o1.getF() - o2.getF();
//		}
//
//	}
}




