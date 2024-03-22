


package com.eron.designpattern;

import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 多线程发现者模式
 *
 * @author ERON_AMD
 */
public class MutiObserverTest {
    //多线程方法 synchronized    使用支持多线程的结构存储状态
    private static final Logger log = LoggerFactory.getLogger(MutiObserverTest.class);

    public interface Watched {
        public void addWatcher(Watcher watcher);

        public void removeWatcher(Watcher watcher);

        public void notifyAllWatcher();
    }

    public interface Watcher {
        public void takeAction();
    }

    public static class Enemy extends Thread implements Watched {

        Vector<Watcher> watchers = new Vector<Watcher>();

        @Override
        public void addWatcher(Watcher watcher) {
            watchers.add(watcher);
        }

        @Override
        public void removeWatcher(Watcher watcher) {
            watchers.remove(watcher);
        }

        @Override
        public void notifyAllWatcher() {
            for (Watcher watcher : watchers) {
                watcher.takeAction();
            }
        }

        @Override
        public void run() {
            super.run(); //To change body of generated methods, choose Tools | Templates.
            this.notifyAllWatcher();
        }


    }


    public static class Police implements Watcher {

        @Override
        public void takeAction() {
            log.info("police action");
        }

    }

    public static class Shooter implements Watcher {

        @Override
        public void takeAction() {
            log.info("shooter action");
        }

    }

    public static void main(String[] args) {
        // 观察者模式应当反被动为主动， shooter和police相当于是客户端， 一般都是观察者主动观察某一个被观察者， 而不是被观察者主动邀请观察者去监管
        Police police = new Police();
        Shooter shooter = new Shooter();

        Enemy enemy = new Enemy();
        enemy.addWatcher(police);
        enemy.addWatcher(shooter);

        enemy.start();
    }

}












