package com.eron.basic.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMICallingAPI extends Remote {

    public String sayHello(String message) throws RemoteException;

}
