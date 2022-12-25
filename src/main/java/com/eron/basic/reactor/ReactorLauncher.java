package com.eron.basic.reactor;


// Reactor 启动调度器 依次启动客户端和服务端 
public class ReactorLauncher {

    public static void main(String[] args) {
        
        // 单reactor模型 联合测试 
        Thread singleReactor = new Thread(SingleReactorServer.createSingleReactorServer(9090));
        singleReactor.start();
        
        Thread clientThread = new Thread(new ReactorClient(9090));
        clientThread.start();
        
        // 多reactor模型测试 
        Thread multiReactor = new Thread(MultiReactorServer.createMultiReactor(9191));
        multiReactor.start();
        
        Thread multiClient = new Thread(new ReactorClient(9191));
        multiClient.start();
    }
    
    
}











