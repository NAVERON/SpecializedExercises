/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.algorithms.strategy;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author ERON_AMD
 */
public class ScrawLabber {
    //爬楼梯算法  类似于树形结构的遍历穷举
    
    private static final Logger log = LoggerFactory.getLogger(ScrawLabber.class);
    
    public static void main(String[] args) {
        int ladder = 10;
        int maxJump = 3;
        int jumps = ladderCalCount(ladder, maxJump);
        
        log.info("max jumps : {}", jumps);
    }
    
    // 爬n层楼梯，每次最大m层，求可能情况
    public static int ladderCalCount(int ladder, int maxJump){  // 可以使用dp以空间换实践
        int jump = 0;
        if(ladder == 0){
            return 1;
        }
        if(ladder >= maxJump){
            // 需要多步
            for(int i = 1; i <= maxJump; i++){
                jump += ladderCalCount(ladder - i, maxJump);
            }
        }else{
            // 最小一部以内
            jump = ladderCalCount(ladder, ladder);
        }
        
        return jump;
    }
    
    
    
    // 有10级台阶，每次只能1-2步   动态规划的思路
    public static int ladderStepCount(int ladder, Map<Integer, Integer> map){
        
        if(ladder < 1){
            return 0;
        }
        if(ladder < 2){
            return 1;
        }
        
        if(map.containsKey(ladder)){
            return map.get(ladder);
        }else{
            int jump = ladderStepCount(ladder-1, map) + ladderStepCount(ladder - 2, map);
            map.put(ladder, jump);
            return jump;
        }
        
    }
}
