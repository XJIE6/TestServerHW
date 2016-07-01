package ru.spbau.mit.kravchenkoyura.testserver.utils;

import com.google.protobuf.InvalidProtocolBufferException;
import ru.spbau.mit.kravchenkoyura.testserver.Protocol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


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
    public static final int MAX_TIME_WAIT = 5000;
    public static final int WAIT_CONNECTION_TIME = 500;

    public static List<Integer> randomList(int elementNumber) {
        Random random = new Random();
        List<Integer> res = new ArrayList<>();
        for (int i = 0; i < elementNumber; i++) {
            res.add(random.nextInt());
        }
        return res;
    }

    public static double averageFromList (List<Long> list) {
        return list.stream().mapToLong(Long::longValue).filter(i -> i < MAX_TIME_WAIT).average().orElse(0);
    }

    public static byte[] listToBytes (List <Integer> list) {
        byte[] message = Protocol.Array.newBuilder().addAllValue(list).build().toByteArray();
        ByteBuffer bb = ByteBuffer.allocate(message.length + Integer.SIZE / Byte.SIZE);
        bb.putInt(message.length);
        bb.put(message);
        return bb.array();
    }
    public static List<Integer> bytesToList (byte[] bytes) throws InvalidProtocolBufferException {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        int size = bb.getInt();
        if (size <= 0) {
            return new ArrayList<>();
        }
        return bytesToListWithSize(bb, size);
    }

    public static List<Integer> bytesToListWithSize (ByteBuffer bb, int size) throws InvalidProtocolBufferException {
        byte[] message = new byte[size];
        bb.get(message);
        return new ArrayList<>(Protocol.Array.parseFrom(message).getValueList());
    }

}
