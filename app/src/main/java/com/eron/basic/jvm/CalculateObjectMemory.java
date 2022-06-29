package com.eron.basic.jvm;

import java.lang.instrument.Instrumentation;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.lucene.util.RamUsageEstimator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 使用多种方法获取当前jvm对象占用大小 
 * @author eron 
 * 
 */
public class CalculateObjectMemory {
    
    private static final Logger log = LoggerFactory.getLogger(CalculateObjectMemory.class);
    public static void main(String[] args) {
        CalculateObjectMemory calculator = new CalculateObjectMemory();
        calculator.luceneUsage();
    }
    
    public void jdkApiUsgae() {
        
    }
    
    public void luceneUsage() {
        Map<String, String> map = new HashMap<>();
        log.info("map init value is {}", RamUsageEstimator.sizeOfMap(map));
        for(int i = 0; i < 100; i++) {
            RandomStringUtils.randomAlphabetic(100);
            map.put(RandomStringUtils.randomAlphabetic(10), RandomStringUtils.randomAlphabetic(10));
            
        }
        
        log.info("map size 100, value --- {}", RamUsageEstimator.sizeOfMap(map));
    }
    
    
}





