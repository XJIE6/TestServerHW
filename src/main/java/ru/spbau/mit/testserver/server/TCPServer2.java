package ru.spbau.mit.testserver.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer2 extends TCPServer {
    private ExecutorService cacedThreadPool;
    TCPServer2() throws IOException {
        super();
        cacedThreadPool = Executors.newCachedThreadPool();
    }
    void start() {
        while(!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                cacedThreadPool.execute(() -> whileHandle(socket));
            } catch (IOException e) {
            }
        }
    }
}
