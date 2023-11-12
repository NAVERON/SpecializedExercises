package com.eron.structures;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import guru.nidi.graphviz.attribute.Color;
import guru.nidi.graphviz.attribute.Rank;
import guru.nidi.graphviz.attribute.Rank.RankDir;
import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.MutableGraph;

/**
 * 参考资料 https://linux.thai.net/~thep/datrie/datrie.html
 * https://zhuanlan.zhihu.com/p/80325757
 * 在trie的基础上 增加fail链接, 相当于kmp算法中的next指针
 * Trie 树的实现 思路跟这里差不多
 *
 * @author eron
 * @see com.eron.structures.TrieBuilder
 */
// 其余比较好的实现方法 https://github.com/hankcs/HanLP 
public class ACautomaton {

    private static final Logger log = LoggerFactory.getLogger(ACautomaton.class);

    public static void main(String[] args) {
        ACautomaton auto = new ACautomaton();
        List<String> list = new ArrayList<>() {{
            add("he");
            add("she");
            add("his");
            add("hers");
        }};
        // 实现ac自动机 多模式匹配
        Trie trie = new Trie();
        auto.createGoto(trie, list);
        auto.createFail(trie);

        auto.drawTrie(trie, "/home/eron/Desktop/ac_pic.png");  // 是否输出trie图 
        String text = "one day she say her has eaten many shrimps";
        List<String> res = auto.match(trie, text);

        log.info("匹配结果 --> {}", res);
    }

    private static class Node {
        String id;
        Map<Character, Node> children = new HashMap<>();
        String pattern = "";
        int endCount = 0;
        Node fail;  // 失败指针
        String path = "/";  // 节点链

        public Node(String id) {
            this.id = id;
        }
    }

    private static class Trie {
        Node root;
        int count = 0;

        public Trie() {
            this.root = new Node("/");
        }

        public void insert(String word) {
            Node cur = this.root;
            for (int i = 0, wdLength = word.length(); i < wdLength; i++) {
                char x = word.charAt(i);
                Node child = cur.children.get(x);
                if (child == null) {
                    child = new Node(Character.toString(x));
                    child.path = cur.path + "->" + child.id;  // 保存路径

                    cur.children.put(x, child);

                    this.count++;
                }

                cur = child;
            }
            cur.pattern = word;
            cur.endCount++;
        }
    }

    public void createGoto(Trie trie, List<String> patterns) {  // 生成 
        patterns.forEach(p -> {
            trie.insert(p);
        });
    }

    public void createFail(Trie trie) {  // 失败节点 
        // trie 广度优先遍历 
        Node root = trie.root;
        Queue<Node> queue = new ArrayBlockingQueue<>(trie.count);
        queue.add(root);

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node != null) {
                node.children.entrySet().forEach(childEntry -> {
                    Character i = childEntry.getKey();
                    Node child = childEntry.getValue();

                    if (node == root) {  // 如果是根节点 失败链指向自己
                        child.fail = root;
                    } else {
                        Node p = node.fail;
                        while (p != null) {
                            if (p.children.get(i) != null) {  // 存在相同的子
                                child.fail = p.children.get(i);
                                break;
                            }
                            p = p.fail;
                        }

                        if (p == null) {
                            child.fail = root;
                        }
                    }

                    queue.add(child);
                });

            }
        }

        root.fail = root;  // 不处理 root的fail指针是null; 不能在循环中处理, 因为在回溯fail指针有无限循环 所以只能最后处理 
    }

    public List<String> match(Trie trie, String text) {  // 匹配 
        List<String> res = new ArrayList<String>();  // 所有匹配到的patterns
        Set<String> unique = new HashSet<String>();  // 匹配到的patterns 全重后的

        Node root = trie.root, p = root;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            while (p.children.get(c) == null && p != root) {
                p = p.fail;
            }

            p = p.children.get(c);  // 第一个c 匹配成功
            if (p == null) {
                p = root;  // 如果没有匹配的 
            }
            // 以上的逻辑是判断是否匹配到, 否则一直在root

            Node node = p;
            while (node != root) {  // 如果第一层匹配到
                if (node.endCount > 0) {  // 是单词的终点  匹配到了完整的单词
                    int pos = i - node.pattern.length() + 1;  // 获取匹配到终结点的索引起始位置 
                    log.info("模式匹配串 {} 其起始位置 {}", node.pattern, pos);

                    if (!unique.contains(node.pattern)) {
                        unique.add(node.pattern);

                        res.add(node.pattern);
                    }
                }

                node = node.fail;
            }
        }

        return res;
    }

    public void drawTrie(Trie trie, String filePath) {
        // 实现绘制生成的ac自动机 
        MutableGraph ac = Factory.mutGraph("ac_automaton_trie")
                .setDirected(true).graphAttrs().add(Rank.dir(RankDir.TOP_TO_BOTTOM));

        Queue<Node> queue = new ArrayBlockingQueue<>(trie.count);
        Node root = trie.root;
        queue.add(root);
        ac.add(  // 提前绘制好 root的死循环 真实情况下root是不需要fail的 
                Factory.mutNode(root.path)
                        .addLink(
                                Factory.to(Factory.mutNode(root.fail.path)).with(Style.DASHED)
                        )
        );

        while (!queue.isEmpty()) {
            Node node = queue.poll();
            if (node != null) {
                node.children.entrySet().forEach(child -> {
                    Character c = child.getKey();
                    Node n = child.getValue();
                    queue.offer(n);

                    // 需要提前定义唯一id 因为在这里,每个节点的id可能相同 
                    if (n.endCount > 0) {
                        // 终点节点 
                        ac.add(
                                Factory.mutNode(node.path)
                                        .addLink(Factory.mutNode(n.path).add(Color.RED))
                        );
                    } else {
                        ac.add(
                                Factory.mutNode(node.path)
                                        .addLink(Factory.mutNode(n.path))
                        );
                    }

                    ac.add(
                            Factory.mutNode(n.path)
                                    .addLink(Factory.to(Factory.mutNode(n.fail.path)).with(Style.DASHED).with(Color.BLUE))

                    );

                });
            }
        }

        try {
            Graphviz.fromGraph(ac).width(1500).render(Format.PNG).toFile(new File(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}










