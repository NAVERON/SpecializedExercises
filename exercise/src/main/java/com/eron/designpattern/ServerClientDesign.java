package com.eron.designpattern;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * 源代码从FxTutorial 项目中复制过来 提供学习 
 * @author AlmasB
 * 创建简单的聊天功能
 */
public class ServerClientDesign extends Application {

    
    private static class DataPacket implements Serializable {

        private byte[] rawBytes;

        public DataPacket(byte[] rawBytes) {
            this.rawBytes = rawBytes;
        }

        public byte[] getRawBytes() {
            return rawBytes;
        }
    }
    
    private static class Encryptor {

        public byte[] enc(byte[] input) {
            byte[] output = new byte[input.length];
            for (int i = 0; i < input.length; i++) {
                output[i] = (byte) (input[i] + 1);
            }

            return output;
        }

        public byte[] dec(byte[] input) {
            byte[] output = new byte[input.length];
            for (int i = 0; i < input.length; i++) {
                output[i] = (byte) (input[i] - 1);
            }

            return output;
        }
    }
    
    private static abstract class NetworkConnection {

        private ConnectionThread connThread = new ConnectionThread();
        private Consumer<Serializable> onReceiveCallback;

        public NetworkConnection(Consumer<Serializable> onReceiveCallback) {
            this.onReceiveCallback = onReceiveCallback;
            connThread.setDaemon(true);
        }

        public void startConnection() throws Exception {
            connThread.start();
        }

        public void send(Serializable data) throws Exception {
            connThread.out.writeObject(data);
        }

        public void closeConnection() throws Exception {
            connThread.socket.close();
        }

        protected abstract boolean isServer();
        protected abstract String getIP();
        protected abstract int getPort();

        private class ConnectionThread extends Thread {
            private Socket socket;
            private ObjectOutputStream out;

            // TCP - slow, reliable
            // UDP - fast, unreliable

            @Override
            public void run() {
                try (ServerSocket server = isServer() ? new ServerSocket(getPort()) : null;
                        Socket socket = isServer() ? server.accept() : new Socket(getIP(), getPort());
                        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                    this.socket = socket;
                    this.out = out;
                    socket.setTcpNoDelay(true);

                    while (true) {
                        Serializable data = (Serializable) in.readObject();
                        onReceiveCallback.accept(data);
                    }
                }
                catch (Exception e) {
                    DataPacket closePacket = new DataPacket("Connection closed".getBytes());
                    onReceiveCallback.accept(closePacket);
                }
            }
        }
    }

    private static class Server extends NetworkConnection {

        private int port;

        public Server(int port, Consumer<Serializable> onReceiveCallback) {
            super(onReceiveCallback);
            this.port = port;
        }

        @Override
        protected boolean isServer() {
            return true;
        }

        @Override
        protected String getIP() {
            return null;
        }

        @Override
        protected int getPort() {
            return port;
        }
    }
    
    private static class Client extends NetworkConnection {

        private String ip;
        private int port;

        public Client(String ip, int port, Consumer<Serializable> onReceiveCallback) {
            super(onReceiveCallback);
            this.ip = ip;
            this.port = port;
        }

        @Override
        protected boolean isServer() {
            return false;
        }

        @Override
        protected String getIP() {
            return ip;
        }

        @Override
        protected int getPort() {
            return port;
        }
    }
    
    private boolean isServer = true;
    private TextArea messages = new TextArea();
    private NetworkConnection connection = isServer ? createServer() : createClient();
    
    private Parent createContent() {
        messages.setFont(Font.font(36));
        messages.setPrefHeight(550);
        messages.setEditable(false);

        TextField input = new TextField();
        input.setOnAction(event -> {
            String message = isServer ? "Server: " : "Client: ";
            message += input.getText();
            input.clear();

            messages.appendText(message + "\n");

            DataPacket packet = new DataPacket(
                    new Encryptor().enc(message.getBytes())
            );

            try {
                connection.send(packet);
            }
            catch (Exception e) {
                messages.appendText("Failed to send\n");
            }
        });

        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600, 600);
        return root;
    }

    @Override
    public void init() throws Exception {
        connection.startConnection();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        connection.closeConnection();
    }

    private Server createServer() {
        return new Server(55555, data -> {
            DataPacket packet = (DataPacket) data;
            byte[] original = new Encryptor().dec(packet.getRawBytes());

            Platform.runLater(() -> {
                messages.appendText(new String(original) + "\n");
            });
        });
    }

    private Client createClient() {
        return new Client("127.0.0.1", 55555, data -> {
            DataPacket packet = (DataPacket) data;
            byte[] original = new Encryptor().dec(packet.getRawBytes());

            Platform.runLater(() -> {
                messages.appendText(new String(original) + "\n");
            });
        });
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
    public static void chatStarter(String[] args) {
        Application.launch(args);
    }
    
    public static class Launcher {
        public static void main(String[] args) {
            ServerClientDesign.chatStarter(args);
        }
    }
    
}










