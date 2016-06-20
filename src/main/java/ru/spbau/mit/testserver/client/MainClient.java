package ru.spbau.mit.testserver.client;

import ru.spbau.mit.testserver.server.*;
import ru.spbau.mit.testserver.utils.ProtocolUtils;
import ru.spbau.mit.testserver.utils.TimeCounter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static ru.spbau.mit.testserver.utils.ProtocolUtils.*;

public class MainClient {

    private Socket socket;
    private String ip;
    private static Client getClient(int cmd, int elementNumber, int requestNumber, long delay) {
        switch(cmd) {
            case RUN_TCP_1:
                return new TCPClient(elementNumber, requestNumber, 1, delay);
            case RUN_TCP_2:
                return new TCPClient(elementNumber, requestNumber, 1, delay);
            case RUN_TCP_3:
                return new TCPClient(elementNumber, requestNumber, 1, delay);
            case RUN_TCP_4:
                return new TCPClient(elementNumber, 1, requestNumber, delay);
            case RUN_UDP_1:
                return new UDPClient(elementNumber, requestNumber, delay);
            case RUN_UDP_2:
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
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
        System.out.print(port);
        //ExecutorService threadPool = Executors.newFixedThreadPool(4);
        ArrayList<Thread> threads = new ArrayList<>();
        for (int i = 0; i < clientNumber; i++) {
            Thread thread = new Thread(() -> getClient(cmd, elementNumber, requestNumber, delay).start(ip, port));
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                //fail
                //witing for next thread
            }
        }

        //System.out.println("awaited");
        try {
            out.writeInt(cmd);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        double[] res = new double[3];
        res[0] = ProtocolUtils.averageFromList(TimeCounter.getTimes());
        try {
            res[1] = in.readDouble();
            res[2] = in.readDouble();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return res;
    }

}
