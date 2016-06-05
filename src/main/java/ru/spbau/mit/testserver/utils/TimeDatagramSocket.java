package ru.spbau.mit.testserver.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.net.SocketException;

public class TimeDatagramSocket extends DatagramSocket implements TimeCounter {
    private boolean started = false;
    private long startTime;

    private void start() {
        if (!started) {
            startTime = System.nanoTime();
            started = true;
        }
    }

    public TimeDatagramSocket() throws SocketException {
        super();
    }

    public TimeDatagramSocket(int port) throws SocketException {
        super(port);
    }

    public TimeDatagramSocket(SocketAddress addr) throws SocketException {
        super(addr);
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
        times.add(System.nanoTime() - startTime);
        super.close();
    }
}
