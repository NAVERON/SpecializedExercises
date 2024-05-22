package com.eron.design.pattern;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.XMLFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 享元模式
 *
 * 一般使用为 缓存 cache 密集重复对象管理
 */
public class FlyWeight {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlyWeight.class);

    // 我的理解: 抽取变化和固定的部分, 可枚举的部分独立出来 缓存, 对象中使用引用公共对象
    public record TreeType(String name, String color, String other) {}
    public record Tree(int x, int y, TreeType type) {}

    // 享元对象 缓存
    public static class TreeTypeFactory {
        private static Map<String, TreeType> cache = new ConcurrentHashMap<>();

        public static TreeType getByKey(String name, String color, String other) {
            TreeType result = cache.get(name);
            if (Objects.isNull(result)) {
                result = new TreeType(name, color, other);
                cache.put(name, result);
            }

            return result;
        }
    }

    public static class Forest {
        public void plantTree(int x, int y, String name, String color, String other) {
            TreeType type = TreeTypeFactory.getByKey(name, color, other);
            Tree tree = new Tree(x, y ,type);
            LOGGER.info("创建新的tree --> {}", tree);
        }
    }
}
