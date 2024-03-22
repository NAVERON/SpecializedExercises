package com.eron.basic.rmi;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * remote method invocation 远程方法调用
 *
 * @author eron
 */
public class RMIAppLauncher {

    private static final Logger log = LoggerFactory.getLogger(RMIAppLauncher.class);
    private static final String serviceKeyName = "DefaultCallingAPIImplService";

    public static void main(String[] args) throws RemoteException, AlreadyBoundException, NotBoundException {
        RMIAppLauncher launcher = new RMIAppLauncher();

        // 创建服务RMI 注册
        launcher.registRMiService();
        // 创建客户端调用
        launcher.clientCallingService();

    }

    // 注册远程方法调用服务
    public void registRMiService() throws RemoteException, AlreadyBoundException {
        RMICallingAPI defaultImpl = new RMIServer();
        RMICallingAPI stub = (RMICallingAPI) UnicastRemoteObject.exportObject(defaultImpl, 0);

        Registry localRegistry = LocateRegistry.createRegistry(9090);
        localRegistry.bind(RMIAppLauncher.serviceKeyName, stub);

    }

    // 客户端调用
    public void clientCallingService() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(9090);
        RMICallingAPI remoteCall = (RMICallingAPI) registry.lookup(RMIAppLauncher.serviceKeyName);

        String serverMessage = remoteCall.sayHello("client calling");
        log.info("returned from server : {}", serverMessage);

    }

}








