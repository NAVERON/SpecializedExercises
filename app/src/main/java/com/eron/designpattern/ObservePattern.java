


package com.eron.designpattern;

import java.util.Vector;
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
    
    public static void main(String[] args) {
        Subject subject = new ConcreteSubject();
        
        Observer observer = new ConcreteObserver("1111111", "firstState");
        Observer observer1 = new ConcreteObserver("2222222", "observerState");
        
        observer.regist(subject);
        observer1.regist(subject);
        
        log.info("initial all finished !");
        subject.notifyAllObservers("wangyulong");
        
        observer1.offline(subject);
        
        subject.notifyAllObservers("hello");
        
    }
    
}





