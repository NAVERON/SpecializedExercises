
package com.eron.designpattern;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  单线程情况下的观察者模式
 * @author ERON_AMD
 */
public class ObservePattern {
    
    private static final Logger log = LoggerFactory.getLogger(ObservePattern.class);
    
    // 观察者模式
    public static abstract class Subject{
        private Vector<Observer> observes = new Vector<Observer>();
        
        public void add(Observer observer){
            this.observes.add(observer);
        }
        
        public void remove(Observer observer){
            log.info("subject remove observer : {}", observer.toString());
            this.observes.remove(observer);
        }
        
        public void notifyAllObservers(String newState){
            throw new UnsupportedOperationException();
        }
        
    }
    
    public static class ConcreteSubject extends Subject{
        private String stateString;
        
        public String getState(){
            return this.stateString;
        }

        @Override
        public void notifyAllObservers(String newState) {
            for(Observer observer : super.observes){
                observer.update(newState);
            }
        }
        
    }
    
    public interface Observer{
        default void update(String state){
            throw new UnsupportedOperationException("unimplement method ~");
        }
        
        default void regist(Subject subject){
            throw new UnsupportedOperationException();
        }
        
        public void offline(Subject subject);
    }
    
    public static class ConcreteObserver implements Observer{
        
        private String name;
        private String observeState;
        
        public ConcreteObserver(String name, String initState){
            this.name = name;
            this.observeState = initState;
        }

        @Override
        public void update(String state) {
            this.observeState = state;
            log.info("state chenged : {}, {}", this.name, this.observeState);
        }

        @Override
        public void regist(Subject subject) {
            log.info("observer add self to Subject ! name : {}, state : {}", this.name, this.observeState);
            subject.add(this);
        }

        @Override
        public void offline(Subject subject) {
            log.info("observer remove item from subject !");
            subject.remove(this);
        }
        
        @Override
        public String toString(){
            return "name : " + name + " , state : " + observeState;
        }
        
    }
    
    // =========================================================================
    // java 9 之后deprecated 了之前的subject和observer接口 使用concurrent.Flow 中publisher subscribe 模型替代 
    
    // 官网自定义实现 publisher 
    public static class StreamPublisher<T> implements Flow.Publisher<T> {
    	
    	private Supplier<Stream<? extends T>> streamSuplier;
    	private List<Flow.Subscription> subscribtions = new LinkedList<>();  // 保存订阅者list 实现订阅着管理功能 
    	
    	public StreamPublisher(Supplier<Stream<? extends T>> streamSuplier) {
    		this.streamSuplier = streamSuplier;
    	}

		@Override
		public void subscribe(Flow.Subscriber<? super T> subscriber) {
			log.info("StreamPublisher subscribe");
			StreamSubscription streamSubscription = new StreamSubscription(subscriber);
			subscriber.onSubscribe(streamSubscription);
			
			streamSubscription.dealWithError();  // 处理过程中发生的错误 
		}
		
		// 我的理解  subscribtion 向但与包装了subscriber一层, 链接publisher和subscriber 
		// 创建List 可以实现增强管理订阅者 
	    private class StreamSubscription implements Flow.Subscription {  // 如果没有中间组件 publisher只能实现push模式 
	    	// 类中的 T 沿用 外部类 Publisher 的 T 
	    	private Flow.Subscriber<? super T> subscriber;
	    	private Iterator<? extends T> iterator;
	    	
	    	private AtomicBoolean isTerminated = new AtomicBoolean(false);  // 是否已经终结标志 
	    	private AtomicLong demand = new AtomicLong();  // 记录当前request总量 
	    	private AtomicReference<Throwable> error = new AtomicReference<>(); // 保存抛出的错误 统一处理给subscriber.onError() 
	    	
	    	public StreamSubscription(Flow.Subscriber<? super T> subscriber) {
	    		this.subscriber = subscriber;
	    		Iterator<? extends T> iterator = null;
	    		
	    		try {
	    			iterator = streamSuplier.get().iterator();
	    		} catch (Exception e) {
	    			error.set(e);
	    			// subscriber.onError(e);
				}
	    		
	    		this.iterator = iterator;
	    	}
	    	
			@Override
			public void request(long n) {
				if (n < 0 && !this.isTerminated()) {
					subscriber.onError(new IllegalArgumentException("negative subscription request !"));
					return;
				}
				
//				if(demand.getAndAdd(n) > 0) {  // 如果赏赐的请求没有结束, 直接返回 
//					return;
//				}
				for (;;) {  // 自旋  解决demand 越界问题 
				    long currentDemand = demand.getAcquire();
				    if (currentDemand == Long.MAX_VALUE) {
				        return;
				    }
				    long adjustedDemand = currentDemand + n;
				    if (adjustedDemand < 0L) { 
				        adjustedDemand = Long.MAX_VALUE;
				    }
				    if (demand.compareAndSet(currentDemand, adjustedDemand)) { // CAS compare and sawrp  https://en.wikipedia.org/wiki/Compare-and-swap     
				        if (currentDemand > 0) {
				            return;
				        }
				        break;
				    }
				}
				
				log.info("StreamSubscription request -> {}", n);
				for(; demand.get() > 0 && this.iterator.hasNext() && !this.isTerminated(); demand.decrementAndGet()) {
					try {
						this.subscriber.onNext(iterator.next());  // 因为onNext 中会调用request, 所以使用demand变量表明当前状态 
					} catch (Exception e) {
						if(!this.isTerminated()) {  // ? 这里的疑问, 为什么不直接使用subscriber onError?
							error.set(e);
						}
					}
				}
				
				if(this.iterator.hasNext() && !this.isTerminated()) { // 推送流结束 
					subscriber.onComplete();
				}
			}
			
