package com.eron.design.pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 组合模式
 */
public class Composite {
    private static final Logger LOGGER = LoggerFactory.getLogger(Composite.class);

    public interface Graphic {
        void move(int x, int y);
        void draw();
    }

    public static class Dot implements Graphic { // 或者实现一个基础图形类 然后其他基础图形继承实现/覆盖
        public int x, y;
        public Dot(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public void move(int x, int y) {
            LOGGER.info("dot move");
        }
        @Override
        public void draw() {
            LOGGER.info("dot draw");
        }
    }

    public static class Triangle extends Dot {
        public int radius;
        public Triangle(int x, int y, int radius) {
            super(x, y);
            this.radius = radius;
        }
        @Override
        public void move(int x, int y) {
            LOGGER.info("circle move");
        }
        @Override
        public void draw() {
            LOGGER.info("circle draw");
        }
    }

    public static class CompoundGraphic implements Graphic { // 相当于图形组合
        public List<Graphic> graphics = new ArrayList<>();

        public CompoundGraphic(Graphic... graphic) {
            this.graphics.addAll(Arrays.asList(graphic));
        }
        @Override
        public void move(int x, int y) {
            graphics.forEach(graphic -> graphic.move(x, y));
            LOGGER.info("compound graphic move");
        }
        @Override
        public void draw() {
            graphics.forEach(Graphic::draw);
            LOGGER.info("compound graphic draw");
        }
        public void addGraphic(Graphic graphic) {
            graphics.add(graphic);
        }
        public void removeGraphic(Graphic graphic) {
            graphics.removeIf(child -> child.equals(graphic));
        }
    }

}
