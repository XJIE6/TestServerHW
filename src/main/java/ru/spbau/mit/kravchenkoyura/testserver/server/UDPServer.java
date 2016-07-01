package ru.spbau.mit.kravchenkoyura.testserver.server;

import ru.spbau.mit.kravchenkoyura.testserver.utils.ProtocolUtils;
import ru.spbau.mit.kravchenkoyura.testserver.utils.ArraySorter;
import ru.spbau.mit.kravchenkoyura.testserver.utils.TimeDatagramSocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

//Арактный класс для всех UDP
//Имеет встроенную функцию обработки одного пакета

public abstract class UDPServer implements Server {
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
