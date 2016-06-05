package ru.spbau.mit.testserver.client;

import ru.spbau.mit.testserver.server.*;
import ru.spbau.mit.testserver.utils.ProtocolUtils;
import ru.spbau.mit.testserver.utils.TimeCounter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static ru.spbau.mit.testserver.utils.ProtocolUtils.*;

public class MainClient {
    private static final int PORT = 12345;
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
        socket.connect(new InetSocketAddress(ip, PORT));
    }

    double[] runServer(int cmd, int clientNumber, int elementNumber, int requestNumber, long delay) {
        TimeCounter.resetTime();
        int port = 11111;
        try {
            socket.getOutputStream().write(cmd);
            socket.getOutputStream().flush();
            //port = socket.getInputStream().read();
        } catch (IOException e) {
            return null;
        }
        System.out.print(port);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.print(port);
        ExecutorService threadPool = Executors.newFixedThreadPool(4);
        for (int i = 0; i < clientNumber; i++) {
            threadPool.execute(() -> {
                System.out.println("executed");
                getClient(cmd, elementNumber, requestNumber, delay).start(ip, port);
                }
            );
        }
        threadPool.shutdown();

        System.out.println("shutted");
        try {
            threadPool.awaitTermination(1, TimeUnit.HOURS);
        } catch (InterruptedException e) {
        }

        System.out.println("awaited");
        try {
            socket.getOutputStream().write(cmd);
            socket.getOutputStream().flush();
        } catch (IOException e) {
        }
        double[] res = new double[3];
        res[0] = ProtocolUtils.averageFromList(TimeCounter.getTimes());
        try {
            res[1] = new DataInputStream(socket.getInputStream()).readDouble();
            res[2] = new DataInputStream(socket.getInputStream()).readDouble();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

}
