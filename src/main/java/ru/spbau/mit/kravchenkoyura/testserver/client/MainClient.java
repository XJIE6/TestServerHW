package ru.spbau.mit.kravchenkoyura.testserver.client;

import ru.spbau.mit.kravchenkoyura.testserver.utils.ProtocolUtils;
import ru.spbau.mit.kravchenkoyura.testserver.utils.TimeCounter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;

public class MainClient {

    private Socket socket;
    private String ip;
    private static Client getClient(int cmd, int elementNumber, int requestNumber, long delay) {
        switch(cmd) {
            case ProtocolUtils.RUN_TCP_1:
                return new TCPClient(elementNumber, requestNumber, 1, delay);
            case ProtocolUtils.RUN_TCP_2:
                return new TCPClient(elementNumber, requestNumber, 1, delay);
            case ProtocolUtils.RUN_TCP_3:
                return new TCPClient(elementNumber, requestNumber, 1, delay);
            case ProtocolUtils.RUN_TCP_4:
                return new TCPClient(elementNumber, 1, requestNumber, delay);
            case ProtocolUtils.RUN_UDP_1:
                return new UDPClient(elementNumber, requestNumber, delay);
            case ProtocolUtils.RUN_UDP_2:
                return new UDPClient(elementNumber, requestNumber, delay);
            default:
                return null;
        }
    }

    MainClient(String ip) throws IOException {
        socket = new Socket();
        this.ip = ip;
        socket.connect(new InetSocketAddress(ip, ProtocolUtils.SERVER_PORT));
    }

    double[] runServer(int cmd, int clientNumber, int elementNumber, int requestNumber, long delay) {
        TimeCounter.resetTime();
        DataInputStream in;
        DataOutputStream out;
        int port;
        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(cmd);
            port = in.readInt();
        } catch (IOException e) {
            return null;
        }
        try {
            Thread.sleep(ProtocolUtils.WAIT_CONNECTION_TIME);
        } catch (InterruptedException e) {
            //oops
            //continue
        }

        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < clientNumber; i++) {
            Thread thread = new Thread(() -> getClient(cmd, elementNumber, requestNumber, delay).start(ip, port));
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join(ProtocolUtils.MAX_TIME_WAIT);
            } catch (InterruptedException e) {
                //fail
                //witing for next thread
            }
        }

        try {
            out.writeInt(cmd);
        } catch (IOException e) {
            //wrong info
            return null;
        }
        double[] res = new double[3];
        res[0] = ProtocolUtils.averageFromList(TimeCounter.getTimes());
        try {
            res[1] = in.readDouble();
            res[2] = in.readDouble();
        } catch (IOException e) {
            //wrong info
            return null;
        }
        return res;
    }

}
