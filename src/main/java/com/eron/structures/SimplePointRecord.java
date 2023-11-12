package com.eron.structures;

public record SimplePointRecord(double x, double y) {

    public double distance(double x, double y) {
        double diffX = x - this.x();
        double diffY = y - this.y();
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public double distance(SimplePoint another) {
        double diffX = another.x() - this.x();
        double diffY = another.y() - this.y();
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    @Override
    public String toString() {
        return "SimplePointRecord{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
