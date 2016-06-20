package ru.spbau.mit.testserver.server;

import ru.spbau.mit.testserver.utils.ProtocolUtils;

import java.io.IOException;
import java.net.DatagramPacket;

public class UDPServer1 extends UDPServer{
    public UDPServer1() throws IOException {
        super();
    }
    public void start() {
        while (!socket.isClosed()) {
            try {
                message = new byte[ProtocolUtils.MESSAGE_SIZE];
                DatagramPacket packet = new DatagramPacket(message, message.length);
                socket.receive(packet);
                new Thread(() -> handle(packet)).start();
            } catch (IOException e) {
                //socket closed
            }
        }
    }
}
