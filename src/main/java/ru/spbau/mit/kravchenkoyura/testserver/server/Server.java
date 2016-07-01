package ru.spbau.mit.kravchenkoyura.testserver.server;

import java.io.IOException;

public abstract class Server {
    public abstract void start();
    public abstract int getPort();
    public abstract void close() throws IOException;
}
