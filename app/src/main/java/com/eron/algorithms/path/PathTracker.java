package com.eron.algorithms.path;

import java.util.logging.Logger;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * 实验性短代码snip
 * 实现判断船舶航行过程中按照航线航行，偏离航线的距离大小，在 1  2  3 航线的哪一段上  计算方法，今后可以逐步改善，融合到船舶航行仿真
 * @author eron
 *
 */
/**
 * 判断一个点属于路径轨迹上的哪一段路径
 * @author ERON
 * @function
 * 先前使用距离算法，计算点到两点连线的距离，根据距离判断  运动点是否属于路径
 * 之后存在问题，换作面积算法，求取面积并根据面积的大小判断属于哪段路径，实现判断关系的方法
 * 总体上还是存在一定的局限性，并不是通用解法，只能作为求解参考
 * 后续需要考虑：多个点的话 会存在问题，根据实际的情况来重新设计算法
 * @version  V1.1
 * @other  使用JavaFx Canvas的功能，现在看来十分好用，以后经常做练习才好
 */
public class PathTracker extends Application {

	private static final Logger log = Logger.getLogger(PathTracker.class.getName());

	GraphicsContext g;
	Canvas canvas;

	Point2D[] points = { // 路径点坐标
			new Point2D(50, 350), new Point2D(50, 200), new Point2D(200, 200), new Point2D(300, 100) 
	};

	double mouseX, mouseY;

	public Pane createContent() {
		Pane content = new Pane();
		canvas = new Canvas(600, 400);
		g = canvas.getGraphicsContext2D();
		content.getChildren().add(canvas);

		canvas.setOnMouseClicked(e -> { // 点击鼠标设置某一点的位置，同时判断属于哪个路径 
			
			mouseX = e.getX();
			mouseY = e.getY();
			log.info(mouseX + " : " + mouseY);
			// g.strokeOval(mouseX - 5, mouseY - 5, 10, 10);
			
			if(e.getButton() == MouseButton.PRIMARY) {  // 主键 
				log.info("左键");
				commonRepaint();

				// 判断路径算法
				Point2D cur = new Point2D(mouseX, mouseY);
//				double dis1, dis2, dis3;
				double area1, area2, area3;
//				dis1 = dis(cur, points[0], points[1]);
//				dis2 = dis(cur, points[1], points[2]);
//				dis3 = dis(cur, points[2], points[3]);
				// 先判断点在哪一个三角形上，再根据距离判断
				// 距离判断方法失效，使用面积方法
				area1 = area(cur, points[0], points[1]);
				area2 = area(cur, points[1], points[2]);
				area3 = area(cur, points[2], points[3]);
				// 计算面积, 最小表示在航段上  不能弥补钝角三角形, 添加钝角检测, bugfix 延长线上的情况 
				if (area1 < area2 && area1 < area3) {
					log.info("1");
					drawArea(cur, points[0], points[1]);
				} else if (area2 < area1 && area2 < area3) {
					log.info("2");
					drawArea(cur, points[1], points[2]);
				} else if (area3 < area1 && area3 < area2) {
					log.info("3");
					drawArea(cur, points[2], points[3]);
				}
			} else if (e.getButton() == MouseButton.MIDDLE){ // 中间建 
				log.info("中间键");
				commonRepaint();
				
			} else if (e.getButton() == MouseButton.SECONDARY) { // 右键 
				log.info("右键");
				commonRepaint();
				
				// 圆形范围法 
				Point2D cur = new Point2D(mouseX, mouseY);
				Double dis1 = disOfCircle(cur, points[0], points[1]);
				Double dis2 = disOfCircle(cur, points[1], points[2]);
				Double dis3 = disOfCircle(cur, points[2], points[3]);
				if (dis1 <= dis2 && dis1 <= dis3) {
					// 在第一段 
					log.info("111");
					drawCircle(cur, points[0], points[1]);
				} else if (dis2 <= dis1 && dis2 <= dis3) {
					// 第二段 
					log.info("222");
					drawCircle(cur, points[1], points[2]);
				} else if (dis3 <= dis1 && dis3 <= dis2) {
					// 第三段 
					log.info("333");
					drawCircle(cur, points[2], points[3]);
				}
				
			}
			
			
		});

		drawPath();
		return content;
	}

	public void drawPath() {
		g.setStroke(Color.BLACK);
		// 绘制出已经存在的坐标连线
		double pointX[] = new double[4], pointY[] = new double[4];
		for (int i = 0; i < points.length; i++) {
			pointX[i] = points[i].getX();
			pointY[i] = points[i].getY();
		}
		g.strokePolyline(pointX, pointY, 4);
	}
	
	public void drawArea(Point2D x, Point2D y, Point2D z) {
		g.setStroke(Color.RED);
		double[] pointsX = new double[] {
					x.getX(), y.getX(), z.getX() 
				}, 
				pointsY = new double[] {
					x.getY(), y.getY(), z.getY() 
				};
		
		g.strokePolygon(pointsX, pointsY, 3);
		
		g.strokeOval(x.getX() - 5, x.getY() - 5, 10, 10);
	}
	
	public void drawCircle(Point2D cur, Point2D start, Point2D end) {
		g.setStroke(Color.GREEN);
		// 绘制2点的园 
		Point2D center = start.midpoint(end);
		Double distance = start.distance(end);
		
		g.strokeOval(center.getX() - distance/2, center.getY() - distance/2, distance, distance);
		g.strokeOval(cur.getX() - 5, cur.getY() - 5, 10, 10);
		
	}
	
	public void commonRepaint() {
		g.setStroke(Color.BLACK);
		g.clearRect(0, 0, 600, 400);
		drawPath();
	}

	private static double dis(Point2D X, Point2D A, Point2D B) { // 点X距离AB直线的最小距离
		return Math.abs((A.getX() - X.getX()) * (B.getY() - X.getY()) - (B.getX() - X.getX()) * (A.getY() - X.getY()))
				/ Math.sqrt(Math.pow(A.getX() - B.getX(), 2) + Math.pow(A.getY() - B.getY(), 2));
	}

	private static double area(Point2D X, Point2D A, Point2D B) {
		return 0.5 * Math
				.abs((B.getX() - A.getX()) * (X.getY() - A.getY()) - (X.getX() - A.getX()) * (B.getY() - A.getY()));
	}
	
	public Double disOfCircle(Point2D cur, Point2D start, Point2D end) {
		
		// 计算 ab的圆心 和 距离 
		Point2D center = start.midpoint(end);
		Double distance = start.distance(end);
		
		return cur.distance(center);
	}

	public void start(Stage primaryStage) {
		Scene scene = new Scene(createContent());
		primaryStage.setScene(scene);
		primaryStage.setTitle("TrackerPoints");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	public static class Launcher {
		public static void main(String[] args) {
			PathTracker.main(args);
		}
	}

}









