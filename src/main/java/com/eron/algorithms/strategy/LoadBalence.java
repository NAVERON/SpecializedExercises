package com.eron.algorithms.strategy;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 负载均衡算法 
 * 
 * @author eron 
 * 随机算法 
 * hash槽算法 
 * 一致性hash算法 
 */
public class LoadBalence {
    private static final Logger log = LoggerFactory.getLogger(LoadBalence.class);

    public static void main(String[] args) {
        List<ServerResource> servers = new ArrayList<>() {{
            add(new ServerResource("1", "192.168.101.1", "8888", 2));
            add(new ServerResource("2", "192.168.101.2", "8888", 1));
            add(new ServerResource("3", "192.168.101.3", "8888", 1));
            add(new ServerResource("4", "192.168.101.4", "8888", 1));
            add(new ServerResource("5", "192.168.101.5", "8888", 3));
        }};
        
        // 实验一致性hash算法 
        ConstentHashing constentHashing = new ConstentHashing(servers);
        log.info("hash 一致性算法 --> {}, {}, {}", 
                constentHashing.select("name"), constentHashing.select("xxxx"), constentHashing.select("test"));
        // 加权随机/随机算法 
        RandomRound randomRound = new RandomRound(servers);
        log.info("测试循环随机 负载均衡 --> {}, {}, {}", 
                randomRound.select(), randomRound.select(), randomRound.select());
        WeightedRound oneByOneWithWeight = new WeightedRound(servers);
        log.info("加权重的轮询 负载均衡 --> {}, {}, {}", 
                oneByOneWithWeight.select(), oneByOneWithWeight.select(), oneByOneWithWeight.select());
    }
    // 定义服务资源 
    private static class ServerResource {
        // 定义服务资源 
        String serverName;  // 服务名称 
        String serverIP;  // 服务IP 
        String serverPort;  // 端口号 
        Integer serverWeight;  // 权重 
        public ServerResource(String name, String ip, String port, Integer weight) {
            this.serverName = name;
            this.serverIP = ip;
            this.serverPort = port;
            this.serverWeight = weight;
        }
        public List<ServerResource> transferToMuti(){  // 根据权重转换数量 
            List<ServerResource> dupServers = new LinkedList<>();
            for(int i = 0; i < this.serverWeight; i++) {
                dupServers.add(this);
            }
            return dupServers;
        }
        @Override
        public String toString() {
            return "ServerResource [serverName=" + serverName + ", serverIP=" + serverIP + ", serverPort=" + serverPort
                    + ", serverWeight=" + serverWeight + "]";
        }
    }
    // 一致性hash算法  可以保证同一个请求落在同一个服务器上 hash计算是一样的 = 一致性 
    // dubbo 的实现 参考, 从guide-rpc-framework 摘录的
    private static class ConstentHashing {
        private List<ServerResource> servers = Collections.emptyList();  // 可供选择的server列表 
        private TreeMap<Long, ServerResource> cache = new TreeMap<>();  // 保存服务资源hash对应值
        int replicaNum = 5;  // 虚拟出服务节点数量 
        public ConstentHashing(List<ServerResource> servers) {
            this.servers = servers;
            this.servers.forEach(server -> {
                for(int i = 0; i < this.replicaNum / 4; i++) {
                    byte[] digest = this.md5(server.toString() + i);
                    for(int j = 0; j < 4; j++) {
                        long x = this.hash(digest, j);
                        cache.put(x, server);
                        log.info("设定节点位置 --> {}, {}", x, server.toString());
                    }
                }
            });
        }
        public String select(String serviceKey) {
            byte[] digest = this.md5(serviceKey);
            long hashCode = this.hash(digest, 0);
            Entry<Long, ServerResource> entry = cache.tailMap(hashCode, true).firstEntry();
            if(entry == null) {
                entry = cache.firstEntry();
            }
            ServerResource selected = entry.getValue();
            return selected.toString();
        }
        private byte[] md5(String key) {
            MessageDigest md = null;
            try {
                md = MessageDigest.getInstance("MD5");
                byte[] bytes = key.getBytes(StandardCharsets.UTF_8);
                md.update(bytes);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return md.digest();
        }
        private long hash(byte[] digest, int number) {
            return (((long) (digest[3 + number * 4] & 0xFF) << 24)
                    | ((long) (digest[2 + number * 4] & 0xFF) << 16)
                    | ((long) (digest[1 + number * 4] & 0xFF) << 8)
                    | (digest[number * 4] & 0xFF))
                    & 0xFFFFFFFFL;
        }
    }
    // 随机/轮询算法 思路类似 
    private static class RandomRound {
        private List<ServerResource> servers;
        private AtomicInteger counter = new AtomicInteger(0);  // 或者使用reentrantLock 前后加锁 线程竞争小的情况下CAS方便 
        public RandomRound(List<ServerResource> servers) {
            this.servers = servers;
            Collections.shuffle(this.servers);  // 打乱顺序 
        }
        public String select() {  // 按照顺序依次选择  或者计算随机数 
            ServerResource resource = servers.get(this.counter.getAndIncrement());
            return resource.toString();
        }
    }
    // 加权轮询 
    private static class WeightedRound extends RandomRound {
        public WeightedRound(List<ServerResource> servers) {
            super(
                servers.stream().map(server -> server.transferToMuti())
                    .flatMap(List::stream).collect(Collectors.toList())
            );
            log.info("----{}", 
                servers.stream().map(server -> server.transferToMuti())
                .flatMap(List::stream).collect(Collectors.toList())
            );
        }
    }
}










