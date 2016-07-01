package ru.spbau.mit.kravchenkoyura.testserver.server;

import ru.spbau.mit.kravchenkoyura.testserver.utils.ProtocolUtils;
import ru.spbau.mit.kravchenkoyura.testserver.utils.TimeDatagramSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UDPServer2 extends UDPServer{
    private ExecutorService threadPool;
    private final static int THREAD_COUNT = 4;
    public UDPServer2() throws IOException {
        super();
        threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
    }
    public void start() {
        while (!socket.isClosed()) {
            try {
                message = new byte[ProtocolUtils.MESSAGE_SIZE];
                DatagramPacket packet = new DatagramPacket(message, message.length);
                socket.receive(packet);
                threadPool.execute(() -> handle(packet));
                ((TimeDatagramSocket) socket).round();
            } catch (IOException e) {
                //fail
                //next client
            }
        }
        threadPool.shutdown();
    }
}
