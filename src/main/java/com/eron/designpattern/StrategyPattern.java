package com.eron.designpattern;

/**
 * 策略模式 策略的多个实现, 在实际使用中传入不同的实现
 *
 * @author eron
 */
public class StrategyPattern {

    public interface Strategy {
        public int doOption(int x, int y);
    }

    public static class AddStrategy implements Strategy {

        @Override
        public int doOption(int x, int y) {
            return x + y;
        }
    }

    public static class MutiplyStrategy implements Strategy {

        @Override
        public int doOption(int x, int y) {
            return x * y;
        }
    }

    public static class Context {

        private Strategy strategy;

        public Context(Strategy strategy) {
            this.strategy = strategy;
        }

        public int executeStrategy(int x, int y) {
            return this.strategy.doOption(x, y);
        }

        public void setStrategy(Strategy strategy) {
            this.strategy = strategy;
        }
    }

    public static void main(String[] args) {
        Context c = new Context(new AddStrategy());
        int addResult = c.executeStrategy(2, 4);
        System.out.println(addResult);

        c.setStrategy(new MutiplyStrategy()); // 可以随意的更换策略
        int mutiplyResult = c.executeStrategy(2, 4);
        System.out.println(mutiplyResult);
    }
}
