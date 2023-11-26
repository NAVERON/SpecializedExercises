package com.eron.algorithms.convexscan;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;

import com.eron.structures.SimplePoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



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
 *
 * @author eron
 *
 * <p>
 * <a href = https://github.com/bkiers/GrahamScan/blob/master/src/main/cg/GrahamScan.java >GrahamScan.java</a>
 * <br>
 * <a href = https://www.math.net/collinear>https://www.math.net/collinear</a>
 * </p>
 */
public class GrahamConvexScanner {

    private static final Logger log = LoggerFactory.getLogger(GrahamConvexScanner.class);

    protected static enum Turn { // 表示 两个向量的转向
        CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR;
    }

    /**
     * Returns true if all points in <code>points</code> are collinear.
     *
     * @param points the list of points.
     * @return true if all points in <code>points</code> are collinear.
     */
    protected static boolean areAllCollinearOfSimplePoint(List<SimplePoint> points) {
        // 判断是不是所有的点都在一条线上
        if (points.size() < 2) {
            return true;
        }

        final SimplePoint a = points.get(0);
        final SimplePoint b = points.get(1);

        for (int i = 2; i < points.size(); i++) {
            SimplePoint c = points.get(i);

            if (getTurnOfSimplePoint(a, b, c) != Turn.COLLINEAR) {
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
     * @return the convex hull of the points created from <code>xs</code>
     * and <code>ys</code>.
     * @throws IllegalArgumentException if <code>xs</code> and <code>ys</code>
     *                                  don't have the same size, if all points
     *                                  are collinear or if there are less than
     *                                  3 unique points present.
     */
    public static List<SimplePoint> getConvexHullOfSimplePoint(int[] xs, int[] ys) throws IllegalArgumentException {
        // 外部数据输入
        if (xs.length != ys.length) {
            throw new IllegalArgumentException("xs and ys don't have the same size");
        }

        List<SimplePoint> points = new ArrayList<>();
        for (int i = 0; i < xs.length; i++) {
            points.add(new SimplePoint(xs[i], ys[i]));
        }

        return getConvexHullOfSimplePoint(points);
    }

    /**
     * Returns the convex hull of the points created from the list
     * <code>points</code>. Note that the first and last point in the
     * returned <code>List&lt;java.awt.Point&gt;</code> are the same
     * point.
     *
     * @param points the list of points.
     * @return the convex hull of the points created from the list
     * <code>points</code>.
     * @throws IllegalArgumentException if all points are collinear or if there
     *                                  are less than 3 unique points present.
     */
    public static List<SimplePoint> getConvexHullOfSimplePoint(List<SimplePoint> points) throws IllegalArgumentException {

        List<SimplePoint> sorted = List.copyOf(getSortedPointSetOfSimplePoint(points));

        if (sorted.size() < 3) {
            throw new IllegalArgumentException("can only create a convex hull of 3 or more unique points");
        }

        if (areAllCollinearOfSimplePoint(sorted)) {
            throw new IllegalArgumentException("cannot create a convex hull from collinear points");
        }

        Stack<SimplePoint> stack = new Stack<>();
        stack.push(sorted.get(0));
        stack.push(sorted.get(1));

        for (int i = 2; i < sorted.size(); i++) {

            SimplePoint head = sorted.get(i);
            SimplePoint middle = stack.pop();
            SimplePoint tail = stack.peek();

            Turn turn = getTurnOfSimplePoint(tail, middle, head);

            switch (turn) {
                case COUNTER_CLOCKWISE: {
                    stack.push(middle);
                    stack.push(head);
                    break;
                }
                case CLOCKWISE: {
                    i--;
                    break;
                }
                case COLLINEAR: {
                    stack.push(head);
                    break;
                }
            }
        }

        // close the hull
        stack.push(sorted.get(0));

        return new ArrayList<>(stack);
    }

    /**
     * Returns a sorted set of points from the list <code>points</code>. The
     * set of points are sorted in increasing order of the angle they and the
     * lowest point <tt>P</tt> make with the x-axis. If tow (or more) points
     * form the same angle towards <tt>P</tt>, the one closest to <tt>P</tt>
     * comes first.
     *
     * @param points the list of points to sort.
     * @return a sorted set of points from the list <code>points</code>.
     * @see GrahamConvexScanner#getSortedPointSetOfSimplePoint(java.util.List)
     */
    protected static Set<SimplePoint> getSortedPointSetOfSimplePoint(List<SimplePoint> points) {

        SimplePoint lowest = getLowestPointOfSimplePoint(points);  // 获取基准点

        // 与基准点的角度比较
        TreeSet<SimplePoint> set = new TreeSet<>((a, b) -> {

            if (a == b || a.equals(b)) {
                return 0;
            }

            // use longs to guard against int-underflow  这里point2D 由先后从呢个的方法计算2点的tan值
            double thetaA = Math.atan2((long) a.y() - lowest.y(), (long) a.x() - lowest.x());
            double thetaB = Math.atan2((long) b.y() - lowest.y(), (long) b.x() - lowest.x());

            if (thetaA < thetaB) {  // A 点与基准形成的角度小
                return -1;
            } else if (thetaA > thetaB) {
                return 1;
            } else {  // 在一条线上
                // collinear with the 'lowest' point, let the point closest to it come first
                // use longs to guard against int-over/underflow
                double distanceA = Math.sqrt((((long) lowest.x() - a.x()) * ((long) lowest.x() - a.x())) +
                        (((long) lowest.y() - a.y()) * ((long) lowest.y() - a.y())));
                double distanceB = Math.sqrt(
                        (((long) lowest.x() - b.x()) * ((long) lowest.x() - b.x())) +
                                (((long) lowest.y() - b.y()) * ((long) lowest.y() - b.y()))
                );

                if (distanceA < distanceB) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        // set = new TreeSet<>(new Point2DComparator(lowest));

        set.addAll(points);

        return set;
    }

    /**
     * 比较器单独实现
     */
    public static class Point2DComparator implements Comparator<SimplePoint> {
        private SimplePoint lowest;

        public Point2DComparator(SimplePoint lowest) {  // 点云最左下角 或者传入所有点云points
            this.lowest = lowest;
        }

        @Override
        public int compare(SimplePoint a, SimplePoint b) {
            if (a == b || a.equals(b)) {
                return 0;
            }

            double thetaA = Math.atan2((long) a.y() - lowest.y(), (long) a.x() - lowest.x());
            double thetaB = Math.atan2((long) b.y() - lowest.y(), (long) b.x() - lowest.x());
            if (thetaA < thetaB) {
                return -1;
            } else if (thetaA > thetaB) {
                return 1;
            } else {
                double distanceA = Math.sqrt((((long) lowest.x() - a.x()) * ((long) lowest.x() - a.x())) +
                        (((long) lowest.y() - a.y()) * ((long) lowest.y() - a.y())));
                double distanceB = Math.sqrt(
                        (((long) lowest.x() - b.x()) * ((long) lowest.x() - b.x())) +
                                (((long) lowest.y() - b.y()) * ((long) lowest.y() - b.y()))
                );
                if (distanceA < distanceB) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }

    /**
     * Returns the points with the lowest y coordinate. In case more than 1 such
     * point exists, the one with the lowest x coordinate is returned.
     *
     * @param points the list of points to return the lowest point from.
     * @return the points with the lowest y coordinate. In case more than
     * 1 such point exists, the one with the lowest x coordinate
     * is returned.
     */
    protected static SimplePoint getLowestPointOfSimplePoint(List<SimplePoint> points) {  // 获取左下角的点 -> 使用点作为向量起始点 

        SimplePoint lowest = points.get(0);

        for (int i = 1; i < points.size(); i++) {

            SimplePoint temp = points.get(i);

            if (temp.y() < lowest.y() || (temp.y() == lowest.y() && temp.x() < lowest.x())) {
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
     * <p>
     * and if <tt>C</tt> is less than 0, the turn is CLOCKWISE, if
     * <tt>C</tt> is more than 0, the turn is COUNTER_CLOCKWISE, else
     * the three points are COLLINEAR.
     *
     * @param a the starting point.
     * @param b the second point.
     * @param c the end point.
     * @return the GrahamScan#Turn formed by traversing through the
     * ordered points <code>a</code>, <code>b</code> and
     * <code>c</code>.
     */
    private static Turn getTurnOfSimplePoint(SimplePoint a, SimplePoint b, SimplePoint c) {

        long crossProduct = (long) (((b.x() - a.y()) * (c.y() - a.y())) -
                ((b.y() - a.y()) * (c.x() - a.x())));

        if (crossProduct > 0) {
            return Turn.COUNTER_CLOCKWISE;
        } else if (crossProduct < 0) {
            return Turn.CLOCKWISE;
        } else {
            return Turn.COLLINEAR;
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
        List<SimplePoint> convexHull = GrahamConvexScanner.getConvexHullOfSimplePoint(xs, ys); // 返回最外层点链接形成的hull
        for (SimplePoint p : convexHull) {
            System.out.println(p);
        }

        System.out.println("案例2 ====================");
        // ==> 官方案例2
        // the same points as the previous example
        List<SimplePoint> points = Arrays.asList(
                new SimplePoint(3, 9),
                new SimplePoint(5, 2),
                new SimplePoint(-1, -4),
                new SimplePoint(8, 3),
                new SimplePoint(-6, 90),
                new SimplePoint(23, 3),
                new SimplePoint(4, -11)
        );

        // find the convex hull
        List<SimplePoint> convexHull2 = GrahamConvexScanner.getConvexHullOfSimplePoint(points);

        for (SimplePoint p : convexHull2) {
            System.out.println(p);
        }

        // 需要实现堆非使用fx 库的支持
        List<SimplePoint> pointsOfSimple = Arrays.asList(
                new SimplePoint(3, 9),
                new SimplePoint(5, 2),
                new SimplePoint(-1, -4),
                new SimplePoint(8, 3),
                new SimplePoint(-6, 90),
                new SimplePoint(23, 3),
                new SimplePoint(4, -11)
        );
        List<SimplePoint> convexHull3 = GrahamConvexScanner.getConvexHullOfSimplePoint(pointsOfSimple);

        System.out.println("案例3 ====================");
        for (SimplePoint p : convexHull3) {
            System.out.println(p);
        }
    }
}



















