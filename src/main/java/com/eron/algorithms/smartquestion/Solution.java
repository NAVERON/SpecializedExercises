package com.eron.algorithms.smartquestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 根据输入句子随机生成随机句子 按照规则,第一个随机,后面的根据前一个单词在原句子中查找
 * 比如 原句子中 is 后跟 and  a等单词, 生成的句子中需要随机生成这些
 *
 * @author eron
 */
public class Solution {
    private static final Logger log = LoggerFactory.getLogger(Solution.class);

    public static void main(String[] args) {
        Solution solution = new Solution();

        // 根据一句话随机生成一句话 第一个单词随机, 从第二个开始 后面的单词必须在原句子中出现过连接 如bad后跟 words
        String input = "is and this is a bad words generate and generate sentence is a good idea ";
        Integer length = 5;
        List<String> result = solution.generateSentence(input, length);
        log.info("输出最终生成的句子--> \n{}\n{}\n{}",
                solution.generateSentence(input, length),
                solution.generateSentence(input, length),
                solution.generateSentence(input, length)
        );
    }

    // 给定一句话 根据这一句话随机生成一定长度的句子 
    public List<String> generateSentence(String input, Integer length) {
        List<String> res = new LinkedList<String>();

        List<String> words = new ArrayList<String>(Arrays.asList(input.split("\s")));
        Map<String, Set<Integer>> mapper = this.preDeal(words);
        // 生成第一个单词 
        Random rd = new Random();
        int randomIndex = rd.nextInt(words.size());
        String start = words.get(randomIndex);
        res.add(start);

        for (int i = 0; i < length - 1; i++) {
            start = this.getNextIndex(words, mapper, start);
            res.add(start);
        }

        return res;
    }

    // 提前处理索引信息 保存的是当前单词后一个单词的索引 
    public Map<String, Set<Integer>> preDeal(List<String> words) {
        Map<String, Set<Integer>> mapper = new HashMap<String, Set<Integer>>();
        int n = words.size();
        for (int i = 0; i < n; i++) {
            String word = words.get(i);
            if (mapper.containsKey(word)) {
                int adder = i >= n - 1 ? 0 : i + 1;
                mapper.get(word).add(adder);
            } else {
                Set<Integer> indexs = new HashSet<Integer>();
                int adder = i >= n - 1 ? 0 : i + 1;
                indexs.add(adder);
                mapper.put(word, indexs);
            }
        }

        return mapper;
    }

    public String getNextIndex(List<String> words, Map<String, Set<Integer>> mapper, String start) {
        String next = null;
        LinkedList<Integer> randomSet = new LinkedList<Integer>(mapper.get(start));
        Collections.shuffle(randomSet);
        next = words.get(randomSet.getFirst());

        return next;
    }
}










