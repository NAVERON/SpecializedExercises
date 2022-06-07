package com.eron.basic.graphviz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 图的描述结构 
 * @author eron
 * 做成顶级  graph结构, 继承即可实现绘图 
 */
public class GraphForGraphviz<T> {

    private static final Logger log = LoggerFactory.getLogger(GraphForGraphviz.class);
    
    private static class GraphNode<T> {
        
        private String id;
        private String name;
        
    }
    
    
}










