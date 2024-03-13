package com.eron.basic.spi;

import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 框架性使用spi机制  无限拓展实现类
 *
 * @author eron
 * 参考文档博客 https://reflectoring.io/service-provider-interface/
 */
public class SPIFramework {
    // 当前实现只针对某个接口的实现扫描 
    private static final Logger log = LoggerFactory.getLogger(SPIFramework.class);

    public static void main(String[] args) {
        // 测试框架
        CommonLog impl = SPIFramework.getInstance().getImplByName("SimpleJDKLogger").orElseThrow(
            IllegalCallerException::new);
        log.info("获取到的实现 --> {}, {}",
            impl.info("impl->info[method]"),
            impl.error("impl->[error]")
        );
    }

    private static SPIFramework commonLogService = null;
    private final ServiceLoader<CommonLog> loader;

    public static synchronized SPIFramework getInstance() {  // 获取单例实例 
        if (commonLogService == null) {
            commonLogService = new SPIFramework();
        }
        return commonLogService;
    }

    private SPIFramework() {
        loader = ServiceLoader.load(CommonLog.class);
    }

    public Optional<CommonLog> getImplByName(String name) {
        log.info("getImplByName --> {}", name);

        // for test 
        loader.stream().map(ServiceLoader.Provider::get).forEach(
                service -> log.info("names --> {}, {}, {}, {}",
                        service.getClass().getName(),
                        service.getClass().getCanonicalName(),
                        service.getClass().getPackageName(),
                        service.getClass().getTypeName()
                )
        );

        // 根据名称获取实现 
        Optional<CommonLog> res = loader.stream().map(ServiceLoader.Provider::get)
                .filter(impl -> impl.getClass().getSimpleName().equals(name))
                .findFirst();

        return res;
    }
}






