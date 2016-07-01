package ru.spbau.mit.kravchenkoyura.testserver.server;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer2 extends TCPServer {
    private ExecutorService cachedThreadPool;
    public TCPServer2() throws IOException {
        super();
        cachedThreadPool = Executors.newCachedThreadPool();
    }
    public void start() {
        while(!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                cachedThreadPool.execute(() -> whileHandle(socket));
            } catch (IOException e) {
                //fail
                //wait for next client
            }
        }
        cachedThreadPool.shutdown();
    }
}
