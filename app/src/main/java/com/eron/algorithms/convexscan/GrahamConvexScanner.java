package com.eron.algorithms.convexscan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.geometry.Point2D;



/*
 * Copyright (c) 2010, Bart Kiers
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */


/**
 * Copyright (c) 2010, Bart Kiers 
 * copy from Bart Kiers and changed 
 * 经典 graham 凸包扫描算法  
 * @author eron  https://github.com/bkiers/GrahamScan/blob/master/src/main/cg/GrahamScan.java  
 * 
 * https://www.math.net/collinear  
 */
public class GrahamConvexScanner { 
	
	private static final Logger log = LoggerFactory.getLogger(GrahamConvexScanner.class);
	
	protected static enum Turn { 
		CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR;
	}
	
    /**
     * Returns true iff all points in <code>points</code> are collinear.
     *
     * @param points the list of points.
     * @return       true iff all points in <code>points</code> are collinear.
     */
    protected static boolean areAllCollinear(List<Point2D> points) {  // 是否所有的点都在一条线上 

        if(points.size() < 2) {
            return true;
        }

        final Point2D a = points.get(0);
        final Point2D b = points.get(1);

        for(int i = 2; i < points.size(); i++) {

            Point2D c = points.get(i);

            if(getTurn(a, b, c) != Turn.COLLINEAR) {
                return false;
            }
        }
        
        return true;
    }
	
    /**
     * Returns the convex hull of the points created from <code>xs</code>
     * and <code>ys</code>. Note that the first and last point in the returned
     * <code>List&lt;java.awt.Point&gt;</code> are the same point.
     *
     * @param xs the x coordinates.
     * @param ys the y coordinates.
     * @return   the convex hull of the points created from <code>xs</code>
     *           and <code>ys</code>.
     * @throws IllegalArgumentException if <code>xs</code> and <code>ys</code>
     *                                  don't have the same size, if all points
     *                                  are collinear or if there are less than
     *                                  3 unique points present.
     */
    public static List<Point2D> getConvexHull(int[] xs, int[] ys) throws IllegalArgumentException {

        if(xs.length != ys.length) {
            throw new IllegalArgumentException("xs and ys don't have the same size");
        }

        List<Point2D> points = new ArrayList<Point2D>();

        for(int i = 0; i < xs.length; i++) {
            points.add(new Point2D(xs[i], ys[i]));
        }

        return getConvexHull(points);
    }
    
    /**
     * Returns the convex hull of the points created from the list
     * <code>points</code>. Note that the first and last point in the
     * returned <code>List&lt;java.awt.Point&gt;</code> are the same
     * point.
     *
     * @param points the list of points.
     * @return       the convex hull of the points created from the list
     *               <code>points</code>.
     * @throws IllegalArgumentException if all points are collinear or if there
     *                                  are less than 3 unique points present.
     */
    public static List<Point2D> getConvexHull(List<Point2D> points) throws IllegalArgumentException {

        List<Point2D> sorted = new ArrayList<Point2D>(getSortedPointSet(points));

        if(sorted.size() < 3) {
            throw new IllegalArgumentException("can only create a convex hull of 3 or more unique points");
        }

        if(areAllCollinear(sorted)) {
            throw new IllegalArgumentException("cannot create a convex hull from collinear points");
        }

        Stack<Point2D> stack = new Stack<Point2D>();
        stack.push(sorted.get(0));
        stack.push(sorted.get(1));

        for (int i = 2; i < sorted.size(); i++) {  // 前提 sorted 是根据角度排序好的 

            Point2D head = sorted.get(i);
            Point2D middle = stack.pop();
            Point2D tail = stack.peek();

            Turn turn = getTurn(tail, middle, head);

            switch(turn) {
                case COUNTER_CLOCKWISE:  // 如果逆时针 插入新的点 
                    stack.push(middle);
                    stack.push(head);
                    break;
                case CLOCKWISE:  // 如果顺时针, 说明中间点不是最外层的, 去掉middle点, 判断head tail 和之前的点  
                    i--;
                    break;
                case COLLINEAR:  // 新的点 与前两点在一条直线上 
                    stack.push(head);
                    break;
            }
        }

        // close the hull
        stack.push(sorted.get(0));

        return new ArrayList<Point2D>(stack);
    }
    

