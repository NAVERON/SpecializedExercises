package com.eron.basic.reactor;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.checkerframework.common.reflection.qual.NewInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// socket 客户端 
public class ReactorClient implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(ReactorClient.class);

    public static void main(String[] args) throws IOException {
        // 单元测试 
        ReactorClient client = new ReactorClient(9090);
        Thread x = new Thread(client);
        x.start();
    }

    private Socket socket = null;
    private Integer timeout = 5000; // 链接超时

    public ReactorClient(Integer port) {
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress("localhost", port));
        } catch (IOException e1) {
            e1.printStackTrace();
            log.info("socket 链接失败, 请先启动服务");
            return;
        }

        Thread receiverThread = new Thread(() -> {
            if (!socket.isConnected()) {
                log.info("socket connection NULL, please start a SocketServer first !");
                return;
            }

            while (true) {
                try {
                    InputStream inputStream = socket.getInputStream();
                    byte[] buffer = new byte[1024];  
                    inputStream.read(buffer);

                    log.info("Client 接收到的 -> {}", new String(buffer, StandardCharsets.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        receiverThread.start();
    }

    @Override
    public void run() {
        while (true) {
            try (Scanner scanner = new Scanner(System.in)) {
                while (scanner.hasNext()) {
                    String s = scanner.nextLine();
                    log.info("客户端输入==>{}", s);
                    socket.getOutputStream().write(s.getBytes());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
