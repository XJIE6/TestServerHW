package ru.spbau.mit.testserver.client;

import ru.spbau.mit.testserver.utils.ProtocolUtils;
import ru.spbau.mit.testserver.utils.TimeDatagramSocket;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.stream.Collectors;

public class UDPClient extends Client{


    UDPClient(int elementNumber, int requestNumber, long delay) {
        super(elementNumber, requestNumber, delay);
    }

    @Override
    public void start(String host, int port) {
        try (TimeDatagramSocket socket = new TimeDatagramSocket(new InetSocketAddress(host, port))){
            for (int i = 0; i < requestNumber; i++) {
                List list = random.ints(elementNumber).boxed().collect(Collectors.toList());
                socket.send(ProtocolUtils.byteToPacket(ProtocolUtils.listToByte(list)));
                byte[] message = new byte[ProtocolUtils.MESSAGE_SIZE];
                DatagramPacket packet = new DatagramPacket(message, message.length);
                socket.receive(packet);
                list = ProtocolUtils.byteTolist(ProtocolUtils.packetToByte(packet));
                failed &= list.size() == elementNumber;
                Thread.sleep(delay);
            }
            socket.close();
        } catch (Exception e) {
            failed = true;
        }
    }
}
