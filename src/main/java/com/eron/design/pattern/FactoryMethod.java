package com.eron.design.pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 工厂方法: 创建型
 *
 * 工厂方法模式是一种创建型设计模式， 其在父类中提供一个创建对象的方法， 允许子类决定实例化对象的类型
 */
public class FactoryMethod {
    private static final Logger LOGGER = LoggerFactory.getLogger(FactoryMethod.class);

    // 虚拟抽象 模板对象类
    // 通常这个不是虚拟类， 可以使一个普通类，然后子类继承和重新实现关键方法, 替换上级初始化的过程...
    public static abstract class MyDialog {

        public abstract MyButton createButton();

        public void render() {
            MyButton button = createButton();
            button.render();
            button.onClick();
        }
    }

    public static class MyDialogA extends MyDialog {

        @Override
        public MyButton createButton() {
            return new MyButtonA();
        }
    }

    public static class MyDialogB extends MyDialog {

        @Override
        public MyButton createButton() {
            return new MyButtonB();
        }
    }

    public interface MyButton { // 或者使用虚拟类 形成一个默认的方法
        void render();
        void onClick();
    }

    public static class MyButtonA implements MyButton {

        @Override
        public void render() {
            LOGGER.info("render button A");
        }

        @Override
        public void onClick() {
            LOGGER.info("button A clicked");
        }
    }

    public static class MyButtonB implements MyButton {

        @Override
        public void render() {
            LOGGER.info("render button B");
        }

        @Override
        public void onClick() {
            LOGGER.info("button B clicked");
        }
    }

}





