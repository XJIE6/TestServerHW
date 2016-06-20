package ru.spbau.mit.testserver.server;

import ru.spbau.mit.testserver.utils.ArraySorter;
import ru.spbau.mit.testserver.utils.ProtocolUtils;
import ru.spbau.mit.testserver.utils.TimeCounter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.LineNumberReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static ru.spbau.mit.testserver.utils.ProtocolUtils.*;

public class MainServer {
    Socket socket;
    MainServer() throws IOException {
        socket = new ServerSocket(ProtocolUtils.SERVER_PORT).accept();
    }
    void run() {
        Server server = null;
        while (!socket.isClosed()) {
            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                int cmd = in.readInt();
                switch (cmd) {
                    case EXIT:
                        return;
                    case RUN_TCP_1:
                        server = new TCPServer1();
                        System.out.println("SERVEROK");
                        break;
                    case RUN_TCP_2:
                        server = new TCPServer2();
                        break;
                    case RUN_TCP_3:
                        server = new TCPServer3();
                        break;
                    case RUN_TCP_4:
                        server = new TCPServer4();
                        break;
                    case RUN_UDP_1:
                        server = new UDPServer1();
                        break;
                    case RUN_UDP_2:
                        server = new UDPServer2();
                        break;
                }
                TimeCounter.resetTime();
                ArraySorter.resetTime();
                out.writeInt(server.getPort());
                new Thread(server::start).start();
                int end = in.readInt();
                assert (cmd == end);
                server.close();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    //oops
                }

                out.writeDouble(ProtocolUtils.averageFromList(TimeCounter.getTimes()));
                out.writeDouble(ProtocolUtils.averageFromList(ArraySorter.getTimes()));

            } catch (IOException e) {
                e.printStackTrace();
                if (!socket.isClosed()) {
                    try {
                        socket.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        //checked closing
                    }
                }
                break;
            }
        }
    }
    public static void main(String[] args) throws IOException {
        new MainServer().run();
    }
}
