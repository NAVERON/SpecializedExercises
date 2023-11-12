package com.eron.structures;

public record SimplePoint(double x, double y) {

    public double distance(double x, double y) {
        double diffX = x - this.x();
        double diffY = y - this.y();
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public double distance(SimplePoint another) {
        return this.distance(another.x, another.y);
    }

    @Override
    public String toString() {
        return "SimplePoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}









