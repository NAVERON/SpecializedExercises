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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


// 主从Reactor模型
public class MultiReactorServer {

    public static void main(String[] args) {
        // 单元测试 
        Thread reactor = new Thread(new Reactor(9191));
        reactor.start();
    }

    public static Reactor createMultiReactor(Integer port) {
        return new Reactor(port);
    }

    // 主体部分同 singleReactor模型部分 
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
                selectionKey.attach(new Acceptor(serverSocketChannel));  // 绑定事件要做的事

                log.info("MultiReactor 服务端reactor初始化完成");
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

    // 只负责接收连接请求 请求的读写处理交给 subreactor
    private static class Acceptor implements Runnable {

        private static final Logger log = LoggerFactory.getLogger(Acceptor.class);

        private ServerSocketChannel serverSocketChannel;
        private SubReactor subReactor;
        private Selector selector;
        private Thread subReactorThread;

        public Acceptor(ServerSocketChannel serverSocketChannel) {
            this.serverSocketChannel = serverSocketChannel;

            try {
                this.selector = Selector.open();

                subReactor = new SubReactor(this.selector);
                subReactorThread = new Thread(subReactor);
                subReactorThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void run() {

            try {
                SocketChannel socketChannel = this.serverSocketChannel.accept();
                socketChannel.configureBlocking(false);
                log.info("监听到客户端连接事件 => {}", socketChannel.getRemoteAddress().toString());

                selector.wakeup();  // ???  Causes the first selection operation that has not yet returned to returnimmediately. 
                SelectionKey selectionKey = socketChannel.register(this.selector, SelectionKey.OP_READ);
                selectionKey.attach(new WorkerHandler(socketChannel));  // 注册可以读事件 

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private static class SubReactor implements Runnable {

        private static final Logger log = LoggerFactory.getLogger(SubReactor.class);

        private Selector selector;

        public SubReactor(Selector selector) {  // selector 在acceptor中创建的, 监视 可读取事件 
            this.selector = selector;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    selector.select();
                    log.info("cur Thread ==> {}, selector ==> {}", Thread.currentThread().getName(), selector.toString());

                    Set<SelectionKey> selectionKeys = selector.selectedKeys();
                    Iterator<SelectionKey> iter = selectionKeys.iterator();
                    while (iter.hasNext()) {
                        SelectionKey key = iter.next();
                        dispatcher(key);  // 启动
                        iter.remove();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void dispatcher(SelectionKey selectionKey) {
            Runnable executor = (Runnable) selectionKey.attachment();
            executor.run();
        }
    }

    private static class WorkerHandler implements Runnable {

        private static final Logger log = LoggerFactory.getLogger(WorkerHandler.class);

        private SocketChannel socketChannel;

        public WorkerHandler(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
        }

        @Override
        public void run() {
            ByteBuffer buffer = ByteBuffer.allocate(512);
            try {
                this.socketChannel.read(buffer);
                String message = new String(buffer.array(), StandardCharsets.UTF_8);
                log.info("收到客户端消息 => {}", message);

                // 业务处理
                ByteBuffer retry = ByteBuffer.wrap(new String("服务端回复 ->" + message).getBytes(StandardCharsets.UTF_8));
                this.socketChannel.write(retry);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}









