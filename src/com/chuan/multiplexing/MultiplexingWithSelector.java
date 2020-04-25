package com.chuan.multiplexing;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author hechuan
 */
public class MultiplexingWithSelector {

    public static void main(String[] args) {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(8888));
            server.configureBlocking(false);
            System.out.println("NIO server started on: " + server.getLocalAddress());

            Selector selector = Selector.open();
            // 向selector注册serverSocket感兴趣的socket连接就绪的事件
            server.register(selector, SelectionKey.OP_ACCEPT);
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (true) {
                // 查询就绪事件
                int readyCount = selector.select();
                if (readyCount == 0) {
                    continue;
                }

                // 获取就绪事件的key集合，然后遍历
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                SelectionKey key;
                SocketChannel client;
                while (iterator.hasNext()) {
                    key = iterator.next();
                    // 连接就绪
                    if (key.isAcceptable()) {
                        ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                        client = channel.accept();
                        System.out.println("Connection from: " + client.getRemoteAddress());
                        client.configureBlocking(false);
                        // 注册感兴趣的读就绪事件
                        client.register(selector, SelectionKey.OP_READ);
                    }

                    // 读就绪
                    if (key.isReadable()) {
                        client = (SocketChannel) key.channel();
                        client.read(buffer);
                        System.out.println("Message from client " + client.getRemoteAddress() + ": " + new String(buffer.array()));
                        buffer.clear();
                        String res = "Response from server " + server.getLocalAddress() + ": my response\n";
                        client.write(ByteBuffer.wrap(res.getBytes()));
                    }
                    // 删除本次事件
                    iterator.remove();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
