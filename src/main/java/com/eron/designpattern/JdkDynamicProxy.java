
package com.eron.designpattern;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  动态代理 实现方法 
 * @author ERON_AMD 
 *  1.拿到被代理对象的引用，然后获取他的接口 (Proxy.newProxyInstance方法)
 *   2.JDK代理重新生成一个类，同时实现我们给的代理对象所实现的接口 
 *   3.把被代理对象的引用拿到了, 在静态代码块中通过反射获取到信息，我们实现的JdkDynamicProxy中的target 
 *   4.重新动态生成一个class字节码
 *   5.然后编译
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
        log.info("doBefore...");
    }
    
    private void doAfter() {
        log.info("doAfter...");
    }
    
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable { 
        this.doBefore();
        // 可以设计只针对个别的方法 
        String methodName = method.getName();
        log.info("获取代理对象的方法名称 : {}", methodName);
        
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





