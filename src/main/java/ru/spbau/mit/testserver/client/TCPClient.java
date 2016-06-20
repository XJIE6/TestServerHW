package ru.spbau.mit.testserver.client;


import ru.spbau.mit.testserver.utils.ProtocolUtils;
import ru.spbau.mit.testserver.utils.TimeSocket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by YuryKravchenko on 18/06/16.
 */
public class TCPClient extends Client{
    private int connectionNumber;
    public TCPClient(int elementNumber, int requestNumber, int connectionNumber, long delay) {
        super(elementNumber, requestNumber, delay);
        this.connectionNumber = connectionNumber;
    }

    @Override
    public void start(String host, int port) {
        System.out.println(port);
        for (int i = 0; i < connectionNumber; i++) {
            System.out.print(host);
            System.out.print(port);
            try(Socket socket = new TimeSocket(host, port)) {
                for (int j = 0; j < requestNumber; j++) {
                    //DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                    //out.writeInt(elementNumber);
                    //ArrayList<Integer> arr = new ArrayList<>();
                    //for (int k = 0; k < elementNumber; ++k) {
                    //    arr.add(random.nextInt());
                    //}
                    List<Integer> list = ProtocolUtils.randomList(elementNumber);
                    socket.getOutputStream().write(ProtocolUtils.listToBytes(list));
                    socket.getOutputStream().flush();
                    //out.writeInt(arr.size());
                    //for (Integer k : arr) {
                    //    out.writeInt(k);
                    //}
                    //out.flush();
                    list.sort(Comparator.naturalOrder());
                    byte[] bytes = new byte[ProtocolUtils.MESSAGE_SIZE];
                    socket.getInputStream().read(bytes);
                    List ans = ProtocolUtils.bytesToList(bytes);
                    assert (list.equals(ans));
                    //DataInputStream in = new DataInputStream(socket.getInputStream());
                    //assert(in.readInt() == elementNumber);
                    //for (Integer k : arr) {
                    //    assert(in.readInt() == k);
                    //}
                    //System.out.println("OK");
                    Thread.sleep(delay);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
                break;
                //fail connection;
            } catch (IOException e) {
                e.printStackTrace();
                //connection problem
                //go to next connection
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
                //close because of interrupt
            }
        }
    }
}
