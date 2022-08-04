package com.eron.mutithread;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.BiConsumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.checkerframework.common.reflection.qual.NewInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicJdkThreadUsage {
	
	private static final Logger log = LoggerFactory.getLogger(BasicJdkThreadUsage.class);
	
	public static void main(String[] args) {
		BasicJdkThreadUsage basic = new BasicJdkThreadUsage();
		
		basic.threadInfoUsage();  // 新城信息获取 
		
		basic.threadCustomUsage();  // 自定义线程执行 
		basic.lockUsage(); // 并发锁的使用 
		
		basic.scheduledTaskUsage(); // 定时任务
		basic.streamUsage();
		
		try {
			Thread.sleep(5 * 1000);
			
			basic.processUsage();  // 进程创建测试 
			
		} catch (InterruptedException | IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}
	
	public static class CustomThread extends Thread {

		@Override 
		public void run() {
			super.run();
			log.info("继承Thread重写run方法实现. CustomThread->run()");
			
		}
		
	}
	
	public static class CustomRunnable implements Runnable {
		// 实现runnable 传入thread 实现自定义线程 
		@Override
		public void run() {
			log.info("实现Runnable接口实现. CustomRunnable -> run()");
		}
		
	}
	
	public static class CustomCallable<V> implements Callable<V> {
		// Runnable 改进 有返回值 
		@Override 
		public V call() throws Exception {
			log.info("实现Callable接口. CustomCallable -> call()");
			
			return null;
		}
		
	}
	
	public void threadInfoUsage() {
		
		ThreadLocal<Integer> holder = ThreadLocal.withInitial(() -> {
			return 2;  // 初始化 等同于实现 initialValue 重写方法 
		});

		log.warn("more about threadInfo ~");
		ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
		ThreadInfo[] threads = threadMXBean.dumpAllThreads(false, false);
		Arrays.asList(threads).forEach(thread -> {
			log.info("threadInfo, threadID -> {}, threadName -> {}, thread State -> {}", 
				thread.getThreadId(), thread.getThreadName(), thread.getThreadState().toString()
			);
		});
		
	}
	
	public void threadCustomUsage() {
		// 实现自定义线程任务的实现 
		ThreadPoolExecutor executor;
		
		Thread thread1 = new CustomThread();
		thread1.start();
		
		Runnable customRunnable = new CustomRunnable();
		new Thread(customRunnable).start();
		
		Callable<String> customCallable = new CustomCallable<>();
		
		ExecutorService singleExecutor = Executors.newSingleThreadExecutor();  // 单线程线程池 
		ExecutorService cachedExecutor = Executors.newCachedThreadPool(); // 
		// 推荐使用自己定义实现 
		ThreadPoolExecutor customThreadPoolExecutor = new ThreadPoolExecutor(
					1, 2, 5, TimeUnit.SECONDS, 
					new ArrayBlockingQueue<Runnable>(10)
				);  // 建议使用手动的方式创建线程池 
		ScheduledThreadPoolExecutor sheduledExecutor = new ScheduledThreadPoolExecutor(3);
		
		singleExecutor.execute(customRunnable);  // submit 返回future
		
		CompletableFuture<Void> testCompletableFuture = CompletableFuture.runAsync(() -> {
			log.info("hello world, run CompletableFuture ...");
		});
		testCompletableFuture.whenComplete(new BiConsumer<Void, Throwable>() {
			@Override
			public void accept(Void t, Throwable u) {
				log.info("hello world, whenComplete run BiConsumer-> accept() ...");
			}
		});
		
	}
	
	public void lockUsage() {
		// 并发锁的使用 
		Lock lock = new ReentrantLock();
		
		try {
			lock.tryLock();
			// do something ...
		} catch (Exception e) {
			log.error("lock usage exception ! ERROR -> \n{}", e);
		} finally {
			lock.unlock();
		}
		
		ReadWriteLock readWriteLock = new ReentrantReadWriteLock();  // 读写锁 
		Semaphore semaphore = new Semaphore(0);  // 信号量 
		
	}
	
	public void scheduledTaskUsage() {
		log.info("定时任务");
		
		// jdk自带的timer   ScheduledThreadPoolService 定时线程池服务  DelayQueue 延迟队列
		// spring taskThreadPoolService spring实现的定时服务 配合EnableScheduled + Async 实现多线程异步
		// Quartz 第三方框架定时任务 
		
		log.info("使用timer");
		Timer scheduledTimer = new Timer();
		scheduledTimer.schedule(new TimerTask() {  // TimerTask继承了Runable  是一个abstract类 
			@Override
			public void run() {
				log.info("Timer 执行timerTask定时任务");
				
			}
		}, 1000L);
		
		log.info("使用scheduledThreadPoolExecutor 运行定时任务");
		ScheduledThreadPoolExecutor scheduledExecutor = new ScheduledThreadPoolExecutor(10);
		scheduledExecutor.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				log.info("使用scheduledExecutorService 执行定时任务");
			}
		}, 0, 10, TimeUnit.SECONDS);
		
		
	}
	
	public void streamUsage() {
	    // stream 的使用 
	    List<String> begin = new ArrayList<String>();
	    begin.add("hello");
	    begin.add("who");
	    begin.add("purge");
	    begin.add("checker");
	    begin.add("what");
	    
	    // StreamSupport stream化 
	    Long count = begin.stream().filter(x -> x.length() > 4).map(x -> x.toCharArray()).count();
	    log.info("计算结果 : {}", count);
	    
	    Stream<Integer> stream = Stream.of(1, 3, 5, 7, 9);
	}
	
	public void processUsage() throws IOException {
	    // 创建进程执行 
	    ProcessBuilder pb = new ProcessBuilder("la -la");
	    pb.start();
	    pb.command();
	}
	
}












