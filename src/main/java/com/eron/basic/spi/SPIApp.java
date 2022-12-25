package com.eron.basic.spi;

import java.util.Arrays;
import java.util.ServiceLoader;
import java.util.StringJoiner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 实际使用中，定义一个公共接口module 
 * 定义不同的实现工程，工程中引入公共接口
 * 客户端调用实现的项目，引入公共接口项目，并引入需要使用的实现项目pom，自定义实现ServiceLoader加载实现类的过程 
 * @author eron
 *
 */
public class SPIApp {
    
	private static final Logger log = LoggerFactory.getLogger(SPIApp.class);

	// 之前不能正常运行  需要在resource目录下创建  META-INF/services 文件夹, 创建接口全路径文件 并添加实现类 
	public static void main(String[] args) {
		ServiceLoader<CommonLog> services = ServiceLoader.load(CommonLog.class);
		
		services.forEach(service -> {
			log.warn("service info : {}", service.getClass().getName());
			// 获取加载的实现类全路径 拆分
			String[] classPath = service.getClass().getName().split("\\.");
			StringJoiner joiner = new StringJoiner(",");
			Arrays.asList(classPath).forEach(joiner::add);
			log.warn("class path analyse : {}", joiner.toString());
			
			log.info("-------------------------------------");
			log.info("service : {}", service.info("HELLO"));
			log.info("service : {}", service.error("ERROR!"));
		});
		
		log.info("services count : {}", services.stream().count());
	}
	
}








