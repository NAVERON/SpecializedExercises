package com.eron.mutithread;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicJdkThreadUsage {
	
	private static final Logger log = LoggerFactory.getLogger(BasicJdkThreadUsage.class);
	
	public static void main(String[] args) {
		BasicJdkThreadUsage basic = new BasicJdkThreadUsage();
		
		basic.threadInfoUsage();  // 新城信息获取 
		
		basic.threadCustomUsage();  // 自定义线程执行 
		basic.lockUsage(); // 并发锁的使用 
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
		ThreadPoolExecutor customThreadPoolExecutor = new ThreadPoolExecutor(0, 0, 0, null, null);  // 建议使用手动的方式创建线程池 
		
		singleExecutor.execute(customRunnable);  // submit 返回future
		
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
	
	
}












