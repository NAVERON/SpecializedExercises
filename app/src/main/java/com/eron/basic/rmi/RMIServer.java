package com.eron.basic.rmi;


public class RMIServer implements RMICallingAPI {

	@Override
	public String sayHello(String message) {
		
		return "default Implement ! -> " + message;
	}

	
}
