package com.eron.mutithread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * countDownlatch 像是倒时计数器, 但是是计算任务数量的
 * 比如有5个任务，需要5个都完成后才能进行下面的，这种使用countdown
 * @author ERON_AMD
 */

public class CountDownLatchTest {  // CyclicBarrier 与之对应， 加数计数
	// CyclicBarrier 让一组线程到达一个同步点后再一起继续运行，在其中任意一个线程未达到同步点，其他到达的线程均会被阻塞
	
	private static final Logger log = LoggerFactory.getLogger(CountDownLatchTest.class);
    
    public static void main(String[] args){
        
        final CountDownLatch countdownlatch = new CountDownLatch(2);  // 设定有两个任务需要完成 
        
        ExecutorService es1 = Executors.newSingleThreadExecutor();
        es1.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("第一个线程执行中，停顿一秒~");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    log.error("Error : {}", ex);
                }
                
                log.info("第一个执行完毕");
                countdownlatch.countDown();
            }
        });
        
        ExecutorService es2 = Executors.newSingleThreadExecutor();
        es2.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("第二个线程执行, 停顿2秒~~~");
                
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {
                    log.error("{}", ex);
                }
                
                log.info("第二个执行完毕");
                countdownlatch.countDown();
            }
        });
        
        es1.shutdown();
        es2.shutdown();
        
        try {
            System.out.println("人没到齐，先等着");
            countdownlatch.await();
            System.out.println("人齐了，await等待了所有的线程均执行完毕~~");
        } catch (InterruptedException ex) {
            log.error("{}", ex);
        }
        System.out.println("两个线程均执行完毕~");
        
        log.info("开始测试cyclicBarrier 的使用"); 
        
        CountDownLatchTest.cyclicBarrierTest();
    }
    
    /**
     * 实验 CyclicBarrier 
     * @author eron
     *
     */
    static class TaskThread extends Thread {
        
        CyclicBarrier barrier;
        
        public TaskThread(CyclicBarrier barrier) {
            this.barrier = barrier;
        }
        
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(getName() + " 到达栅栏 A");
                barrier.await();
                System.out.println(getName() + " 冲破栅栏 A");
                
                Thread.sleep(2000);
                System.out.println(getName() + " 到达栅栏 B");
                barrier.await();
                System.out.println(getName() + " 冲破栅栏 B");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void cyclicBarrierTest() {
    	// cyclicBarrier 参数中最后执行runnable 可以单独初始化出来，传入公共参数，等待所有线程处理完成后统一汇总
    	// cyclicBarrier 用在多任务执行后还需要进一步处理的情况 , 不会妨碍后面的进行
    	// countownatch 会阻塞主线程 
    	
    	Runnable after = new Runnable() {  // 最后哦执行的任务 
			@Override
			public void run() {
				log.info("{} : 完成最后任务", Thread.currentThread().getName() );
			}
		};
    	
    	int threadNum = 5;
        CyclicBarrier barrier = new CyclicBarrier(threadNum, after);
        
        for(int i = 0; i < threadNum; i++) {
            new TaskThread(barrier).start();
        }
    }
    
}





