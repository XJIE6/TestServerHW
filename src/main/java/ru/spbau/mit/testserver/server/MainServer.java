package ru.spbau.mit.testserver.server;

import ru.spbau.mit.testserver.utils.ArraySorter;
import ru.spbau.mit.testserver.utils.ProtocolUtils;
import ru.spbau.mit.testserver.utils.TimeCounter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import static ru.spbau.mit.testserver.utils.ProtocolUtils.*;

public class MainServer {
    Socket socket;
    MainServer() throws IOException {
        socket = new ServerSocket(12345).accept();
    }
    void run() throws IOException {
        Server server = null;
        while (!socket.isClosed()) {
            int cmd = socket.getInputStream().read();
            switch(cmd) {
                case EXIT:
                    return;
                case RUN_TCP_1:
                    server = new TCPServer1();
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
            //socket.getOutputStream().write(server.getPort());
            //socket.getOutputStream().flush();
            final Server finalServer = server;
            Thread thread = new Thread(() -> finalServer.start());
            thread.setDaemon(true);
            thread.start();
            socket.getInputStream().read();
            server.close();
            new DataOutputStream(socket.getOutputStream()).writeDouble(ProtocolUtils.averageFromList(TimeCounter.getTimes()));
            new DataOutputStream(socket.getOutputStream()).writeDouble(ProtocolUtils.averageFromList(ArraySorter.getTimes()));
            socket.getOutputStream().flush();
        }
    }
    public static void main(String[] args) throws IOException {
        new MainServer().run();
    }
}
