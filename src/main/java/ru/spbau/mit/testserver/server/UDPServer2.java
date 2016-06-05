package ru.spbau.mit.testserver.server;

import ru.spbau.mit.testserver.utils.ProtocolUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPServer2 extends UDPServer{
    private ExecutorService threadPool;
    private final static int THREAD_COUNT = 4;
    UDPServer2() throws IOException {
        super();
        threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
    }
    void start() {
        while (!socket.isClosed()) {
            try {
                message = new byte[ProtocolUtils.MESSAGE_SIZE];
                DatagramPacket packet = new DatagramPacket(message, message.length);
                socket.receive(packet);
                threadPool.execute(() -> handle(packet));
            } catch (IOException e) {
            }
        }
    }
}
