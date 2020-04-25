package com.chuan.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * @author hechuan
 */
public class AioHandler implements CompletionHandler<AsynchronousSocketChannel, AioServer> {

    @Override
    public void completed(AsynchronousSocketChannel result, AioServer attachment) {
        // 调用accept()继续接收其它客户端的请求
        attachment.getServerChannel().accept(attachment, this);
        // 处理请求
        process(result);
    }

    @Override
    public void failed(Throwable exc, AioServer attachment) {
        exc.printStackTrace();
    }

    private void process(AsynchronousSocketChannel clientChannel) {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        try {
            System.out.println("Connection from: " + clientChannel.getRemoteAddress());
            clientChannel.read(buffer);
            System.out.println("Message from client " + clientChannel.getRemoteAddress() + ": " + new String(buffer.array()));
            buffer.clear();
            String res = "Response from server " + clientChannel.getLocalAddress() + ": my response\n";
            clientChannel.write(ByteBuffer.wrap(res.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
