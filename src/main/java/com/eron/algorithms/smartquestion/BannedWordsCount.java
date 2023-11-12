package com.eron.algorithms.smartquestion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 词频统计 统计一个字符串中单词的频率
 *
 * @author eron
 */
public class BannedWordsCount {
    private static final Logger log = LoggerFactory.getLogger(BannedWordsCount.class);

    public static void main(String[] args) {
        String a = "Bob hit a ball, the hit BALL flew far after it was hit.";
        String[] b = new String[]{
                "hit", "flew"
        };
        BannedWordsCount bannedWordsCount = new BannedWordsCount();
        bannedWordsCount.checkWordsCount(a, b);
    }

    // 暴力解法 好像也没有什么其他精妙的办法 ? 
    public void checkWordsCount(String paragraph, String[] banned) {
        String[] words = paragraph.toLowerCase().replaceAll(",", "")
                .replaceAll("\\.", "")
                .split(" ");
        List<String> banWords = new ArrayList<>(Arrays.asList(banned));

        Map<String, Integer> countsStatic = new HashMap<>();
        Arrays.asList(words).forEach(w -> {  // 时间复杂度 n + m 两个list都得遍历一遍 
            if (banWords.contains(w)) {
                return;
            }
            if (countsStatic.containsKey(w)) {
                countsStatic.put(w, countsStatic.get(w) + 1);
            } else {
                countsStatic.put(w, 1);
            }
        });
        // 这个比较其需要自定义 
        Comparator<Map.Entry<String, Integer>> mapComparator = (o1, o2) -> o2.getValue() - o1.getValue();
        countsStatic.forEach((key, value) -> log.info("entry {} : {}", key, value));
        Optional<Map.Entry<String, Integer>> result = countsStatic.entrySet().stream().min(mapComparator); // reduce 操作
        result.ifPresent(x -> log.info("stream操作直接找到 --> {}", result.get()));
    }

}











