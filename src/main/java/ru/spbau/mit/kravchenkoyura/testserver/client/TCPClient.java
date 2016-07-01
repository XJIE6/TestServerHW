package ru.spbau.mit.kravchenkoyura.testserver.client;


import ru.spbau.mit.kravchenkoyura.testserver.utils.ProtocolUtils;
import ru.spbau.mit.kravchenkoyura.testserver.utils.TimeSocket;

import java.io.IOException;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;


public class TCPClient extends Client{
    private int connectionNumber;
    public TCPClient(int elementNumber, int requestNumber, int connectionNumber, long delay) {
        super(elementNumber, requestNumber, delay);
        this.connectionNumber = connectionNumber;
    }

    @Override
    public void start(String host, int port) {
        for (int i = 0; i < connectionNumber; i++) {

            try(Socket socket = new TimeSocket(host, port)) {

                for (int j = 0; j < requestNumber; j++) {
                    List<Integer> list = ProtocolUtils.randomList(elementNumber);

                    socket.getOutputStream().write(ProtocolUtils.listToBytes(list));
                    socket.getOutputStream().flush();

                    list.sort(Comparator.naturalOrder());
                    byte[] bytes = new byte[ProtocolUtils.MESSAGE_SIZE];

                    socket.getInputStream().read(bytes);

                    List ans = ProtocolUtils.bytesToList(bytes);
                    assert (list.equals(ans));

                    Thread.sleep(delay);
                }
            } catch (IOException | InterruptedException e) {
                //fail
                //go to next connection
            }
        }
    }
}
