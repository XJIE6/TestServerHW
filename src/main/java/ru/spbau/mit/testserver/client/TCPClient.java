package ru.spbau.mit.testserver.client;

import ru.spbau.mit.testserver.utils.ProtocolUtils;
import ru.spbau.mit.testserver.utils.TimeSocket;

import java.util.stream.Collectors;

public class TCPClient extends Client {
    int connectionsNumber;
    TCPClient(int elementNumber, int requestNumber, int connectionsNumber, long delay) {
        super(elementNumber, requestNumber, delay);
        this.connectionsNumber = connectionsNumber;
        System.out.println("constructed");
    }

    @Override
    public void start(String host, int port) {
        System.out.println("started");
        for (int j = 0; j < connectionsNumber; j++) {
            try (TimeSocket socket = new TimeSocket(host, port)) {
                System.out.println("connected");
                for (int i = 0; i < requestNumber; i++) {
                    byte[] bytes = ProtocolUtils.listToByte(random.ints(elementNumber).boxed().collect(Collectors.toList()));
                    System.out.println("sended");
                    System.out.println(bytes.length);
                    socket.getOutputStream().write(bytes.length);
                    socket.getOutputStream().write(bytes);
                    System.out.println("readed");
                    int count = socket.getInputStream().read();
                    failed &= count == elementNumber;
                    bytes = new byte[count];
                    count = socket.getInputStream().read(bytes);
                    System.out.println("closed");
                    Thread.sleep(delay);
                }
                socket.close();
            } catch (Exception e) {
                failed = true;
                e.printStackTrace();
            }
        }
        System.out.println("ended");
    }
}
