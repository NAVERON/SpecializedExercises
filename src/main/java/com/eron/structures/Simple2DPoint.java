package com.eron.structures;

import java.util.Objects;

public record Simple2DPoint(double x, double y) {

    public double distance(double x, double y) {
        double diffX = x - this.x();
        double diffY = y - this.y();
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    public double distance(Simple2DPoint another) {
        return this.distance(another.x, another.y);
    }

    public double atan2(double diffY, double diffX) {
        return Math.atan2(diffY, diffX);
    }

    public double atan2(Simple2DPoint another) {
        return this.atan2(another.y - this.y, another.x - this.x);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Simple2DPoint that = (Simple2DPoint) o;
        return Double.compare(x, that.x) == 0 && Double.compare(y, that.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Simple2DPoint{" + "x=" + x + ", y=" + y + '}';
    }
}









