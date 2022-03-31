package com.eron.basic.spi;

import java.util.ServiceLoader;

public class SPIApp {

	// 之前不能正常运行  需要在resource目录下创建  META-INF/services 文件夹, 创建接口全路径文件 并添加实现类 
	public static void main(String[] args) {
		ServiceLoader<CommonLog> services = ServiceLoader.load(CommonLog.class);
		
//		for(CommonLog logger : services) {
//			System.out.println("service : " + logger.info("hello"));
//			System.out.println("service : " + logger.error("ERROR!"));
//		}
		
		services.forEach(service -> {
			System.out.println("service : " + service.info("hello"));
			System.out.println("service : " + service.error("ERROR!"));
		});
		
		System.out.println(services.stream().count());
	}
	
}
