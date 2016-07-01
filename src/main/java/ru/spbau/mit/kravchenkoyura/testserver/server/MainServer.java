package ru.spbau.mit.kravchenkoyura.testserver.server;

import ru.spbau.mit.kravchenkoyura.testserver.utils.ArraySorter;
import ru.spbau.mit.kravchenkoyura.testserver.utils.ProtocolUtils;
import ru.spbau.mit.kravchenkoyura.testserver.utils.TimeCounter;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Это главный сервер
//Запускает/останавливает нужные сервера


public class MainServer {
    private Socket socket;
    public MainServer() throws IOException {
        socket = new ServerSocket(ProtocolUtils.SERVER_PORT).accept();
    }
    public void run() {
        Server server = null;
        while (!socket.isClosed()) {
            try {
                DataInputStream in = new DataInputStream(socket.getInputStream());
                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                int cmd = in.readInt();
                switch (cmd) {
                    case ProtocolUtils.EXIT:
                        return;
                    case ProtocolUtils.RUN_TCP_1:
                        server = new TCPServer1();
                        break;
                    case ProtocolUtils.RUN_TCP_2:
                        server = new TCPServer2();
                        break;
                    case ProtocolUtils.RUN_TCP_3:
                        server = new TCPServer3();
                        break;
                    case ProtocolUtils.RUN_TCP_4:
                        server = new TCPServer4();
                        break;
                    case ProtocolUtils.RUN_UDP_1:
                        server = new UDPServer1();
                        break;
                    case ProtocolUtils.RUN_UDP_2:
                        server = new UDPServer2();
                        break;
                }
                TimeCounter.resetTime();
                ArraySorter.resetTime();

                out.writeInt(server.getPort());
                new Thread(server::start).start();

                int end = in.readInt();
                assert (cmd == end);
                try {
                    Thread.sleep(ProtocolUtils.WAIT_CONNECTION_TIME);
                } catch (InterruptedException e) {
                    //oops
                }
                server.close();
                try {
                    Thread.sleep(ProtocolUtils.WAIT_CONNECTION_TIME);
                } catch (InterruptedException e) {
                    //oops
                }

                out.writeDouble(ProtocolUtils.averageFromList(TimeCounter.getTimes()));
                out.writeDouble(ProtocolUtils.averageFromList(ArraySorter.getTimes()));

            } catch (IOException e) {
                //fail
                continue;
            }
        }
    }
    public static void main(String[] args) throws IOException {
        new MainServer().run();
    }
}
