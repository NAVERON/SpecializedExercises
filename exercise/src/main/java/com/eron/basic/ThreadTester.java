package com.eron.basic;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadTester {
    
    private static final Logger log = LoggerFactory.getLogger(ThreadTester.class);
    
    public static void main(String[] args) {
        log.info("thread test ...");
        ThreadTester tester = new ThreadTester();
        
        new Thread(()->{
            tester.test(3);
        }).start();
        
        new Thread(()->{
            tester.test(6);
        }).start();
    }
    
    
    public void test(Integer x) {  // 多线程调用方法不会干扰局部变量 
        
        int i = x;
        int j = i;
        
        while(x-- >= 0) {
            log.info("i = {}, j = {}, x = {}", i, j, x);
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }
}






