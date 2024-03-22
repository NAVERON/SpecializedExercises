/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eron.designpattern;

/**
 * 一共有三种思路
 * 1  内部类
 * 2  双重锁
 * 3  enum枚举  实际中使用比较好
 *
 * @author ERON_AMD
 */
public class Singleton {

    private Singleton() {
    }  // 防止其他方式初始化

    private static volatile Singleton instance = null;

    public static Singleton getInstance() {  // effective java 推荐 
        return HOLDER.INSTANCE;
    }

    private static class HOLDER {
        private static final Singleton INSTANCE = new Singleton();
    }
}

class TwoAuthSingle {  // 双重锁   两个线程同时进入 第一层null判断， 锁定class， 第一个完成后， 第二个线程继续， 所以内层需要再次判断 

    private static volatile TwoAuthSingle instance;

    // 双重检查
    private TwoAuthSingle() {
    }

    public static TwoAuthSingle getInstance() {  // 这里不需要synchnorized ，  不会那么慢， 这个只需要一次创建，所以加synchronized不好
        if (instance == null) {
            synchronized (TwoAuthSingle.class) {
                if (null == instance) {
                    instance = new TwoAuthSingle();
                }
            }
        }

        return instance;
    }

    public void testBelow() {
        SingleEnum single = SingleEnum.INSTANCE;  // 枚举天生单例
        single.doSomething();
    }

}


enum SingleEnum {
    INSTANCE;

    Singleton instance;

    public void doSomething() {
        System.out.println("doing...");
    }
}


