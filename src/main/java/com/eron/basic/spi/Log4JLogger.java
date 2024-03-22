package com.eron.basic.spi;

public class Log4JLogger implements CommonLog {

    @Override
    public String info(String message) {
        return "Log4JLogger - info = " + message;
    }

    @Override
    public String error(String errorMessage) {
        return "Log4JLogger - error = " + errorMessage;
    }

}
