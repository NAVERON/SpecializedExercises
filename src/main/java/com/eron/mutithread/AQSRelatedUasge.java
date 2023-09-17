package com.eron.mutithread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AQSRelatedUasge {

    private static final Logger log = LoggerFactory.getLogger(AQSRelatedUasge.class);

    public static void handler(Integer count) throws InterruptedException {
        Thread.sleep(1000);
        log.info("execute running task ... {}", count);
        Thread.sleep(1000);
    }

    public static void main(String[] args) {
        // 测试使用semaphore 信号量 基于AQS 共享锁 

        ExecutorService service = Executors.newFixedThreadPool(30);
        Semaphore semaphore = new Semaphore(5);
        Integer threadNum = 50;

        for (int i = 0; i < threadNum; i++) {
            final int num = 1;
            service.submit(() -> {

                try {
                    semaphore.acquire();
                    log.info("提交任务");
                    handler(num);
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
        }

        service.shutdown();
        if (semaphore.hasQueuedThreads()) {
            log.info("semaphore.hasQueuedThreads ---");
        }
        log.info("final");

    }

}










