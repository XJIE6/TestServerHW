package ru.spbau.mit.kravchenkoyura.testserver.server;

import ru.spbau.mit.kravchenkoyura.testserver.utils.ArraySorter;
import ru.spbau.mit.kravchenkoyura.testserver.utils.ProtocolUtils;
import ru.spbau.mit.kravchenkoyura.testserver.utils.TimeServerSocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Арактный класс для всех TCP серверов (кроме NIO)
//Имеет встроенную функцию обработки одного соединения

public abstract class TCPServer implements Server {
    protected ServerSocket serverSocket;
    public TCPServer() throws IOException {
        serverSocket = new TimeServerSocket(0);
    }
    public int getPort() {
        return serverSocket.getLocalPort();
    }
    public void close() throws IOException {
        serverSocket.close();
    }

    protected void whileHandle(Socket socket) {
        while (!socket.isClosed()) {
            handle(socket);
        }
    }

    protected void handle(Socket socket) {

        try {
            byte[] bytes = new byte[ProtocolUtils.MESSAGE_SIZE];
            socket.getInputStream().read(bytes);
            socket.getOutputStream().write(ProtocolUtils.listToBytes(ArraySorter.sort(ProtocolUtils.bytesToList(bytes))));
            socket.getOutputStream().flush();

        } catch (IOException e) {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e1) {
                //do nothing, checked closing
            }
        }
    }

}
