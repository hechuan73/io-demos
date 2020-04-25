package com.chuan.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author hechuan
 */
public class AioServer {

    AsynchronousServerSocketChannel serverChannel;
    ExecutorService pool;
    AsynchronousChannelGroup tg;

    public AsynchronousServerSocketChannel getServerChannel() {
        return serverChannel;
    }

    public AioServer(int port) {
        init(port);
    }

    private void init(int port) {
        try {
            pool = Executors.newFixedThreadPool(3);
            // 创建异步通信组
            tg = AsynchronousChannelGroup.withCachedThreadPool(pool, 1);
            // 打开服务端异步通信channel
            serverChannel = AsynchronousServerSocketChannel.open(tg);
            serverChannel.bind(new InetSocketAddress(port));
            // 接收客户端请求，传入回调函数
            serverChannel.accept(this, new AioHandler());


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
