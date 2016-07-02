package ru.spbau.mit.kravchenkoyura.testserver.utils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

//Обычный DatagramSocket, который считает время от первого пакета до последнего
//Используется в UDP серверах и клиентах

public class TimeDatagramSocket extends DatagramSocket implements TimeCounter {
    private boolean started = false;
    private long startTime;
    private long roundTime;
    private boolean failed = false;

    public void fail() {
        failed = true;
    }

    private void start() {
        if (!started) {
            startTime = System.currentTimeMillis();
            roundTime = startTime;
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
        if (!failed) {
            if (roundTime == startTime) {
                times.add(System.currentTimeMillis() - startTime);
            } else {
                times.add(roundTime - startTime);
            }
        }
        super.close();
    }

    public void round() {
        roundTime = System.currentTimeMillis();
    }

}
