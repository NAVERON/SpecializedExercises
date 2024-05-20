package com.eron.design.pattern;

import javax.swing.text.StyledEditorKit.BoldAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 桥接模式
 */
public class Bridge {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bridge.class);

    public interface Remote {
        Device getDevice();
        Device resetDevice(Device device);
        void togglePower();
        void channelUp();
        void volumeUp();
    }

    // 控制层抽象
    public static class RemoteController implements Remote {
        private Device device;

        public RemoteController(Device device) { // 可以设置默认设备
            this.device = device;
        }

        @Override
        public Device getDevice() {
            return device;
        }
        @Override
        public Device resetDevice(Device device) { // 重设控制设备
            this.device = device;
            return device;
        }

        @Override
        public void togglePower() {
            if (device.isEnabled()) {
                device.disable();
            } else {
                device.enable();
            }
        }
        public void channelUp() {
            device.setChannel(device.getChannel() + 1);
        }
        public void channelDown() {
            device.setChannel(device.getChannel() - 1);
        }
        public void volumeUp() {
            device.setVolume(device.getVolume() + 1);
        }
        public void volumeDown() {
            // 越界问题暂时不考虑
            device.setVolume(device.getVolume() - 1);
        }
    }

    // 控制层 拓展 --> 可以继承， 也可以单独实现
    public static class AdvanceRemoteController extends RemoteController {
        public AdvanceRemoteController(Device device) {
            super(device);
        }

        public void mute() {
            getDevice().setVolume(0);
        }
    }

    // 设备抽象
    public interface Device {
        boolean isEnabled();
        void enable();
        void disable();
        int getVolume();
        void setVolume(int volume);
        int getChannel();
        void setChannel(int channel);
    }

    // 以下为设备的具体是现 这种实现有点类似于适配器模式的抽象实现, 但是目的不同
    public static class TV implements Device {
        private boolean status = false;
        private int volume = 0;
        private int channel = 0;

        @Override
        public boolean isEnabled() {
            return status;
        }
        @Override
        public void enable() {
            status = true;
        }
        @Override
        public void disable() {
            status = false;
        }
        @Override
        public int getVolume() {
            return volume;
        }
        @Override
        public void setVolume(int volume) {
            this.volume = volume;
        }
        @Override
        public int getChannel() {
            return channel;
        }
        @Override
        public void setChannel(int channel) {
            this.channel = channel;
        }

        @Override
        public String toString() {
            return "TV{" +
                "status=" + status +
                ", volume=" + volume +
                ", channel=" + channel +
                '}';
        }
    }

    public static class Radio implements Device {
        // 类似TV的实现， 实际中底层的调用实现是不同的，这里省略...
        @Override
        public boolean isEnabled() {
            return false;
        }
        @Override
        public void enable() {}
        @Override
        public void disable() {}
        @Override
        public int getVolume() {
            return 0;
        }
        @Override
        public void setVolume(int volume) {}
        @Override
        public int getChannel() {
            return 0;
        }
        @Override
        public void setChannel(int channel) {}

        @Override
        public String toString() {
            return "Radio{}";
        }
    }

}
