package com.eron.design.pattern;

import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 单例模式
 */
public class Singleton {
    private static final Logger LOGGER = LoggerFactory.getLogger(Singleton.class);

    private Singleton() {}

    // 子类静态对象 延迟加载单例模式实现
    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }

    public void action() {
        LOGGER.info("内部类静态对象 单例模式");
    }

    // 双重锁 单例模式
    public static class DataBase {
        private static DataBase instance;

        private DataBase() {}
        public static DataBase getInstance() {
            if (Objects.isNull(instance)) {
                synchronized (DataBase.class) {
                    if (Objects.isNull(instance)) {
                        instance = new DataBase();
                    }
                }
            }

            return instance;
        }

        public void action() {
            LOGGER.info("双重锁 单例模式");
        }
    }

    // 使用枚举类型 单例模式
    public enum SingletonEnum {
        INSTANCE;

        public void action() {
            LOGGER.info("枚举类型 单例模式");
        }
    }
}
