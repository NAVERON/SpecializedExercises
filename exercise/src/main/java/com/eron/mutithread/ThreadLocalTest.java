package com.eron.mutithread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 虽然使用的是一个threadlocal，但是各自的值互相不影响 
 * @author ERON_AMD 
 * 举个例子 : 比如http请求中header带有token , 把token保存在threadlocal中, 可以方便后续的处理获取token 
 * 详情见 Ledger项目中 拦截器的处理 
 */
public class ThreadLocalTest {

    private static final Logger log = LoggerFactory.getLogger(ThreadLocalTest.class);

    public static void main(String[] args) {
        SequenceNumber seq = new SequenceNumber();
        CustomeThread t1 = new CustomeThread(seq);
        CustomeThread t2 = new CustomeThread(seq);
        CustomeThread t3 = new CustomeThread(seq);

        t1.start();
        t2.start();
        t3.start();
    }

    public static class CustomeThread extends Thread {

        private static final Logger log = LoggerFactory.getLogger(CustomeThread.class);
        SequenceNumber seqNum;

        public CustomeThread(SequenceNumber seqNum) {
            this.seqNum = seqNum;
        }

        @Override
        public void run() {
            for (int i = 0; i < 5; i++) {
                log.info("thread id " + this.getId() + " 输出当前seqNumber : "  +  seqNum.getNextSeq());
            }
        }
    }

    public static class SequenceNumber {

        private ThreadLocal<Integer> localVarible = new ThreadLocal<>() {
            @Override
            protected Integer initialValue() {
                return 0;
            }
        };

        public Integer getNextSeq() {
            localVarible.set(localVarible.get() + 1);
            return localVarible.get();
        }
    }
}






