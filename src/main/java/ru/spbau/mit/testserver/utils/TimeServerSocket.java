package ru.spbau.mit.testserver.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class TimeServerSocket extends ServerSocket{
    public TimeServerSocket(int port) throws IOException {
        super(port);
    }
    @Override
    public Socket accept() throws IOException{
        if (isClosed())
            throw new SocketException("Socket is closed");
        if (!isBound())
            throw new SocketException("Socket is not bound yet");
        TimeSocket s = new TimeSocket();
        implAccept(s);
        return s;
    }
}
