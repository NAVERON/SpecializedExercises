package com.eron.design.pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 外观模式
 */
public class Facade {
    // 类似于 接口的实现, 对外提供简单的接口, 但内部实现可能会调用复杂的过程/库 对外抽象简单接口
    private static final Logger LOGGER = LoggerFactory.getLogger(Facade.class);

    // 假如提供指向复杂系统的接口 对外提供简单抽象的方法即可
    // 以下例子 提供一个复杂视频转换库的逻辑实现
    public static class VideoConversionFacade {
        // 对外展示的方法接口
        public String convertFile(String fileName, String fileFormat) {
            LOGGER.info("convert file");

            VideoFile videoFile = new VideoFile(fileName, fileFormat);
            Codec codec = CodecFactory.getByType(fileFormat);
            LOGGER.info("根据文件和格式解析文件 --> {}, {}", videoFile.name, codec);
            return "";
        }
    }

    public record VideoFile(String name, String type) {}

    public interface Codec {}

    public static class Mpeg4CompressionCodec implements Codec {
        public String type = "mp4";
        @Override
        public String toString() {
            return "Mpeg4CompressionCodec{" +
                "type='" + type + '\'' +
                '}';
        }
    }
    public static class OggCompressionCodec implements Codec {
        public String type = "ogg";
        @Override
        public String toString() {
            return "OggCompressionCodec{" +
                "type='" + type + '\'' +
                '}';
        }
    }

    public static class CodecFactory {
        public static Codec getByType(String type) {
            if ("mp4".equals(type)) {
                return new Mpeg4CompressionCodec();
            }

            return new OggCompressionCodec();
        }
    }

}


