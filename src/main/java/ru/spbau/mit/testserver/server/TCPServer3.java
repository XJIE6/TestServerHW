package ru.spbau.mit.testserver.server;

import com.google.protobuf.InvalidProtocolBufferException;
import ru.spbau.mit.testserver.utils.ArraySorter;
import ru.spbau.mit.testserver.utils.ProtocolUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TCPServer3 extends Server {
    private final static int THREADS_COUNT = 4;
    private final static int PORT = 11111;

    private final List<SocketChannel> channels = new LinkedList<>();
    private Selector selector;
    private ServerSocketChannel serverSocket;
    private ExecutorService threadPool;

    TCPServer3() throws IOException {
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
        }
    }

    @Override
    public void start() {
        try {
            while (serverSocket.isOpen()) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();

                    if (selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = ((ServerSocketChannel) selectionKey.channel()).accept();
                        socketChannel.configureBlocking(false);
                        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE, new Info());
                        channels.add(socketChannel);
                    }

                    if (selectionKey.isReadable()) {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        Info client = (Info) selectionKey.attachment();

                        if (client.size.hasRemaining()) {
                            socketChannel.read(client.size);
                            if (!client.size.hasRemaining()) {
                                client.size.flip();
                                int lol = client.size.getInt();
                                System.out.println(lol);
                                client.get = ByteBuffer.allocate(lol);
                            }
                        }

                        if (client.get != null) {
                            socketChannel.read(client.get);
                            if (!client.get.hasRemaining()) {
                                threadPool.submit(() -> requestHandler(client));
                            }
                            client.get = null;
                            client.size.clear();
                        }
                    }
                    if (selectionKey.isWritable()) {
                        final SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        final Info client = (Info) selectionKey.attachment();

                        if (client.put != null) {
                            socketChannel.write(client.put);
                            if (!client.put.hasRemaining()) {
                                client.put = null;
                            }
                        }
                    }
                    iterator.remove();
                }
            }
        } catch (IOException e) {
        }
    }

    private void requestHandler(final Info info) {
        /*try {
            info.put = ByteBuffer.wrap(ProtocolUtils.listToByte(ArraySorter.sort(ProtocolUtils.byteToList(info.get.array()))));
        } catch (InvalidProtocolBufferException e) {
        }*/
    }

    private static class Info {
        private ByteBuffer size = ByteBuffer.allocate(4);
        private ByteBuffer get = null;
        private ByteBuffer put = null;
    }
}
