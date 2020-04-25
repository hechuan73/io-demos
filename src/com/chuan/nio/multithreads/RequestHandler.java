package com.chuan.nio.multithreads;

/**
 * @author hechuan
 */
public class RequestHandler {

    public String  handle(String message) {
        return ": my response for " + message + "\n";
    }
}
