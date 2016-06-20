package ru.spbau.mit.testserver.server;


import ru.spbau.mit.testserver.utils.ArraySorter;
import ru.spbau.mit.testserver.utils.ProtocolUtils;
import ru.spbau.mit.testserver.utils.TimeServerSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by YuryKravchenko on 17/06/16.
 */
public abstract class TCPServer extends Server{
    protected ServerSocket serverSocket;
    TCPServer() throws IOException {
        serverSocket = new TimeServerSocket(0);
    }
    public int getPort() {
        return serverSocket.getLocalPort();
    }
    public void close() throws IOException {
        serverSocket.close();
    }
    protected void whileHandle(Socket socket) {
        while (!socket.isClosed()) {
            handle(socket);
        }
    }
    protected void handle(Socket socket) {

        System.out.println("handled");
        try {
            /*DataInputStream in = new DataInputStream(socket.getInputStream());
            int count = in.readInt();
            List<Integer> arr = new ArrayList<>();
            for (int i = 0; i < count; ++i) {
                arr.add(in.readInt());
            }
            ArraySorter.sort(arr);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.writeInt(arr.size());
            for (Integer i : arr) {
                out.writeInt(i);
            }
            out.flush();*/
            byte[] bytes = new byte[ProtocolUtils.MESSAGE_SIZE];
            socket.getInputStream().read(bytes);
            socket.getOutputStream().write(ProtocolUtils.listToBytes(ArraySorter.sort(ProtocolUtils.bytesToList(bytes))));
            socket.getOutputStream().flush();

            System.out.println("ended");
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e1) {
                e.printStackTrace();
                //do nothing, checked closing
            }
        }
    }

}
