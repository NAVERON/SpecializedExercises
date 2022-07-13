package com.eron.algorithms.smartquestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 对一系列日志string处理计算 
 * 日志格式如下 
 * userid1 userid2 amount // 用户1 向用户2 交易金额amount 
 * 给定list log， 分析出交易量最大的用户id 
 * @author eron
 * 
 * 2个问题 : 
 * 题目英文 开素阅读, 了解题意 
 * 总体思路快速定下, 考虑参数校验和边界问题 
 */
public class UserTransactionLogProcess {

    private static final Logger log = LoggerFactory.getLogger(UserTransactionLogProcess.class);
    
    public static void main(String[] args) {
        List<String> logs = new ArrayList<String>() {{
            add("1 2 34");
            add("1 3 33");
            add("1 5 33");
            add("2 3 4");
            add("3 4 66");
        }};
        
        UserTransactionLogProcess transaction = new UserTransactionLogProcess();
        List<String> userids = transaction.processLog(logs, 3);
        log.info("最终结果 --> {}", userids);
        
    }
    
    // 日志 输出top n 
    /**
     * logs --> 
     * 1 2 34
     * 2 4 22 
     * 2 5 45
     * 3 --> 输出top3 
     * @param logs 日志格式 
     * @param threshold 排名top 
     * @return userid 排名 
     */
    public List<String> processLog(List<String> logs, int threshold) {
        List<String> res = null;
        
        Map<Integer, Integer> userTransaction = new HashMap<>();  // userid + transCount 
        logs.forEach(log -> {
            List<Integer> userids = Arrays.asList(log.split("\s")).stream()
                    .map(Integer::valueOf).collect(Collectors.toList());
            Integer userid1 = userids.get(0);
            Integer userid2 = userids.get(1);
            
            userTransaction.merge(userid1, 1, Integer::sum);
            if(userid1.equals(userid2)) {
                return;
            }
            userTransaction.merge(userid2, 1, Integer::sum);
        });
        
        Comparator<Map.Entry<Integer, Integer>> mapComparator = new Comparator<Map.Entry<Integer,Integer>>() {
            @Override
            public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
                return o2.getValue() - o1.getValue();  // 降序 
            }
        };
        Comparator<Integer> listComparator = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;  // 升序 
            }
        };
        
        // 交易次数记录 
        log.info("交易次数记录统计 ---> {}", userTransaction.toString());
        List<Integer> users = userTransaction.entrySet().stream()
                .sorted(mapComparator)
                .limit(threshold)
                .map(entry -> entry.getKey())
                // .sorted(listComparator)  // 再次对userid排序   -- 如果没有这里的排序 可以使用 Collections.sort() 默认升序 
                .collect(Collectors.toList());
        log.info("筛选厚的结果 --> {}", users);
        // 最终的用户id需要升序排序 
        // Collections.sort(users);  // 默认升序 
        res = users.stream().sorted(listComparator).map(userid -> userid.toString()).collect(Collectors.toList());
        // res = users.stream().map(String::valueOf).collect(Collectors.toList());
        
        return res;
    }
    
}




