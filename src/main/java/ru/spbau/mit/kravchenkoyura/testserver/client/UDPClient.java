package ru.spbau.mit.kravchenkoyura.testserver.client;

import ru.spbau.mit.kravchenkoyura.testserver.utils.ProtocolUtils;
import ru.spbau.mit.kravchenkoyura.testserver.utils.TimeDatagramSocket;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//UDP клиент
//Делает UDP запросы
//Запускается главным клиентом

public class UDPClient extends Client{

    public UDPClient(int elementNumber, int requestNumber, long delay) {
        super(elementNumber, requestNumber, delay);
    }

    @Override
    public void start(String host, int port) {
        try (TimeDatagramSocket socket = new TimeDatagramSocket()){

            for (int i = 0; i < requestNumber; i++) {
                List<Integer> list = ProtocolUtils.randomList(elementNumber);

                byte[] bytes = ProtocolUtils.listToBytes(list);
                socket.send(new DatagramPacket(bytes, bytes.length, InetAddress.getByName(host), port));

                byte[] message = new byte[ProtocolUtils.MESSAGE_SIZE];
                DatagramPacket packet = new DatagramPacket(message, message.length);
                socket.receive(packet);

                list.sort(Comparator.naturalOrder());
                List ans = ProtocolUtils.bytesToList(packet.getData());

                assert(list.equals(ans));

                Thread.sleep(delay);
            }

        } catch (Exception e) {
            //fail
            //exit
        }
    }
}
