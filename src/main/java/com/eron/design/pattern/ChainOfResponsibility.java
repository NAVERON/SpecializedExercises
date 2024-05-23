package com.eron.design.pattern;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 责任链模式
 *
 * https://refactoringguru.cn/design-patterns/chain-of-responsibility
 */
public class ChainOfResponsibility {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChainOfResponsibility.class);

    // gui事件传递模型 过滤器模型中 会有责任链模式的实现
    // 以下案例为 请求到达后 验证参数, 类似过滤器 --> 检查认证, 请求次数等
    public static abstract class Middleware {
        private Middleware next;
        // 创建自定义的过滤链
        public static Middleware linkMiddleWare(Middleware head, Middleware... nodes) {
            Middleware first = head;
            for (Middleware node : nodes) {
                first.next = node;
                first = node;
            }

            return head;
        }
        public abstract boolean check(String email, String password);

        public boolean checkNext(String email, String password) {
            if (Objects.isNull(next)) {
                return true;
            }

            return next.check(email, password);
        }
    }
    // 检查请求数量是否超过限制额度
    public static class ThrottlingMiddleware extends Middleware {
        private int requestCount; // 当前请求次数
        private long curTime;
        private int requestPerMin;

        public ThrottlingMiddleware(int requestPerMin) {
            curTime = System.currentTimeMillis();
            this.requestPerMin = requestPerMin;
        }

        @Override
        public boolean check(String email, String password) {
            LOGGER.info("请求检查");
            if (System.currentTimeMillis() > curTime + 60_000) {
                requestCount = 0;
                curTime = System.currentTimeMillis();
            }

            requestCount++;
            if (requestCount > requestPerMin) {
                LOGGER.info("达到最大限制请求次数, 拦截直接返回拒绝访问");
                return false;
            }

            LOGGER.info("请求检查通过");
            return checkNext(email, password);
        }
    }
    // 检查用户是否存在
    public static class UserExistMiddleWare extends Middleware {
        private FakeService service;
        public UserExistMiddleWare(FakeService service) {
            this.service = service;
        }

        @Override
        public boolean check(String email, String password) {
            LOGGER.info("用户检查");
            if (service.hasUser(email)) {
                LOGGER.info("用户检查通过");
                return checkNext(email, password);
            }

            LOGGER.info("不存在当前请求用户id");
            return false;
        }
    }
    // 检查用户角色
    public static class UserRoleMiddleWare extends Middleware {
        @Override
        public boolean check(String email, String password) {
            LOGGER.info("检查请求角色");
            if ("admin@root.com".equals(email)) {
                LOGGER.info("管理员身份进入");
                return checkNext(email, password);
            }

            LOGGER.info("非管理员访问");
            return checkNext(email, password);
        }
    }

    public static class FakeService {
        private Middleware middleware;
        private Map<String, String> users = new ConcurrentHashMap<>();
        public void setMiddleWare(Middleware middleware) {
            this.middleware = middleware;
        }

        public void register(String email, String password) {
            users.put(email, password);
        }
        public boolean hasUser(String email) {
            return users.containsKey(email);
        }
        public boolean isValidUser(String email, String password) {
            return users.containsKey(email) && users.get(email).equals(password);
        }

        public boolean login(String email, String password) {
            return middleware.check(email, password);
        }
    }

}
