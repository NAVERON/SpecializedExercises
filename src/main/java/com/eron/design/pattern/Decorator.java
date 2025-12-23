package com.eron.design.pattern;

import javax.xml.crypto.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 装饰器模式
 */
public class Decorator {
    // 类似 多层的栈封装结构 a -> b -> c
    private static final Logger LOGGER = LoggerFactory.getLogger(Decorator.class);

    // 压缩和解压缩 装饰器
    public interface Datasource {
        String readData();
        void writeData(String data);
    }

    // 假设原本的实现只有原文本直接储存, 现在需要增加加密和压缩功能, 采用装饰器模式
    public static class FileDataSource implements Datasource {
        @Override
        public String readData() {
            LOGGER.info("文件读取数据");
            return "< hello world >";
        }
        @Override
        public void writeData(String data) {
            LOGGER.info("文件保存/写入数据");
        }
    }

    // 需要和原本的实现类 相同的接口，创建一个公共的抽象 其他在其基础上具体实现, 有点像工厂方法的模板策略
    public static class DataSourceWrapper implements Datasource { // 装饰 抽象基础类
        private Datasource wrapper;
        public DataSourceWrapper(Datasource wrapper) {
            this.wrapper = wrapper;
        }

        @Override
        public String readData() {
            return wrapper.readData();
        }
        @Override
        public void writeData(String data) {
            wrapper.writeData(data);
        }
    }

    // 在基础抽象上实现 可以栈结构 装饰器多种实现
    public static class EncryptionDataSourceDecorator extends DataSourceWrapper {
        // 设置一些加密密钥等配置
        public EncryptionDataSourceDecorator(Datasource wrapper) {
            super(wrapper);
        }

        @Override
        public String readData() {
            return this.decrypt(super.readData());
        }
        @Override
        public void writeData(String data) {
            super.writeData(this.encrypt(data));
        }

        // 加密解密相关的方法
        public String encrypt(String data) {
            LOGGER.info("加密... {}", data);
            return data;
        }
        public String decrypt(String data) {
            LOGGER.info("解密... {}", data);
            return  data;
        }
    }

    public static class CompressionDataSourceDecorator extends DataSourceWrapper {
        // 配置压缩等级 压缩算法等配置...
        public CompressionDataSourceDecorator(Datasource wrapper) {
            super(wrapper);
        }

        @Override
        public String readData() {
            return this.unzip(super.readData());
        }
        @Override
        public void writeData(String data) {
            super.writeData(this.compression(data));
        }

        // 压缩 还原 其他基础方法
        public String compression(String data) {
            LOGGER.info("压缩... {}", data);
            return data;
        }

        public String unzip(String data) {
            LOGGER.info("解压缩... {}", data);
            return data;
        }
    }

}
