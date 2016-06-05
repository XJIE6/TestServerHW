package ru.spbau.mit.testserver.server;

import ru.spbau.mit.testserver.utils.ArraySorter;
import ru.spbau.mit.testserver.utils.ProtocolUtils;
import ru.spbau.mit.testserver.utils.TimeServerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class TCPServer extends Server{
    protected TimeServerSocket serverSocket;

    TCPServer() throws IOException {
        serverSocket = new TimeServerSocket(11111);
    }
    int getPort() {
        return serverSocket.getLocalPort();
    }
    void whileHandle(Socket socket) {
        try {
            while (!socket.isClosed()) {
                handle(socket);
            }
        } catch (IOException e) {
        }
    }
    void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
        }
    }
    void handle(Socket socket) throws IOException {
        System.out.println("handled");
        int size = socket.getInputStream().read();
        if (size < 0) {
            socket.close();
            return;
        }
        byte[] bytes = new byte[size];
        socket.getInputStream().read(bytes);
        bytes = ProtocolUtils.listToByte(ArraySorter.sort(ProtocolUtils.byteTolist(bytes)));
        socket.getOutputStream().write(bytes.length);
        socket.getOutputStream().write(bytes);
    }
}
