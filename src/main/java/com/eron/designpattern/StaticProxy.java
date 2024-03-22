
package com.eron.designpattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 静态代理的实现， 对应的由动态代理的实现
 *
 * @author ERON_AMD
 */
public class StaticProxy {

    public static void main(String[] args) {
        Worker myWorker = new WorkerImpl();
        MyProxy proxy = new MyProxy(myWorker);
        proxy.doWork("", "");
    }

}

interface Worker {

    public void doWork(String name, String password);
}

class WorkerImpl implements Worker {

    private final Logger log = LoggerFactory.getLogger(WorkerImpl.class);

    @Override
    public void doWork(String nameString, String password) {
        log.info("my worker implements");
        log.info("params {}, pwd : {}", nameString, password);
    }

}

class MyProxy implements Worker { // 代理类可以在之前的方法执行前后加强处理

    private Worker worker;
    private final Logger log = LoggerFactory.getLogger(MyProxy.class);

    public MyProxy(Worker worker) {
        this.worker = worker;
    }

    @Override
    public void doWork(String nameString, String password) {
        log.info("this is proxy call inner worker running...");
        this.doBefore();
        this.worker.doWork(nameString, password);
        this.doAfter();
    }

    private void doBefore() {
        System.out.println("do work befor do something");
    }

    private void doAfter() {
        System.out.println("do work after do something");
    }
}










