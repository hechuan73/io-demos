package com.chuan.nio.multithreads;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author hechuan
 */
public class ClientHandler implements Runnable {

    private Socket client;
    private RequestHandler handler;

    public ClientHandler(Socket clientSocket, RequestHandler handler) {
        this.client = clientSocket;
        this.handler = handler;
    }

    @Override
    public void run() {
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
                String res = "Response from server " + handler.handle(req);
                client.getOutputStream().write(res.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
