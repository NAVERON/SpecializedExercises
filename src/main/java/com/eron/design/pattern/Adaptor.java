package com.eron.design.pattern;

/**
 * 适配器模式
 *
 * 适配器实现了其中一个对象的接口， 并对另一个对象进行封装
 */
public class Adaptor {
    // 圆孔方钉 适配问题

    // 圆孔的定义, 圆孔大于圆钉 可以使用
    public static class RoundHole {
        public double radius;
        public RoundHole(double radius) {
            this.radius = radius;
        }

        public boolean fits(RoundPeg roundPeg) {
            return radius >= roundPeg.getRadius();
        }
    }

    public static class RoundPeg {
        public double radius;

        public RoundPeg() {}
        public RoundPeg(double radius) {
            this.radius = radius;
        }

        public double getRadius() {
            return radius;
        }
    }

    // 现在增加一个方钉
    public static class SquarePeg {
        public double width;

        public SquarePeg(double width) {
            this.width = width;
        }
    }

    // 为了让方钉也能使现判断 进入圆孔， 实现适配器
    public static class SquarePegAdaptor extends RoundPeg {
        private final SquarePeg squarePeg;

        public SquarePegAdaptor(SquarePeg squarePeg) {
            this.squarePeg = squarePeg;
        }

        @Override
        public double getRadius() {
            return Math.sqrt( Math.pow(squarePeg.width/2, 2) * 2 );
        }
    }

}
