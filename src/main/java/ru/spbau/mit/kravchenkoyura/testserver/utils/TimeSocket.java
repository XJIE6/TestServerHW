package ru.spbau.mit.kravchenkoyura.testserver.utils;

import java.io.IOException;
import java.net.Socket;

//Обычный Socket, который считает время между открытием и закрытием соединения
//Используется в TCP серверах и клиентах

public class TimeSocket extends Socket implements TimeCounter {
    private long startTime;

    public TimeSocket() {
        super();
        startTime = System.currentTimeMillis();
    }
    public TimeSocket(String host, int port) throws IOException {
        super(host, port);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void close() throws IOException {
        times.add(System.currentTimeMillis() - startTime);
        super.close();
    }
}
