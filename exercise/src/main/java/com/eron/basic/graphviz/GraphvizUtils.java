package com.eron.basic.graphviz;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据需要生成树或者图的图形化 
 * @author eron 
 * https://graphviz.org/ 
 * 有2种方法
 * 1. java生成描述字符串, 调用graphviz生成图形
 * 2. 使用java版本graphviz工具 使用api构建   https://github.com/nidi3/graphviz-java
 */

// 使用木板方法 写成虚类 自类覆盖关键方法实现具体的绘制 
public class GraphvizUtils {
    
    private static final Logger log = LoggerFactory.getLogger(GraphvizUtils.class);
	
	public static void main(String[] args) {
		log.info("graphviz utils ...");
		
	}

	/**
	 * 树结构绘制 
	 * @param file
	 */
	public static void treeOfGraphviz(File file) {
	    
	}
	
	/**
	 * 图的绘制 
	 * @param file
	 */
	public static void graphOfGraphviz(File file) {
	    
	}
	
	
}








