package ru.spbau.mit.testserver.server;

import java.io.IOException;
import java.net.Socket;

public class TCPServer4 extends TCPServer {
    public TCPServer4() throws IOException {
        super();
    }
    public void start() {
        while (!serverSocket.isClosed()) {
            Socket socket;
            try {
                socket = serverSocket.accept();
            } catch (IOException e) {
                continue;
                //fail
                //wait for next client
            }
            new Thread(() -> {
                try {
                    handle(socket);
                    socket.close();
                } catch (IOException e) {
                    //fail
                    //wait for next client
                }
            }).start();
        }
    }
}