    /**
     * Returns a sorted set of points from the list <code>points</code>. The
     * set of points are sorted in increasing order of the angle they and the
     * lowest point <tt>P</tt> make with the x-axis. If tow (or more) points
     * form the same angle towards <tt>P</tt>, the one closest to <tt>P</tt>
     * comes first.
     *
     * @param points the list of points to sort.
     * @return       a sorted set of points from the list <code>points</code>.
     * @see GrahamScan#getLowestPoint(java.util.List)
     */
    protected static Set<Point2D> getSortedPointSet(List<Point2D> points) {

        final Point2D lowest = getLowestPoint(points);  // 获取基准点 

        TreeSet<Point2D> set = new TreeSet<Point2D>(new Comparator<Point2D>() {  // 与基准点的角度比较 
        	
            @Override 
            public int compare(Point2D a, Point2D b) { 

                if(a == b || a.equals(b)) {
                    return 0;
                }

                // use longs to guard against int-underflow  这里point2D 由先后从呢个的方法计算2点的tan值 
                double thetaA = Math.atan2((long)a.getY() - lowest.getY(), (long)a.getX() - lowest.getX());
                double thetaB = Math.atan2((long)b.getY() - lowest.getY(), (long)b.getX() - lowest.getX());

                if(thetaA < thetaB) {  // A 点与基准形成的角度小 
                    return -1;
                } else if (thetaA > thetaB) {
                    return 1;
                } else {  // 在一条线上 
                    // collinear with the 'lowest' point, let the point closest to it come first

                    // use longs to guard against int-over/underflow
                    double distanceA = Math.sqrt((((long)lowest.getX() - a.getX()) * ((long)lowest.getX() - a.getX())) +
                                                (((long)lowest.getY() - a.getY()) * ((long)lowest.getY() - a.getY())));
                    double distanceB = Math.sqrt(
                    		(((long)lowest.getX() - b.getX()) * ((long)lowest.getX() - b.getX())) +
                            (((long)lowest.getY() - b.getY()) * ((long)lowest.getY() - b.getY()))
                            );

                    if (distanceA < distanceB) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            }
        });

        set.addAll(points);

        return set;
    }

    /**
     * Returns the points with the lowest y coordinate. In case more than 1 such
     * point exists, the one with the lowest x coordinate is returned.
     *
     * @param points the list of points to return the lowest point from.
     * @return       the points with the lowest y coordinate. In case more than
     *               1 such point exists, the one with the lowest x coordinate
     *               is returned.
     */
    protected static Point2D getLowestPoint(List<Point2D> points) {  // 获取左下角的点 -> 使用点作为向量起始点 

        Point2D lowest = points.get(0);

        for(int i = 1; i < points.size(); i++) {

            Point2D temp = points.get(i);

            if(temp.getY() < lowest.getY() || (temp.getY() == lowest.getY() && temp.getX() < lowest.getX())) {
                lowest = temp;
            }
        }

        return lowest;
    }

    
	/**
     * Returns the GrahamScan#Turn formed by traversing through the
     * ordered points <code>a</code>, <code>b</code> and <code>c</code>.
     * More specifically, the cross product <tt>C</tt> between the
     * 3 points (vectors) is calculated:
     *
     * <tt>(b.x-a.x * c.y-a.y) - (b.y-a.y * c.x-a.x)</tt>
     *
     * and if <tt>C</tt> is less than 0, the turn is CLOCKWISE, if
     * <tt>C</tt> is more than 0, the turn is COUNTER_CLOCKWISE, else
     * the three points are COLLINEAR.
     *
     * @param a the starting point.
     * @param b the second point.
     * @param c the end point.
     * @return the GrahamScan#Turn formed by traversing through the
     *         ordered points <code>a</code>, <code>b</code> and
     *         <code>c</code>.
     */
    protected static Turn getTurn(Point2D a, Point2D b, Point2D c) { 

        // use longs to guard against int-over/underflow
        long crossProduct = (long) ( ( (b.getX() - a.getY()) * (c.getY() - a.getY()) ) -
                            ((b.getY() - a.getY()) * (c.getX() - a.getX())) );

        if(crossProduct > 0) {
            return Turn.COUNTER_CLOCKWISE;
        }
        else if(crossProduct < 0) {
            return Turn.CLOCKWISE;
        }
        else {
            return Turn.COLLINEAR;
        }
    }
    
    
    // 或者单独把比较器实现独立出来实现   getSortedPointSet 方法中的比较器实现 
    public static class Point2DComparator implements Comparator<Point2D> {

		@Override 
		public int compare(Point2D o1, Point2D o2) {
			// TODO Auto-generated method stub
			return 0;
		}
    	
    }
    
    public static void main(String[] args) {
		// 测试算法 
    	// ==> 官方案例1 
    	// x coordinates
    	int[] xs = {3, 5, -1, 8, -6, 23, 4};
    	// y coordinates
    	int[] ys = {9, 2, -4, 3, 90, 3, -11};
    	// find the convex hull
    	List<Point2D> convexHull = GrahamConvexScanner.getConvexHull(xs, ys); // 返回最外层点链接形成的hull 
    	for(Point2D p : convexHull) {
    	    System.out.println(p);
    	}
    	
    	System.out.println("案例2 ====================");
    	// ==> 官方案例2 
    	// the same points as the previous example
    	List<Point2D> points = Arrays.asList(
    	        new Point2D(3, 9),
    	        new Point2D(5, 2),
    	        new Point2D(-1, -4),
    	        new Point2D(8, 3),
    	        new Point2D(-6, 90),
    	        new Point2D(23, 3),
    	        new Point2D(4, -11)
    	);

    	// find the convex hull
    	List<Point2D> convexHull2 = GrahamConvexScanner.getConvexHull(points);

    	for(Point2D p : convexHull2) {
    	    System.out.println(p);
    	}
	}
    
}



















