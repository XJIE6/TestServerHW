package ru.spbau.mit.testserver.utils;

import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class ProtocolUtils {
    public static final int MESSAGE_SIZE = 1 << 21;
    public static final int RUN_TCP_1 = 0;
    public static final int RUN_TCP_2 = 1;
    public static final int RUN_TCP_3 = 2;
    public static final int RUN_TCP_4 = 3;
    public static final int RUN_UDP_1 = 4;
    public static final int RUN_UDP_2 = 5;
    public static final int EXIT = 6;
    public static final int SERVER_PORT = 12345;

    public static List<Integer> randomList(int elementNumber) {
        Random random = new Random();
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < elementNumber; i++) {
            res.add(random.nextInt());
        }
        return res;
    }


    public static byte[] listToBytes(List<Integer> list) {
        ByteBuffer bb = ByteBuffer.allocate((list.size() + 1) * 4);
        IntBuffer ib = bb.asIntBuffer();
        ib.put(list.size());
        list.forEach(ib::put);
        return bb.array();
    }

    public static List<Integer> bytesToList(byte[] bytes) {
        IntBuffer ib = ByteBuffer.wrap(bytes).asIntBuffer();
        int size = ib.get();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < size; ++i) {
            list.add(ib.get());
        }
        return list;
    }

    public static double averageFromList (List<Long> list) {
        return list.stream().mapToLong(Long::longValue).average().orElse(0);
    }
}
