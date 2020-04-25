package com.chuan.nio.multithreads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hechuan
 */
public class NioServerWithThreadsPool {

    public static void main(String[] args) throws IOException {
        // 系统的并发受限于线程池的大小，以及可以创建的资源开销，超过线程数连接会等待，线程切换也会有资源开销
        ExecutorService pool = Executors.newFixedThreadPool(3);
        ServerSocket server = new ServerSocket(8888);
        System.out.println("BIO server started on: " + server.getLocalSocketAddress());

        RequestHandler handler = new RequestHandler();
        while (true) {
            // 每次来一个连接，交给线程池去处理
            Socket client = server.accept();
            pool.submit(new ClientHandler(client, handler));
        }
    }
}
