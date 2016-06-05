package ru.spbau.mit.testserver.server;

import java.io.IOException;
import java.net.Socket;

public class TCPServer4 extends TCPServer {
    TCPServer4() throws IOException {
        super();
    }
    void start() {
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                new Thread(() -> {
                    try {
                        handle(socket);
                        socket.close();
                    } catch (IOException e) {
                    }
                }).start();
            } catch (IOException e) {
            }
        }
    }
}
