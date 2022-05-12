package com.eron.structures;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 字典树的实现 主要应用在输入提示和前缀匹配的场景中
 * 
 * @author eron 实现前缀匹配输出的功能, 词频统计等
 */
public class TrieBuilder {

    private static final Logger log = LoggerFactory.getLogger(TrieBuilder.class);

    private static class TrieNode { // trie node结构定义

        private Character c; // 当前节点的字符 应当限制为字母范围
        private Integer count = 0; // 词库经过当前节点的次数
        // private String part; // 到当前为止组成的字符串
        // private TrieNode parent; // 父节点
        private Boolean terminal = false; // 是否是终结字符
        private TreeMap<Character, TrieNode> subs; // 下级节点 红黑树(或者链表实现也可以)

        public TrieNode(Character c, Boolean terminal) {
            this.c = c;
            this.terminal = terminal;

            subs = new TreeMap<Character, TrieBuilder.TrieNode>(); // 初始化 否则subs是null
        }

        public Integer countIncr() {
            return ++this.count;
        }

        public Boolean isTerminal() {
            return this.terminal;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("[").append(this.c).append(this.count).append(this.terminal).append("]-->")
                    .append(subs.keySet().toString());

            return sb.toString();
        }

    }

    private List<String> origin = new LinkedList<String>(); // 输入原始构建数据
    private TrieNode root = new TrieNode('/', false); // trie 根

    public TrieBuilder append(String s) {
        this.origin.add(s);
        return this;
    }

    public TrieBuilder append(List<String> s) {
        this.origin.addAll(s);
        return this;
    }

    public void build() {
        // 使用输入的字符串生成trie树
        this.origin.forEach(s -> {
            char[] charSequence = s.toCharArray();
            log.info("parse a string to char array -> {}", charSequence.toString());

            TrieNode index = root;
            for (int i = 0; i < charSequence.length; i++) { // 区分终结字符
                char c = charSequence[i];

                // 判断是否已经存在
                if (index.subs.get(c) != null) {
                    index.subs.get(c).countIncr(); // 更新路径经过次数 表示输入的字符串公共前缀的数量

                    // 查找和插入 当前的前缀已经存在
                    index = index.subs.get(c);
                    continue;
                }

                // 判断是否时结束字符 创建新的TrieNode
                Boolean terminal = i == charSequence.length - 1 ? true : false;
                TrieNode node = new TrieNode(c, terminal);
                index.subs.put(c, node);

                index = index.subs.get(c);
            }

        });
    }

    @Deprecated
    public Integer insertTrieNode(TrieNode node) {
        // 插入TrieNode 返回插入结果 -1 无法插入/已存在, 0 插入成功
        Integer status = -1;

        return status;
    }

    // 移除路径点
    public Integer removeTriePath(String s) {
        // 移除string s 路径上的所有值
        Integer status = -1;

        return status;
    }

    // 移除一个特定的节点
    @Deprecated
    public Integer removeTrieNode(TrieNode node) {
        // 给出的TrieNode
        Integer status = -1;

        return status;
    }

    // 查找信息树中是否有完全匹配的
    public Boolean query(String s) {
        char[] charSequence = s.toCharArray();
        TrieNode index = root;

        for (Character c : charSequence) {
            TrieNode x = index.subs.get(c);

            if (Objects.isNull(x)) {
                // 没有查到
                return false;
            }

            index = x;
        }

        // index 非 null表示一直到终结, 否则表示
        return Objects.isNull(index) ? false : true;
    }

    // 查找经过的路径
    public List<TrieNode> queryPath(String s) {
        char[] charSequence = s.toCharArray();
        TrieNode index = root;
        List<TrieNode> result = new ArrayList<TrieBuilder.TrieNode>(charSequence.length);

        for (Character c : charSequence) {
            TrieNode x = index.subs.get(c);

            if (Objects.isNull(x)) {
                // 没有查到
                break;
            }
            result.add(x);

            index = x;
        }

        return result;
    }

    public static void main(String[] args) {
        // 创建trie
        List<String> origin = new LinkedList<String>();
        origin.add("hello");
        origin.add("who");
        origin.add("testing");
        origin.add("purge");
        origin.add("thing");

        // 创建trie 信息结构
        TrieBuilder builder = new TrieBuilder();
        builder.append(origin).build();

        // 统计
        Boolean result = builder.query("baba");
        log.info("查询结果 - > {}", result.toString());
        List<TrieNode> path = builder.queryPath("testx");
        log.info("查询路径结果 -> {}", path);

    }

}
