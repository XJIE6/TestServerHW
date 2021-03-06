package ru.spbau.mit.kravchenkoyura.testserver.server;

import com.google.protobuf.InvalidProtocolBufferException;
import ru.spbau.mit.kravchenkoyura.testserver.utils.ProtocolUtils;
import ru.spbau.mit.kravchenkoyura.testserver.utils.ArraySorter;
import ru.spbau.mit.kravchenkoyura.testserver.utils.TimeCounter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer3 implements Server {
    private final static int THREADS_COUNT = 4;
    private final static int PORT = 11111;

    private final List<SocketChannel> channels = new LinkedList<>();
    private Selector selector;
    private ServerSocketChannel serverSocket;
    private ExecutorService threadPool;

    public TCPServer3() throws IOException {
        super();
        selector = Selector.open();
        serverSocket = ServerSocketChannel.open();
        serverSocket.socket().bind(new InetSocketAddress(PORT));
        serverSocket.configureBlocking(false);
        serverSocket.register(selector, SelectionKey.OP_ACCEPT);
        threadPool = Executors.newFixedThreadPool(THREADS_COUNT);
    }

    @Override
    public int getPort() {
        return PORT;
    }

    public void close() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            //already closed
        }
    }

    @Override
    public void start() {
        while (serverSocket.isOpen()) {
            try {
                selector.select();
            } catch (IOException e) {
                //fail
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                if (selectionKey.isAcceptable()) {
                    try {
                        SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
                        socketChannel.configureBlocking(false);
                        Info info = new Info();
                        info.start();
                        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, info);
                        channels.add(socketChannel);
                    } catch (ClosedChannelException e) {
                        continue;
                    } catch (IOException e) {
                        continue;
                    }
                }
                if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    Info client = (Info) selectionKey.attachment();
                    if (client.size.hasRemaining()) {
                        try {
                            socketChannel.read(client.size);
                            if (!client.size.hasRemaining()) {
                                client.size.flip();
                                client.length = client.size.getInt();
                                client.get = ByteBuffer.allocate(client.length);
                            }
                        } catch (IOException e) {
                            client.fail();
                            continue;
                        }
                    }
                    if (!client.readed) {
                        try {
                            socketChannel.read(client.get);
                            if (!client.get.hasRemaining()) {
                                threadPool.submit(() -> requestHandler(client));
                                client.readed = true;
                                client.size.clear();
                            }
                        } catch (IOException e) {
                            client.fail();
                            continue;
                        }
                    }
                }
                if (selectionKey.isWritable()) {
                    Info client = null;
                    try {
                        final SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        client = (Info) selectionKey.attachment();
                        if (client.put != null) {
                            socketChannel.write(client.put);
                            if (!client.put.hasRemaining()) {
                                client.put = null;
                            }
                        }
                    } catch (IOException e) {
                        client.fail();
                        continue;
                    }
                }
                iterator.remove();
            }
        }
        threadPool.shutdown();
    }

    private void requestHandler(final Info info) {
        try {
            byte[] arr = info.get.array();
            ByteBuffer bb = ByteBuffer.wrap(arr);
            List<Integer> list = ProtocolUtils.bytesToListWithSize(bb, info.length);
            byte[] bytes = ProtocolUtils.listToBytes(ArraySorter.sort(list));
            info.put = ByteBuffer.wrap(bytes);
            info.readed = false;
            info.end();
        } catch (InvalidProtocolBufferException e) {
            info.fail();
            //fail
            //continue
        }
    }

    private static class Info implements TimeCounter {
        private int length = 0;
        private boolean readed = false;
        private boolean failed = false;
        private ByteBuffer size = ByteBuffer.allocate(4);
        private ByteBuffer get = null;
        private ByteBuffer put = null;

        private long startTime;

        private void fail() {
            failed = true;
        }


        private void start() {
            failed = false;
            startTime = System.currentTimeMillis();
        }

        private void end() {
            if (!failed) {
                times.add(System.currentTimeMillis() - startTime);
            }
            start();
        }

    }
}
