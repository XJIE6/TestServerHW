package ru.spbau.mit.testserver.server;

public abstract class Server {
    abstract void start();
    abstract int getPort();
    abstract void close();
}
