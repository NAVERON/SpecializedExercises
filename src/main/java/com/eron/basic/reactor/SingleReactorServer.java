package com.eron.basic.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// 单Reactor模型 
public class SingleReactorServer {

    private static final Logger log = LoggerFactory.getLogger(SingleReactorServer.class);

    public static void main(String[] args) {
        // 单元测试 
        Thread singleReactor = new Thread(new Reactor(9090));
        singleReactor.start();
    }

    public static Reactor createSingleReactorServer(Integer port) {  // 方便创建 
        return new Reactor(port);
    }

    private static class Reactor implements Runnable {
        private static final Logger log = LoggerFactory.getLogger(Reactor.class);

        private ServerSocketChannel serverSocketChannel;
        private Selector selector;

        public Reactor(Integer port) {
            try {
                serverSocketChannel = ServerSocketChannel.open();
                selector = Selector.open();

                serverSocketChannel.socket().bind(new InetSocketAddress(port));
                serverSocketChannel.configureBlocking(false);  // 阻塞模式 非阻塞模式 
                // selector 接收请求accept事件, 并绑定到 acceptor上
                SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
                selectionKey.attach(new Acceptor(selector, serverSocketChannel));  // 绑定事件要做的事

                log.info("服务端reactor初始化完成");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {

            while (true) {  // 不断从selector中查看是否有数据来
                try {
                    selector.select();
                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = selectionKeys.iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        dispatcher(key);
                        iter.remove(); //处理完链接事件 移除 
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void dispatcher(SelectionKey selectionKey) {
            Runnable executor = (Runnable) selectionKey.attachment();  // 获取的是Acceptor runnable  与attach相对应 每次获取的都是同一个
            executor.run();  // 直接调用 不会重新开启一个新的线程 
        }

    }

    // 处理连接事件触发后的动作
    private static class Acceptor implements Runnable {
        Selector selector;
        ServerSocketChannel serverSocketChannel;

        public Acceptor(Selector selector, ServerSocketChannel serverSocketChannel) {
            this.selector = selector;
            this.serverSocketChannel = serverSocketChannel;
        }

        @Override
        public void run() {
            try {
                SocketChannel socketChannel = serverSocketChannel.accept(); // selector 绑定了请求连接事件 有新的请求会执行这个方法 
                log.info("有client连接...{}", socketChannel.getRemoteAddress().toString());
                socketChannel.configureBlocking(false);
                SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
                selectionKey.attach(new WorkHandle(socketChannel));  // 绑定如果有数据可以读取的事件处理

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    // 处理读取到数据后的动作
    private static class WorkHandle implements Runnable {

        private ExecutorService executionPool = Executors.newFixedThreadPool(10);

        private SocketChannel socketChannel;

        public WorkHandle(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {

            try {
                ByteBuffer buffer = ByteBuffer.allocate(512);  // socket读取数据 到缓存buffer 
                socketChannel.read(buffer);

                executionPool.submit(new ProcessingItem(socketChannel, buffer));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    // 将处理收到的信息 单独进行处理
    private static class ProcessingItem implements Runnable {

        private SocketChannel socketChannel;
        private ByteBuffer buffer;

        public ProcessingItem(SocketChannel socketChannel, ByteBuffer buffer) {
            this.socketChannel = socketChannel;
            this.buffer = buffer;
        }

        @Override
        public void run() {

            try {
                String message = new String(buffer.array(), StandardCharsets.UTF_8);
                log.info("服务端收到={}=的信息 -> {}", socketChannel.getRemoteAddress(), message);

                // 服务端返回给客户端数据 
                ByteBuffer retry = ByteBuffer.wrap(new String("服务端返回数据->" + message).getBytes(StandardCharsets.UTF_8));
                socketChannel.write(retry);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}













