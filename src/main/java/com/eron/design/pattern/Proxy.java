package com.eron.design.pattern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代理模式
 */
public class Proxy {
    private static final Logger LOGGER = LoggerFactory.getLogger(Proxy.class);

    // 有点类似于 适配器模式, 在调用实际的实现类时 前后增加特殊的处理
    // 适配器提供不同的接口 装饰器加强接口 代理提供相同的接口

    // 实现一个缓存代理
    public interface ThirdPartLib {
        Map<String, Video> popularVideos();
        Video getVideo(String videoId);
    }

    public record Video(String id, String title, String data) {}

    public static class YouTubeSource implements ThirdPartLib {
        @Override
        public Map<String, Video> popularVideos() {
            LOGGER.info("爬虫执行: 链接视频网站, 通过认证, 爬取目标视频信息");
            LOGGER.info("爬取视频资源并下载");
            return new HashMap<>();
        }
        @Override
        public Video getVideo(String videoId) {
            LOGGER.info("根据资源id获取");
            return new Video("001", "", "");
        }
    }

    public static class YouTubeCacheProxy implements ThirdPartLib {
        private final ThirdPartLib thirdPartLib;
        private final Map<String, Video> cache = new ConcurrentHashMap<>();

        public YouTubeCacheProxy(ThirdPartLib thirdPartLib) {
            this.thirdPartLib = thirdPartLib;
        }

        @Override
        public Map<String, Video> popularVideos() {
            if (cache.isEmpty()) {
                cache.putAll(this.thirdPartLib.popularVideos());
                LOGGER.info("已缓存videos...");
            } else {
                LOGGER.info("video from cache");
            }

            return cache;
        }
        @Override
        public Video getVideo(String videoId) {
            Video video = cache.get(videoId);
            if (Objects.isNull(video)) {
                LOGGER.info("从源获取...");
                video = this.thirdPartLib.getVideo(videoId);
                cache.put(videoId, video);
            } else {
                LOGGER.info("query from cache ...");
            }

            return video;
        }
    }

    public interface Test {
        public void test();
    }

    public static class MyTest implements Test {
        @Override
        public void test() {
            LOGGER.info("implenents");
        }

    }

    /**
     * 动态代理 实现方法
     *
     * @author ERON_AMD
     * 1.拿到被代理对象的引用，然后获取他的接口 (Proxy.newProxyInstance方法)
     * 2.JDK代理重新生成一个类，同时实现我们给的代理对象所实现的接口
     * 3.把被代理对象的引用拿到了, 在静态代码块中通过反射获取到信息，我们实现的JdkDynamicProxy中的target
     * 4.重新动态生成一个class字节码
     * 5.然后编译
     */
    public static class JdkDynamicProxy implements InvocationHandler {
        private Object target;

        public static void main(String[] args) {
            Test proxy = (Test) new JdkDynamicProxy().getProxy(new MyTest());
            proxy.test();
        }

        private Object getProxy(Object obj) {
            this.target = obj;
            LOGGER.info("get proxy : {}", this.target);
            return java.lang.reflect.Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
        }

        private void doBefore() {
            LOGGER.info("doBefore...");
        }

        private void doAfter() {
            LOGGER.info("doAfter...");
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            this.doBefore();
            // 可以设计只针对个别的方法
            String methodName = method.getName();
            LOGGER.info("获取代理对象的方法名称 : {}", methodName);

            Object obj = method.invoke(this.target, args);
            this.doAfter();
            return obj;
        }
    }

}
