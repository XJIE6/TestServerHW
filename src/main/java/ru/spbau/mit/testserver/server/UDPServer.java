package ru.spbau.mit.testserver.server;

import ru.spbau.mit.testserver.utils.ArraySorter;
import ru.spbau.mit.testserver.utils.ProtocolUtils;
import ru.spbau.mit.testserver.utils.TimeDatagramSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketAddress;

public abstract class UDPServer extends Server{
    protected TimeDatagramSocket socket;
    protected byte[] message;
    UDPServer() throws IOException {
        socket = new TimeDatagramSocket();
    }
    void close() {
        socket.close();
    }
    int getPort() {
        return socket.getLocalPort();
    }
    void handle(DatagramPacket packet) {
        try {
            packet = ProtocolUtils.byteToPacket(ProtocolUtils.listToByte(ArraySorter.sort(ProtocolUtils.byteTolist(ProtocolUtils.packetToByte(packet)))));
            socket.send(packet);
            socket.close();
        } catch (Exception e) {
        }
    }
}
