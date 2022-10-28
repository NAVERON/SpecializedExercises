package com.eron.javafx.handtrack.tracking.impl;

import com.almasb.fxgl.logging.Logger;
import com.eron.javafx.handtrack.tracking.Hand;
import com.eron.javafx.handtrack.tracking.HandTrackingDriver;

import javafx.geometry.Point3D;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public final class JSHandTrackingDriver implements HandTrackingDriver {

    // 包装 mediapip 层  格式化数据为 Hand, 交给上层分析处理 
    // 上层传入 dataHandler 
    private Consumer<Hand> dataHandler;

    private MediaPipeServer server = new MediaPipeServer(
            new InetSocketAddress("localhost", 55555),
            this::onMessage
    );

    public JSHandTrackingDriver(Consumer<Hand> dataHandler) {
        this.dataHandler = dataHandler;
    }

    @Override
    public void start() {
        server.start();
    }

    @Override
    public void setOnHandData(Consumer<Hand> dataHandler) {
        this.dataHandler = dataHandler;
    }

    @Override
    public void stop() {
        try {
            server.stop();
        } catch (InterruptedException e) {
            Logger.get(JSHandTrackingDriver.class).warning("Failed to stop server.", e);
        }
    }

    private void onMessage(String message) {  // 字符串转换成 标准结构 id + points3D (x,y,z) 
        try {
            var rawData = message.split(",");

            int id = Integer.parseInt(rawData[0]);

            var points = new ArrayList<Point3D>();

            for (int i = 1; i < rawData.length; i += 3) {
                var x = Double.parseDouble(rawData[i + 0]);
                var y = Double.parseDouble(rawData[i + 1]);
                var z = Double.parseDouble(rawData[i + 2]);

                points.add(new Point3D(x, y, z));
            }

            dataHandler.accept(new Hand(id, points));
        } catch (Exception e) {
            Logger.get(JSHandTrackingDriver.class).warning("Failed to parse message.", e);
        }
    }
}





