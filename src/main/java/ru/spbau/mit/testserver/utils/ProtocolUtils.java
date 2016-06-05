package ru.spbau.mit.testserver.utils;

import com.google.protobuf.InvalidProtocolBufferException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;


public class ProtocolUtils {
    public static final int MESSAGE_SIZE = 1 << 20;
    public static final int RUN_TCP_1 = 0;
    public static final int RUN_TCP_2 = 1;
    public static final int RUN_TCP_3 = 2;
    public static final int RUN_TCP_4 = 3;
    public static final int RUN_UDP_1 = 4;
    public static final int RUN_UDP_2 = 5;
    public static final int EXIT = 6;

    public static byte[] listToByte (List <Integer> list) {
        //return Protocol.Array.newBuilder().addAllValue(list).build().toByteArray();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        list.stream().forEach(baos::write);
        return baos.toByteArray();
    }
    public static List<Integer> byteTolist (byte[] bytes) throws InvalidProtocolBufferException {
        //return Protocol.Array.parseFrom(bytes).getValueList();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        List<Integer> list = new ArrayList<>();
        while (bais.available() > 0) {
            list.add(bais.read());
        }
        return list;
    }
    public static DatagramPacket byteToPacket (byte[] bytes) {
        bytes = ByteBuffer.allocate(bytes.length + Integer.SIZE / Byte.SIZE).putInt(bytes.length).put(bytes).array();
        return new DatagramPacket(bytes, bytes.length);
    }
    public static byte[] packetToByte(DatagramPacket packet) {
        ByteBuffer buff = ByteBuffer.wrap(packet.getData());
        byte[] bytes = new byte[buff.getInt()];
        buff.get(bytes);
        return bytes;
    }
    public static double averageFromList (List<Long> list) {
        return list.stream().mapToLong(Long::longValue).average().orElse(-1);
    }
}
