package com.eron.design.pattern;

import java.util.Objects;

public class Prototype {

    public static abstract class Shape {
        public int x, y;
        public String color;

        public Shape() {}
        public Shape(Shape source) {
            if (Objects.nonNull(source)) {
                this.x = source.x;
                this.y = source.y;
                this.color = source.color;
            }
        }

        public abstract Shape clone();
    }

    public static class Rectangle extends Shape {
        public int width, height;

        public Rectangle(){}
        public Rectangle(Rectangle rectangle) {
            super(rectangle);
            if (Objects.nonNull(rectangle)) {
                this.width = rectangle.width;
                this.height = rectangle.height;
            }
        }

        @Override
        public Shape clone() {
            return new Rectangle(this);
        }

        @Override
        public String toString() {
            return "Rectangle{" +
                "width=" + width +
                ", height=" + height +
                ", x=" + x +
                ", y=" + y +
                ", color='" + color + '\'' +
                '}';
        }
    }

    public static class Circle extends Shape {
        public int radius;

        public Circle() {}
        public Circle(Circle circle) {
            super(circle);
            if (Objects.nonNull(circle)) {
                this.radius = circle.radius;
            }
        }

        @Override
        public Shape clone() {
            return new Circle(this);
        }

        @Override
        public String toString() {
            return "Circle{" +
                "radius=" + radius +
                ", x=" + x +
                ", y=" + y +
                ", color='" + color + '\'' +
                '}';
        }
    }
}
