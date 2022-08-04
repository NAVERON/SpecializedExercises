package com.eron.algorithms.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 全排列问题的解决方法 
 * 比如给定一个nums 数字数组, 给出所有可能的排列情况 
 * 可以构建一颗类似的trie树, 列举所有的情况 
 * 或者 回溯法 
 * @author eron
 *
 */
public class FullyArrange {

    private static final Logger log = LoggerFactory.getLogger(FullyArrange.class);
    
    // 保存最终结果 
    private List<List<Integer>> res = new LinkedList<List<Integer>>();
    
    public static void main(String[] args) {
        // 用于测试 
        Integer[] nums = {1, 2, 3};
        FullyArrange fullyArrange = new FullyArrange();
        
        fullyArrange.trackback(new LinkedList<>(), new LinkedList<>(Arrays.asList(nums)));
        log.info("最终结果 ==> {}", fullyArrange.res.toString());
        
        // 另一道算法题 给出一个数字序列, 这个数字序列可以组成多少种二叉树情况 
        // 序列形成的二叉树情况与序列的内容无关  只与序列个数有关, 所以迭代计算即可
        fullyArrange.treeCount(10);
    }
    
    // 输入 待排序数组 输出:可以排序的所有情况 
    // 使用回溯法 
    public void trackback(List<Integer> inner, List<Integer> outer) {  // inner 已经存在的num outer可以放入的 
        if(outer.isEmpty()) {
            log.info("当前inner生成 --> {}", inner.toString());
            res.add(inner);
        }
        
        for(int i = 0; i < outer.size(); i++) {
            List<Integer> first = generateAdd(inner, outer.get(i));
            List<Integer> second = generateDel(outer, i);
            
            trackback(first, second);  // 生成了新的 不需要手动回溯 因为传入的是新生成的完整对象, 不是引用 
        }
    }
    
    // 以下动作简称为深拷贝  也可以使用数组换位的方式进行 当时需要传入索引位置, 思路比较麻烦 
    public List<Integer> generateDel(List<Integer> origin, Integer index){  // 移除 origin 中indx位置的
        List<Integer> x = new ArrayList<>(origin.size());
        for(int i = 0; i < origin.size(); i++) {
            if(index.equals(i)) {
                continue;
            }
            x.add(origin.get(i));
        }
        
        return x;
    }
    public List<Integer> generateAdd(List<Integer> origin, Integer n){
        List<Integer> x = new ArrayList<>();
        for(int i = 0; i < origin.size(); i++) {
            x.add(origin.get(i));
        }
        x.add(n);
        
        return x;
    }
    
    // 计算序列可组成二叉树的情况数 
    public void treeCount(int n) {
        Integer[] g = new Integer[n+1];
        for(int i = 0; i <= n; i++) {  // 初始化 不然会报空指针错误 
            g[i] = 0;
        }
        
        g[0] = 1;
        g[1] = 1;
        
        for(int i = 2; i <= n; i++) {
            for(int j = 1; j <= i; j++) {
                g[i] += g[j-1]*g[i-j];
            }
        }
        
        log.info("序列可组成的tree情况数 --> {}", Arrays.asList(g).toString());
    }
}










