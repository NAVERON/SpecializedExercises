package com.eron.design.pattern;

/**
 * 对象创建 生成器
 */
public class Builder {

    public interface MyBuilder {
        void setCarType(CarType type);
        void setCarSeats(int seats);
        void setCarGPS(GPSClass gps);
        void setCarEngine(CarEngine engine);
    }
    public enum CarType {
        SUV, SPORT, OTHER
    }
    public enum GPSClass {
        GPS, BD
    }
    public enum CarEngine {
        BWM, BYD, XIAO_MI, CHANG_CHENG
    }
    // builder 同类型 需要参数一致
    public record Car(CarType type, int seats, GPSClass gps, CarEngine engine) {
        @Override
        public String toString() {
            return "Car{" +
                "type=" + type +
                ", seats=" + seats +
                ", gps=" + gps +
                ", engine=" + engine +
                '}';
        }
    }
    public record Thing(CarType type, int seats, GPSClass gps, CarEngine engine) {
        @Override
        public String toString() {
            return "Thing{" +
                "type=" + type +
                ", seats=" + seats +
                ", gps=" + gps +
                ", engine=" + engine +
                '}';
        }
    }

    // 创建内容相同的builder
    public static class CarBuilder implements MyBuilder {
        private CarType type;
        private int seats;
        private GPSClass gps;
        private CarEngine engine;

        @Override
        public void setCarType(CarType type) {
            this.type = type;
        }
        @Override
        public void setCarSeats(int seats) {
            this.seats = seats;
        }
        @Override
        public void setCarGPS(GPSClass gps) {
            this.gps = gps;
        }
        @Override
        public void setCarEngine(CarEngine engine) {
            this.engine = engine;
        }
        public Car build() {
            return new Car(this.type, this.seats, this.gps, this.engine);
        }
    }

    public static class ThingBuilder implements MyBuilder {
        private CarType type;
        private int seats;
        private GPSClass gps;
        private CarEngine engine;

        @Override
        public void setCarType(CarType type) {
            this.type = type;
        }
        @Override
        public void setCarSeats(int seats) {
            this.seats = seats;
        }
        @Override
        public void setCarGPS(GPSClass gps) {
            this.gps = gps;
        }
        @Override
        public void setCarEngine(CarEngine engine) {
            this.engine = engine;
        }
        public Thing build() {
            return new Thing(this.type, this.seats, this.gps, this.engine);
        }
    }

    public static class Director { // 实际中 一个主管可以初始化多种builder对象
        public void constructSportCar(MyBuilder builder) {
            builder.setCarType(CarType.SPORT);
            builder.setCarSeats(4);
            builder.setCarGPS(GPSClass.GPS);
            builder.setCarEngine(CarEngine.XIAO_MI);
        }

        public void constructSUV(MyBuilder builder) {
            builder.setCarType(CarType.SUV);
            builder.setCarSeats(8);
            builder.setCarGPS(GPSClass.BD);
            builder.setCarEngine(CarEngine.BYD);
        }
    }
}