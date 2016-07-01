package ru.spbau.mit.kravchenkoyura.testserver.server;

import java.io.IOException;
import java.net.Socket;

public class TCPServer1 extends TCPServer {
    public TCPServer1() throws IOException {
        super();
    }
    public void start() {
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(() -> whileHandle(socket)).start();
            } catch (IOException e) {
                //fail
                //wait for next client
            }
        }
    }
}
