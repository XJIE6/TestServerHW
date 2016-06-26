package ru.spbau.mit.testserver.server;

import com.google.protobuf.InvalidProtocolBufferException;
import ru.spbau.mit.testserver.utils.ArraySorter;
import ru.spbau.mit.testserver.utils.ProtocolUtils;
import ru.spbau.mit.testserver.utils.TimeDatagramSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public abstract class UDPServer extends Server{
    protected DatagramSocket socket;
    protected byte[] message;
    UDPServer() throws IOException {
        socket = new TimeDatagramSocket(0);
        System.out.print(socket.getLocalPort());
    }
    public void close() {
        socket.close();
    }
    public int getPort() {
        return socket.getLocalPort();
    }
    protected void handle(DatagramPacket packet) {
        try {
            byte[] bytes = ProtocolUtils.listToBytes(ArraySorter.sort(ProtocolUtils.bytesToList(packet.getData())));
            socket.send(new DatagramPacket(bytes, bytes.length, packet.getAddress(), packet.getPort()));
        } catch (IOException e) {
            //fail
            //exit
        }
    }
}
