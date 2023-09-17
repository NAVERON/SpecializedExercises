package com.eron.structures;

public class SimplePoint {

    private final double x;
    private final double y;

    public SimplePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double distance(double x, double y) {
        double diffX = x - this.getX();
        double diffY = y - this.getY();
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public double distance(SimplePoint another) {
        double diffX = another.getX() - this.getX();
        double diffY = another.getY() - this.getY();
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    @Override
    public String toString() {
        return "SimplePoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}









