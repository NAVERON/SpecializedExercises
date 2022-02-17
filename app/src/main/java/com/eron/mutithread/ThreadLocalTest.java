package com.eron.mutithread;

import java.util.logging.Logger;

/**
 *
 * @author ERON_AMD
 */
public class ThreadLocalTest {

    private static final Logger log = Logger.getLogger(ThreadLocalTest.class.getName());

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

        private static final Logger log = Logger.getLogger(CustomeThread.class.getName());
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






