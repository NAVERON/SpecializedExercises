


package com.eron.designpattern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  动态代理 实现方法 
 * @author ERON_AMD 
 */
public class JdkDynamicProxy implements InvocationHandler {
    
    private final Logger log = LoggerFactory.getLogger(JdkDynamicProxy.class);
    
    private Object target;
    
    public static void main(String[] args) {
        Test proxy = (Test) new JdkDynamicProxy().getProxy(new MyTest());
        proxy.test();
    }
    
    private Object getProxy(Object obj){
        this.target = obj;
        log.info("get proxy : {}", this.target);
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
    }
    
    private void doBefore() {
        log.info("diBefore...");
    }
    
    private void doAfter() {
        log.info("doAfter...");
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable { 
        this.doBefore();
        Object obj = method.invoke(this.target, args);
        this.doAfter();
        return obj;
    }
}




interface Test {
    public void test();
}

class MyTest implements Test {
    private final Logger log = LoggerFactory.getLogger(MyTest.class);

    @Override
    public void test() {
        log.info("implenents");
    }
    
}





