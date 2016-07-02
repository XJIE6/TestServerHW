package ru.spbau.mit.kravchenkoyura.testserver.client;

import ru.spbau.mit.kravchenkoyura.testserver.utils.ProtocolUtils;
import ru.spbau.mit.kravchenkoyura.testserver.utils.TimeSocket;

import java.io.IOException;
import java.net.Socket;
import java.util.Comparator;
import java.util.List;

//TCP клиент
//Делает TCP запросы
//Запускается главным клиентом

public class TCPClient extends Client{
    private int connectionNumber;
    public TCPClient(int elementNumber, int requestNumber, int connectionNumber, long delay) {
        super(elementNumber, requestNumber, delay);
        this.connectionNumber = connectionNumber;
    }

    @Override
    public void start(String host, int port) {
        for (int i = 0; i < connectionNumber; i++) {
            Socket socket = null;
            try {
                socket = new TimeSocket(host, port);

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

                socket.close();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                ((TimeSocket) socket).fail();
                if (!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        //checked closing
                    }
                }
                //fail
                //go to next connection
            }
        }
    }
}
