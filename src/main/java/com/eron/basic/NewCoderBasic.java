package com.eron.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class NewCoderBasic {


    public static void main(String[] args) {
        G();
    }
    
    
    // 连续输入多个数组 
    public static void A() {
        try (Scanner scanner = new Scanner(System.in)) {
            while(scanner.hasNext()) {
                int a = scanner.nextInt();
                int b = scanner.nextInt();
                System.out.println(a + b);
            }
        }
    }
    
    public static void B() {
        Scanner scanner = new Scanner(System.in);
        int round = scanner.nextInt();
        
        for(int i = 0; i < round; i++) {
            int a = scanner.nextInt();
            int b = scanner.nextInt();
            System.out.println(a + b);
        }
        
    }
    
    public static void C() {
        try (Scanner scanner = new Scanner(System.in)) {
            while(scanner.hasNext()) {
                int a = scanner.nextInt();
                int b = scanner.nextInt();
                if(a == 0 && b == 0) break;
                System.out.println(a + b);
            }
        }
    }
    
    public static void D() {
        Scanner scanner = new Scanner(System.in);
        
        int num = scanner.nextInt();
        System.out.println(num);
        int k = 0;
        while(k < num && scanner.hasNext() ) {
            k++;
            int count = scanner.nextInt();
            // if(count == 0) break;
            
            int sum = 0;
            for(int i = 0; i < count; i++) {
                sum += scanner.nextInt();
            }
            System.out.println(sum);
        }
    }
    
    public static void E() {
        
        Scanner scanner = new Scanner(System.in);
        
        while(scanner.hasNext()) {
            int count = scanner.nextInt();
            
            int sum = 0;
            for(int i = 0; i < count; i++) {
                sum += scanner.nextInt();
            }
            System.out.println(sum);
        }
        
    }
    
    public static void F() {
        try (Scanner scanner = new Scanner(System.in)) {
            
            while(scanner.hasNextLine()) {
                String curLine = scanner.nextLine();
                List<Integer> res = Arrays.asList(curLine.split("\\s+")).stream()
                                                        .map(Integer::parseInt)
                                                        .collect(Collectors.toList());
                int sum = res.stream().reduce(Integer::sum).orElse(0);
                System.out.println(sum);
            }
        }
    }
    
    public static void G() {
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        scanner.nextLine();
        
        if(scanner.hasNextLine()) {
            List<String> res = Arrays.asList(scanner.nextLine().split("\\s+")).stream()
                    .limit(num)
                    .collect(Collectors.toList());
            
            System.out.println(String.join(",", res));
        }
        
    }
    
    

    public static void X() {
        Scanner scanner = new Scanner(System.in);
        String s1 = scanner.nextLine();
        String s2 = scanner.nextLine();
        
        int s2Count = getStringCount(s2);
        List<String> splitedList = getValidString(s1);
        System.out.println("当前分割结果 --> " + splitedList);
        System.out.println("最大输出数量 --> " + s2Count);
        
        String result = calMaxCount(splitedList, s2Count);
        System.out.println(result);
    }
    
    // 把字符串分割成有效的字符串  0-9  a-f 无效字符,作为分割标记
    public static List<String> getValidString(String s1){
        List<String> res = new LinkedList<>();
        String cur = "";
        for(char c : s1.toCharArray()) {
            if( (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f') ) {
                if(!cur.isEmpty()) {
                    res.add(cur);
                    cur = "";
                }
                continue;
            }
            cur += String.valueOf(c);
        }
        if(!cur.isEmpty()) {
            res.add(cur);
        }
        
        return res;
    }
    
    
    // 分割后的有效字符串  不同字符的最大长度 
    public static String calMaxCount(List<String> lists, int count) {
        String maxStr = "";
        for(String s : lists) {
            int curCount = getStringCount(s);
            maxStr = curCount <= count && curCount >= maxStr.length() ? s : maxStr;
        }
        
        return maxStr;
    }
    
    // 获取字符串不同字母数量 
    public static int getStringCount(String str) {
        if(str == null) return 0;
        
        Set<Character> set = new HashSet<Character>();
        for(char c : str.toCharArray()) {
            set.add(c);
        }
        
        return set.size();
    }
    
    
    
    
    
    
    
    public static void Y() {
        Scanner scanner = new Scanner(System.in);
        int goodsCount = Integer.parseInt(scanner.nextLine());
        int daysOfSell = Integer.parseInt(scanner.nextLine());
        
        // scanner.nextLine();
        // 商品 最大数量
        List<Integer> goodsCapcity = Arrays.asList(scanner.nextLine().split("\\s+"))
                                            .stream()
                                            .map(Integer::parseInt)
                                            .limit(goodsCount)  // 限制商品数量 
                                            .collect(Collectors.toList());
        List<List<Integer>> pricesOfDay = new ArrayList<>();  // 第i天 每个商品的价格价格
        for(int i = 0; i < daysOfSell; i++) {
            List<Integer> prices = Arrays.asList(scanner.nextLine().split("\\s+"))
                    .stream()
                    .map(Integer::parseInt)
                    .limit(goodsCount)  // 限制商品数量 
                    .collect(Collectors.toList());
            pricesOfDay.add(prices);
        }
        
        // 输入数据处理完成 
        System.out.println("商品数量 --> " + goodsCount);
        System.out.println("天数 --> " + daysOfSell);
        System.out.println("商品 可用于库存 --> " + goodsCapcity);
        System.out.println("商品每天的价格 --> " + pricesOfDay);
        
        // 获取 可以获取的最大利润  就是每件商品 可以获取的最大利润 
        List<Integer> maxCanGoods = maxCan(pricesOfDay);
        int sum = 0;
        for(int i = 0; i < goodsCount; i++) {
            sum += goodsCapcity.get(i) * maxCanGoods.get(i);
        }
        
        System.out.println("最大获利" + sum);
    }
    
    // 每件商品 可以在n 天获取到的 最大利润
    public static List<Integer> maxCan(List<List<Integer>> pricesOfDay) {
        // 保存每件商品可以在这几天内获取到的最大利润   如果不能买入  设置 0 
        List<Integer> res = new ArrayList<>();
        
        pricesOfDay.stream().forEach(oneGoodsPrices -> {
            if(oneGoodsPrices.size() <= 1) {
                res.add(0);
                return;
            }
            // 一个商品一段时间的价格变化 
            int sum = 0;
            for(int i = 1; i < oneGoodsPrices.size(); i++) {
                int preDay = oneGoodsPrices.get(i - 1);
                int curDay = oneGoodsPrices.get(i);
                if(curDay > preDay) {
                    sum += curDay - preDay;
                }
            }
            res.add(sum);
        });
        
        return res;
    }
    
    
    
    
    public static void Z() {
        Scanner scanner = new Scanner(System.in);
        int length = Integer.parseInt(scanner.nextLine());  // 供货商数量 
        
        List<Integer> goods = Arrays.asList(scanner.nextLine().split("\\s+"))
                .stream()
                .map(Integer::parseInt)
                .limit(length)
                .collect(Collectors.toList());
        
        List<Integer> types = Arrays.asList(scanner.nextLine().split("\\s+"))
                .stream()
                .map(Integer::parseInt)
                .limit(length)
                .collect(Collectors.toList());
        
        int K = Integer.parseInt(scanner.nextLine());
        
        System.out.println("供货商数量 --> " + length);
        System.out.println("货物数量 --> " + goods);
        System.out.println("对应货物种类 --> " + types);
        System.out.println("每种类 车 数量 --> " + K);
        
        // 干货湿货 分开 
        List<Integer> goodsOfDry = new ArrayList<>();
        List<Integer> goodsOfWet = new ArrayList<>();
        for(int i = 0; i < goods.size(); i++) {
            int thing = goods.get(i);
            if(types.get(i) == 0) {
                // 干货 
                goodsOfDry.add(thing);
            } else {
                goodsOfWet.add(thing);
            }
        }
        
        System.out.println("分开后的情况  --> 干 : " + goodsOfDry + "\n湿的 : " + goodsOfWet );
        // 干 湿 2 中的较大值   按照题意,  需要一次全部拉完 
        int dryCapcity = maxCapcity(goodsOfDry, K);
        int wetCapcity = maxCapcity(goodsOfWet, K);
        int res = Math.max(dryCapcity, wetCapcity);
        System.out.println(res);
        
        int avg = 59 / 2;
        if(59 % 2 > 0) {
            avg += 1;
        }
        System.out.println(avg);
    }
    
    
    // 货物 和可以使用的车 
    // 一次拉完, 需要车每次最少可以拉多少 
    public static int maxCapcity(List<Integer> goods, int K) {
        if(goods.isEmpty()) return 0;
        if( K == 0) return Integer.MAX_VALUE; // 如果没有车 整个没有意义
        
        Collections.sort(goods);  // 升序 
        if(K >= goods.size()) {  // 车的数量 大
            // List<Integer> tmp = goods.stream().sorted().collect(Collectors.toList());
            // Collections.reverse(goods);
            return goods.get(goods.size() - 1);
        }
        
        // 如果车的数量不够 
        int sum = 0;
        for(int num : goods) {
            sum += num;
        }
        
        // 平均承载 

        int avg = sum % 2 == 0 ? sum / K : (sum + 1)/K;
        if(sum % K > 0) {
            avg += 1;
        }
        int res = 0;
        Collections.reverse(goods);
        for(Integer x : goods) {  // 除了第一个最大,  其余应该 递增遍历  ==============================
            // 这里的逻辑需要修改, 单车拉取的量应当 ceil(avg) 
            // 最大值 如果 < avg, 按照最小添加的方式逐步加上去, 直到 > avg 
            res += x;
            if(res >= avg) {
                break;
            }
        }
        
        return res;
    }
    
    
    
}











