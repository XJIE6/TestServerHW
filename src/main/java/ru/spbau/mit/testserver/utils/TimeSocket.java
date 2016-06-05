package ru.spbau.mit.testserver.utils;

import java.io.IOException;
import java.net.Socket;

public class TimeSocket extends Socket implements TimeCounter {
    private long startTime;

    TimeSocket() {
        super();
        startTime = System.nanoTime();
    }
    public TimeSocket(String host, int port) throws IOException {
        super(host, port);
        startTime = System.nanoTime();
    }

    @Override
    public void close() throws IOException {
        times.add(System.nanoTime() - startTime);
        super.close();
    }
}
