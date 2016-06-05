package ru.spbau.mit.testserver.server;

import java.io.IOException;
import java.net.Socket;

public class TCPServer1 extends TCPServer {
    TCPServer1() throws IOException {
        super();
    }
    void start() {
        System.out.println("started");
        System.out.println(getPort());
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("accepted");
                new Thread(() -> whileHandle(socket)).start();
            } catch (IOException e) {
            }
        }
    }
}
