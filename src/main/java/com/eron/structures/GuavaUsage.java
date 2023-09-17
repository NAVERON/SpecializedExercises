package com.eron.structures;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.eron.algorithms.strategy.CommonParent;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultiset;

/**
 * 使用guava
 *
 * @author wangy
 */
public class GuavaUsage {
    private static final Logger log = LoggerFactory.getLogger(GuavaUsage.class);

    public static void main(String[] args) throws IOException {
        GuavaUsage guavaUsage = new GuavaUsage();
        guavaUsage.simpleJsonUsage();
        guavaUsage.guavaSimpleUsage();
        guavaUsage.commonsLangUsage();
    }

    // 使用simple json 
    public void simpleJsonUsage() throws IOException {
        JSONObject jo = new JSONObject();
        jo.put("1", "test111");
        jo.put("2", "test222");
        jo.put("foo", "bar");
        jo.put("name", null);

        log.info("输出json obj -> {}", jo.toString());

        Map<String, Object> info = new LinkedHashMap<String, Object>() {{
            put("name", "nike");
            put("paper", null);
            put("foo", 10);
        }};
        StringWriter out = new StringWriter();
        JSONValue.writeJSONString(info, out);
        log.info("JsonValue 输出内容 => {}", out.toString());

        JSONArray list = new JSONArray() {{
            add("aaa");
            add("bbb");
            add("ccc");
            add(null);
        }};
        jo.put("array", list);
        log.info("输出json数组 -> {}\n{}", list.toString(), jo.toJSONString());
    }

    // 使用guava 库经典类的使用 
    public void guavaSimpleUsage() {
        // 集合...
        ImmutableMultiset<String> sortedSet = ImmutableMultiset.<String>builder()
                .add("set111").add("set222").add("set333").build();
        List<String> simple = new ArrayList<String>() {{
            add("hello");
            add("world");
        }};
        ImmutableList<String> immutableList = ImmutableList.copyOf(simple);

        log.info("\n输出set -> {}, \n输出list -> {}", sortedSet, immutableList);
    }

    // 使用apache commons 框架工具包
    public void commonsLangUsage() {

    }
}










