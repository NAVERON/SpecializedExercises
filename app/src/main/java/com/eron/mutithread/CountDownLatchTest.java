


package com.eron.mutithread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ERON_AMD
 */

public class CountDownLatchTest {
    
    public static void main(String[] args){
        System.out.println("hello every body ~");
        
        final CountDownLatch countdownlatch = new CountDownLatch(2);
        
        ExecutorService es1 = Executors.newSingleThreadExecutor();
        es1.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("第一个线程执行中，停顿一秒~");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CountDownLatchTest.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                countdownlatch.countDown();
            }
        });
        
        ExecutorService es2 = Executors.newSingleThreadExecutor();
        es2.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("第二个线程执行, 停顿2秒~~~");
                
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(CountDownLatchTest.class.getName()).log(Level.SEVERE, null, ex);
                }
                
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
            Logger.getLogger(CountDownLatchTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("两个线程均执行完毕~");
    }
    
}
