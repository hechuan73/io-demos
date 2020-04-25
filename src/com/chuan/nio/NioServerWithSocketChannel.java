package com.chuan.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * @author hechuan
 */
public class NioServerWithSocketChannel {

    public static void main(String[] args) {
        try {
            ServerSocketChannel server = ServerSocketChannel.open();
            server.bind(new InetSocketAddress(8888));
            System.out.println("NIO server started on: " + server.getLocalAddress());


            while (true) {
                // 默认分配的HeapByteBuffer，为堆内内存
                ByteBuffer buffer = ByteBuffer.allocate(1024);

                // server.accept()是阻塞式的获取客户端的连接，所以当该方法返回时，一定能够获取到连接。
                // server.configureBlocking(false)可以设置非阻塞式的获取客户端连接，accept()会立即返回，获取失败返回null，所以我们需
                // 要检查是否获取连接成功再读取数据。
                SocketChannel client = server.accept();
                System.out.println("Connection from: " + client.getRemoteAddress());
                client.read(buffer);

                System.out.println("Message from client " + client.getRemoteAddress() + ": " + new String(buffer.array()));
                buffer.clear();
                String res = "Response from server " + server.getLocalAddress() + ": my response\n";
                client.write(ByteBuffer.wrap(res.getBytes()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
