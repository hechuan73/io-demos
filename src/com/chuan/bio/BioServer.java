package com.chuan.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author hechuan
 */
public class BioServer {

    public static void main(String[] args) {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(8888);

            System.out.println("BIO server started on: " + socket.getLocalSocketAddress());

            while (true) {
                // 不停接收连接
                Socket client = socket.accept();
                System.out.println("Connection from: " + client.getRemoteSocketAddress());

                // client.getInputStream()是阻塞的，所以某个用户连接时，其余用户会被阻塞
                try (Scanner input = new Scanner(client.getInputStream())) {
                    // 不停与同一客户端交互
                    while (true) {
                        String req = input.nextLine();
                        if ("quit".equals(req)) {
                            break;
                        }

                        System.out.println("Message from client " + client.getRemoteSocketAddress() + ": " + req);
                        String res = "Response from server " + socket.getLocalSocketAddress() + ": my response\n";
                        client.getOutputStream().write(res.getBytes());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