			@Override 
			public void cancel() {
				this.terminate();
			}
			
			public Boolean terminate() {
				return this.isTerminated.getAndSet(true);
			}
			
			public Boolean isTerminated() {
				return this.isTerminated.get();
			}
			
			public void dealWithError() {
				Throwable throwable = error.get();
				if(throwable != null && !this.isTerminated()) {
					subscriber.onError(throwable);
				}
			}
	    	
	    }
    	
    }
    
    public static class StreamSubscriber<T> implements Flow.Subscriber<T> {

		@Override
		public void onSubscribe(Flow.Subscription subscription) {
			log.info("StreamSubscriber onSubscribe");
			subscription.request(6);
		}

		@Override
		public void onNext(T item) {
			log.info("StreamSubscriber onNext -> {}", item.toString());
		}

		@Override
		public void onError(Throwable throwable) {
			log.error("ERROR -> {}", throwable.toString());
		}

		@Override
		public void onComplete() {
			log.info("StreamSubscriber onComplete");
		}
    	
    }
    
    public static class CustomSubscriber implements Flow.Subscriber<Integer> {

    	private Flow.Subscription subscription;
    	private String name; // 订阅者名称 
    	private Long totalCount = 0L;  // 需要获取的总数 
    	private Long counter = 0L;  // 当前获取的量 
    	
    	public CustomSubscriber(String name, Long totalCount) {
    		this.name = name;
    		this.totalCount = totalCount;
    	}
    	
		@Override
		public void onSubscribe(Flow.Subscription subscription) {
			this.subscription = subscription;
			log.info("CustomeSubscribe onSubscribe, 订阅成功后执行");
			
			this.subscription.request(1);  // 订阅者 pull 模式 
			log.info("onSubscribe 订阅后第一次请求");
		}

		@Override
		public void onNext(Integer item) {
			log.info("CustomeSubscriber onNext => {}, subscriber name => {}", item, this.name);
			
			counter++;
			if(counter > totalCount) {
				log.warn("订阅者名称 : {}, 当前获取量 : {}, 需要获取总数 : {}", 
						this.name, this.counter, this.totalCount);
				subscription.cancel();
			}
			
			subscription.request(1);  // 请求下一个 
		}

		@Override
		public void onError(Throwable throwable) {
			// TODO Auto-generated method stub
			log.error("CustomeSubscriber onError => {}", throwable);
		}

		@Override
		public void onComplete() {
			// TODO Auto-generated method stub
			log.info("CustomeSubscriber onComplete -- {}", this.name);
			
		}
    	
    }
    
    public void oldObservableUsage() {
    	log.info("普通观察者模式创建");
        Subject subject = new ConcreteSubject();  // 订阅主题 
        Observer observer = new ConcreteObserver("1111111", "firstState");
        Observer observer1 = new ConcreteObserver("2222222", "observerState");
        
        observer.regist(subject); // 绑定观察者和被观察主题 
        observer1.regist(subject);
        
        log.info("initial all finished !");
        subject.notifyAllObservers("wangyulong");
        observer1.offline(subject);
        subject.notifyAllObservers("hello");
    }
    
    public void publisherSubscriberUsage() {
    	log.info("java 9+ Publisher Subscriber 模型使用");
    	
    	// 使用jdk实现的submissionpublisher 
    	log.info("使用jdk 实现的SubmissionPublisher");
        try (SubmissionPublisher<Integer> customPublisher = new SubmissionPublisher<>();) { // ForkJoinPool.commonPool(), 16
	        CustomSubscriber customeSubscriber = new CustomSubscriber("test", 20L);
	        customPublisher.subscribe(customeSubscriber);
	        IntStream.range(10, 50).forEach(customPublisher::submit); // 模拟 publisher 提供内容源 stream 
	        try {
				Thread.sleep(3000);  // ? 问题 : 为什么需要延迟结束主线程 ? 
				// 猜测 publisher使用了 future等异步执行, 有一个等待时间, 
				// 如果主线程在提交后直接结束, 会使得提交的任务来不及回调而直接结束
				log.info("主线程sleep结束");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	        // customPublisher.close(); // 如果使用try resource 结构, 不需要主动关闭 因为实现了 AutoCloseable 
        }
        
        log.info("使用自定义实现的Publisher ---");
        Random rand = new Random();
        // 自定义实现publisher 
        Supplier<Stream<? extends Number>> supplier = () -> Stream.of(1, 3, 4, 5, 6, 7, 9);
        StreamPublisher<Number> streanPublisher = new StreamPublisher<>(supplier);
        StreamSubscriber<Number> streamSubscriber = new StreamSubscriber<>();
        streanPublisher.subscribe(streamSubscriber);
        
    }
    
    public static void main(String[] args) {
    	
    	ObservePattern observePattern = new ObservePattern();
    	
    	// 普通观察者模式 
    	observePattern.oldObservableUsage();
    	// 发布者订阅模式 
    	observePattern.publisherSubscriberUsage();
        
    }
    
}





