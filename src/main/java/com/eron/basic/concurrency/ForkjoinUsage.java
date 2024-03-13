package com.eron.basic.concurrency;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.*;

/**
 * 使用forkjoin
 *
 * @author eron
 */
public class ForkjoinUsage {
    private static final Logger log = LoggerFactory.getLogger(ForkjoinUsage.class);
    public static final int threshost = 250;

    public static void main(String[] args) {
        Random random = new Random();
        double sum = 0;

        // 创建随机数组 
        int[] a = new int[10000];
        for (int i = 0; i < a.length; i++) {
            a[i] = random.nextInt(100);
            sum += a[i];
        }
        log.info("期望总和 --> {}", sum);

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        RealTask realTask = new RealTask(a, 0, a.length);
        try {
            Double result = forkJoinPool.submit(realTask).get(5, TimeUnit.SECONDS);
            log.info("最终结果 --> {}", result);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static class RealTask extends RecursiveTask<Double> {

        int[] arr;
        private int start = 0;
        private int end = 0;

        public RealTask(int[] arr, int start, int end) {
            this.arr = arr;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Double compute() {
            if (end - start < ForkjoinUsage.threshost) {
                double sum = 0;
                for (int i = start; i < end; i++) {
                    sum += arr[i];
                }

                return sum;
            }
            // 子任务拆分 
            int middle = (end + start) / 2;
            RealTask task1 = new RealTask(arr, start, middle);
            RealTask task2 = new RealTask(arr, middle, end);
            ForkJoinTask.invokeAll(task1, task2);

            double res1 = task1.join();
            double res2 = task2.join();
            return res1 + res2;
        }
    }
}







