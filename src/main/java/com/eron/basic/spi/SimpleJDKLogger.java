package com.eron.basic.spi;

public class SimpleJDKLogger implements CommonLog {

	@Override
	public String info(String message) {
		return "SimpleJDKLogger - info = " + message;
	}

	@Override
	public String error(String errorMessage) {
		return "SimpleJDKLogger - error = " + errorMessage;
	}

}
