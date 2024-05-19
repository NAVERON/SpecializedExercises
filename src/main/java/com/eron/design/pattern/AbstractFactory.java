package com.eron.design.pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 虚拟工厂
 *
 * 有一个工厂可以生成 A B C 。。。多个产品， 但是在不同的风格或平台上需要生产不同的产品
 */
public class AbstractFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractFactory.class);

    // 工厂抽象
    public interface AbstractGuiFramework {
        MyButton createButton();
        MyCheckBox createCheckBox();
    }

    public static class WinGui implements AbstractGuiFramework {
        @Override
        public MyButton createButton() {
            return new MyWinButton();
        }
        @Override
        public MyCheckBox createCheckBox() {
            return new MyWinCheckBox();
        }
    }
    public static class UnixLikeGui implements AbstractGuiFramework {
        @Override
        public MyButton createButton() {
            return new MyLinuxButton();
        }
        @Override
        public MyCheckBox createCheckBox() {
            return new MyLinuxCheckBox();
        }
    }

    // 产品抽象
    public interface MyButton {
        void paint();
        void onHover();
    }
    public interface MyCheckBox {
        void paint();
        void onChecked();
    }

    // 两种产品在不同平台上不同的实现
    public static class MyWinButton implements MyButton {
        @Override
        public void paint() {
            LOGGER.info("windows button paint");
        }
        @Override
        public void onHover() {
            LOGGER.info("windows button hover");
        }
    }
    public static class MyWinCheckBox implements MyCheckBox {
        @Override
        public void paint() {
            LOGGER.info("windows checkbox paint");
        }
        @Override
        public void onChecked() {
            LOGGER.info("windows checkbox checked");
        }
    }
    public static class MyLinuxButton implements MyButton {
        @Override
        public void paint() {
            LOGGER.info("linux button paint");
        }
        @Override
        public void onHover() {
            LOGGER.info("linux button hover");
        }
    }
    public static class MyLinuxCheckBox implements MyCheckBox {
        @Override
        public void paint() {
            LOGGER.info("linux checkbox paint");
        }
        @Override
        public void onChecked() {
            LOGGER.info("linux checkbox checked");
        }
    }

}
