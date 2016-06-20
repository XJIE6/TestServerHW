package ru.spbau.mit.testserver.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class TimeDatagramSocket extends DatagramSocket implements TimeCounter {
    private boolean started = false;
    private long startTime;

    private void start() {
        if (!started) {
            startTime = System.currentTimeMillis();
            started = true;
        }
    }

    public TimeDatagramSocket() throws SocketException {
        super();
        startTime = System.currentTimeMillis();
    }

    public TimeDatagramSocket(int port) throws SocketException {
        super(port);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void receive(DatagramPacket packet) throws IOException {
        super.receive(packet);
        start();
    }

    @Override
    public void send(DatagramPacket packet) throws IOException {
        super.send(packet);
        start();
    }

    @Override
    public void close() {
        times.add(System.currentTimeMillis() - startTime);
        super.close();
    }
}
