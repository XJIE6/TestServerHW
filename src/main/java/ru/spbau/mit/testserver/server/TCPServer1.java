package ru.spbau.mit.testserver.server;

import java.io.IOException;
import java.net.Socket;

public class TCPServer1 extends TCPServer {
    public TCPServer1() throws IOException {
        super();
    }
    public void start() {

        System.out.println("started");
        System.out.print(serverSocket.isClosed());
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("accepted");
                new Thread(() -> whileHandle(socket)).start();
            } catch (IOException e) {
                e.printStackTrace();
                //fail
                //wait for next client
            }
        }
    }
}
